package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter.ItemClickListener;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter.ItemViewHolder;
import de.hddesign.androidutils.androidutils.base.BaseActivity;
import de.hddesign.androidutils.androidutils.model.MainItem;
import de.hddesign.androidutils.androidutils.utils.Preferences;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutDialog;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class MainActivity extends BaseActivity implements ItemClickListener {

    private MainAdapter mainAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private Preferences preferences;

    @OnClick(R.id.fab)
    public void onAddClicked() {
        final TextInputLayoutDialog tilDialog = new TextInputLayoutDialog(this, R.style.AppCompatAlertDialogStyle);

        tilDialog.setTilHint(getString(R.string.item_name));
        tilDialog.setTitle(R.string.title_mainitem_create);
        tilDialog.setTilErrorHelper(TilErrorType.MIN_MAX_LENGTH, R.string.error_min_max_length);
        tilDialog.getTilErrorHelper().setMinLength(3);
        tilDialog.getTilErrorHelper().setMaxLength(15);

        tilDialog.setOnPositiveButtonClickedListener(R.string.create, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tilDialog.getTilErrorHelper().hasError()) {
                    String name = tilDialog.getInputText();
                    mainAdapter.addItem(new MainItem(mainAdapter.getItemCount() + 1, name, Color.WHITE));
                    mainAdapter.notifyItemInserted(mainAdapter.getItemCount());
                    preferences.setMainItems(mainAdapter.getItems());
                    tilDialog.dismiss();
                }
            }
        });
        tilDialog.show();
    }

    public static Intent newIntent() {
        return newIntent(MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showTitle(R.string.app_name);

        preferences = new Preferences(this);

        mainAdapter = new MainAdapter();

        if (preferences.getMainItems() != null)
            mainAdapter.addItems(preferences.getMainItems());

        mainAdapter.setItemClickListener(this);

        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                mainAdapter.reorderItems(fromPosition, toPosition);
                mainAdapter.notifyItemMoved(fromPosition, toPosition);

                if (fromPosition < toPosition)
                    mainAdapter.notifyItemRangeChanged(fromPosition, mainAdapter.getItemCount());
                else
                    mainAdapter.notifyItemRangeChanged(toPosition, mainAdapter.getItemCount());

                preferences.setMainItems(mainAdapter.getItems());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                MainItem item = mainAdapter.getItem(position);
                mainAdapter.removeItem(item);
                mainAdapter.notifyItemRemoved(position);

                mainAdapter.rewriteIndexes();

                mainAdapter.notifyItemRangeChanged(position, mainAdapter.getItemCount());

                preferences.setMainItems(mainAdapter.getItems());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                }
                if (dy < 0 && !fab.isShown()) {
                    fab.show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void itemSelected(MainItem item, int position) {
        startActivityForResult(ColorPickerActivity.newIntent(item.getIndex() - 1, item.getColor()), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == ColorPickerActivity.RESULT_OK) {
            int index = data.getIntExtra(ColorPickerActivity.INDEX, 0);
            mainAdapter.getItem(index).setColor(data.getIntExtra(ColorPickerActivity.PICKED_COLOR, 0));
            mainAdapter.notifyItemChanged(index);
            preferences.setMainItems(mainAdapter.getItems());
        }
    }

    @Override
    public void onStartItemDrag(ItemViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }

    @Override
    public void itemLongClicked(final MainItem item, final int adapterPosition) {
        final TextInputLayoutDialog tilDialog = new TextInputLayoutDialog(this, R.style.AppCompatAlertDialogStyle);

        tilDialog.setTilHint(getString(R.string.item_name));
        tilDialog.setTitle(R.string.title_mainitem_change);
        tilDialog.setTilErrorHelper(TilErrorType.MIN_MAX_LENGTH, R.string.error_min_max_length);
        tilDialog.getTilErrorHelper().setMinLength(3);
        tilDialog.getTilErrorHelper().setMaxLength(15);
        tilDialog.setInputText(item.getTitle());

        tilDialog.setOnPositiveButtonClickedListener(R.string.change, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tilDialog.getTilErrorHelper().hasError()) {
                    String name = tilDialog.getInputText();
                    item.setTitle(name);
                    mainAdapter.updateItem(item);
                    mainAdapter.notifyItemChanged(adapterPosition);
                    preferences.setMainItems(mainAdapter.getItems());
                    tilDialog.dismiss();
                }
            }
        });
        tilDialog.show();
    }
}
