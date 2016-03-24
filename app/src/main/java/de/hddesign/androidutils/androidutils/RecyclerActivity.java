package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter.ItemClickListener;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter.ItemViewHolder;
import de.hddesign.androidutils.androidutils.base.DrawerActivity;
import de.hddesign.androidutils.androidutils.model.MainItem;
import de.hddesign.androidutils.androidutils.utils.Preferences;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutDialog;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class RecyclerActivity extends DrawerActivity implements ItemClickListener {

    private MainAdapter mainAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    private Preferences preferences;
    private GridLayoutManager gridLayoutManager;

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
        return newIntent(RecyclerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        showTitle(R.string.app_name);

        preferences = new Preferences(this);

        mainAdapter = new MainAdapter();

        if (preferences.getMainItems() != null)
            mainAdapter.addItems(preferences.getMainItems());

        mainAdapter.setItemClickListener(this);

        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                mainAdapter.reorderItems(fromPosition, toPosition);
                mainAdapter.notifyItemMoved(fromPosition, toPosition);

                int count;
                if (fromPosition < toPosition) {
                    count = toPosition - fromPosition;
                    mainAdapter.notifyItemRangeChanged(fromPosition, count + 1);
                } else {
                    count = fromPosition - toPosition;
                    mainAdapter.notifyItemRangeChanged(toPosition, count + 1);
                }

                preferences.setMainItems(mainAdapter.getItems());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final MainItem item = mainAdapter.getItem(position);
                mainAdapter.removeItem(item);
                mainAdapter.notifyItemRemoved(position);

                mainAdapter.rewriteIndexes();

                int count = mainAdapter.getItemCount() - position;
                mainAdapter.notifyItemRangeChanged(position, count);

                preferences.setMainItems(mainAdapter.getItems());

                Snackbar snackbar = Snackbar
                        .make(mainContent, R.string.deleted_entry, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_redo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainAdapter.addItemAtPosition(position, item);
                                mainAdapter.notifyItemInserted(position);
                                mainAdapter.rewriteIndexes();

                                int count = mainAdapter.getItemCount() - position;
                                mainAdapter.notifyItemRangeChanged(position, count);

                                preferences.setMainItems(mainAdapter.getItems());

                                Snackbar snackbar1 = Snackbar.make(mainContent, R.string.restored_entry, Snackbar.LENGTH_SHORT);

                                View snackbarLayout = snackbar1.getView();
                                snackbarLayout.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.snackbar_restore), Mode.SRC_IN));

                                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_undo, 0, 0, 0);
                                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                                textView.setGravity(Gravity.CENTER_VERTICAL);

                                snackbar1.show();
                            }
                        });

                snackbar.setActionTextColor(Color.WHITE);

                View snackbarLayout = snackbar.getView();
                snackbarLayout.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.snackbar_delete), Mode.SRC_IN));

                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);
                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                textView.setGravity(Gravity.CENTER_VERTICAL);

                snackbar.show();
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


        gridLayoutManager = new GridLayoutManager(this, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 2;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager.setSpanCount(2);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(4);
        }

        gridLayoutManager.requestLayout();
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_recycler_view;
    }
}
