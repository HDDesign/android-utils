package de.hddesign.androidutils.androidutils.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.BaseViewHolder;
import de.hddesign.androidutils.androidutils.model.MainItem;

public class MainAdapter extends RecyclerView.Adapter<BaseViewHolder<MainItem>> {


    private ArrayList<MainItem> items;
    private ItemClickListener itemClickListener;

    public void addItems(final List<MainItem> items) {
        this.items.clear();
        Collections.sort(items, new Comparator<MainItem>() {
            @Override
            public int compare(MainItem lhs, MainItem rhs) {
                return lhs.getIndex() - rhs.getIndex();
            }
        });
        this.items.addAll(items);
    }

    public void reorderItems(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }

        rewriteIndexes();
    }

    public void addItem(MainItem item) {
        items.add(item);
    }

    public void removeItem(MainItem item) {
        items.remove(item);
    }

    public MainItem getItem(int position) {
        return items.get(position);
    }

    public ArrayList<MainItem> getItems() {
        return items;
    }

    public void rewriteIndexes() {
        int i = 1;
        for (MainItem d : items) {
            d.setIndex(i);
            i++;
        }
    }

    public interface ItemClickListener {
        void itemSelected(MainItem item, int position);

        void onStartItemDrag(ItemViewHolder holder);
    }

    public int updateItem(MainItem item) {
        int i = 0;
        for (MainItem d : items) {
            if (d.getIndex() == item.getIndex()) {
                items.set(i, item);
                return i;
            }
            i++;
        }
        return 0;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MainAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public BaseViewHolder<MainItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<MainItem> holder, int position) {
        holder.unbindData();
        holder.bindData(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends BaseViewHolder<MainItem> {

        private ItemViewHolder holder;
        private MainItem item;

        @Bind(R.id.txt_pos)
        TextView txtPos;

        @Bind(R.id.txt_title)
        TextView txtTitle;

        @Bind(R.id.reorder_item)
        ImageView reorderItem;

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mainitem);
            holder = this;

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.itemSelected(item, getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void bindData(MainItem item, int position) {
            this.item = item;

            txtPos.setText(String.format(itemView.getContext().getString(R.string.lbl_position), item.getIndex()));
            txtTitle.setText(item.getTitle());

            ((CardView) itemView).setCardBackgroundColor(item.getColor());

            reorderItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        itemClickListener.onStartItemDrag(holder);
                    }
                    return false;
                }
            });
        }
    }
}
