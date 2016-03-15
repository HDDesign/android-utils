package de.hddesign.androidutils.androidutils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter.ItemClickListener;
import de.hddesign.androidutils.androidutils.adapter.MainAdapter.ItemViewHolder;
import de.hddesign.androidutils.androidutils.base.BaseFragment;
import de.hddesign.androidutils.androidutils.model.MainItem;

public class MainFragment extends BaseFragment<MainActivity> implements ItemClickListener {

    private MainAdapter mainAdapter;
    private ItemTouchHelper itemTouchHelper;
    private ArrayList<MainItem> items;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<>();
        items.add(new MainItem(0, "Test 0", 0));
        items.add(new MainItem(1, "Test 1", 0));
        items.add(new MainItem(2, "Test 2", 0));
        items.add(new MainItem(3, "Test 3", 0));
        items.add(new MainItem(4, "Test 4", 0));
        items.add(new MainItem(5, "Test 5", 0));

        mainAdapter = new MainAdapter();
        mainAdapter.addItems(items);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void itemSelected(MainItem item, int position) {
        startActivity(ColorPickerActivity.newIntent());
    }

    @Override
    public void onStartItemDrag(ItemViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }
}
