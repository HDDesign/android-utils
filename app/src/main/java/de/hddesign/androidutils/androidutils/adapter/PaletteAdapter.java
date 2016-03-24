package de.hddesign.androidutils.androidutils.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.BaseViewHolder;
import de.hddesign.androidutils.androidutils.custom.AspectRatioLayout;
import de.hddesign.androidutils.androidutils.model.PaletteItem;
import de.hddesign.androidutils.androidutils.utils.ScaleErrorImageViewTarget;

public class PaletteAdapter extends RecyclerView.Adapter<BaseViewHolder<PaletteItem>> {

    private final RequestManager glide;
    private ArrayList<PaletteItem> items;
    private ItemClickListener itemClickListener;
    private ArrayList<String> keywords;

    private int aspectWidth = 1;
    private int aspectHeight = 1;

    private int imageResolution = 100;
    private String paletteType = "Muted";

    public PaletteAdapter(RequestManager glide) {
        this.glide = glide;
        items = new ArrayList<>();
        keywords = new ArrayList<>();
    }


    public void addItems(final List<PaletteItem> items) {
        this.items.clear();
        Collections.sort(items, new Comparator<PaletteItem>() {
            @Override
            public int compare(PaletteItem lhs, PaletteItem rhs) {
                return lhs.getIndex() - rhs.getIndex();
            }
        });
        this.items.addAll(items);
    }

    public void addKeywords(Set<String> keywords) {
        this.keywords.clear();
        this.keywords.addAll(keywords);
    }

    public void addItemsWithoutClear(final List<PaletteItem> items) {
        Collections.sort(items, new Comparator<PaletteItem>() {
            @Override
            public int compare(PaletteItem lhs, PaletteItem rhs) {
                return lhs.getIndex() - rhs.getIndex();
            }
        });
        this.items.addAll(items);
    }

    public void reorderItems(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
                items.get(i).setIndex(i + 1);
                items.get(i + 1).setIndex(i + 2);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
                items.get(i).setIndex(i + 1);
                items.get(i - 1).setIndex(i);
            }
        }
    }

    public void addItemAtPosition(int position, PaletteItem item) {
        items.add(position, item);
    }

    public void removeItem(PaletteItem item) {
        items.remove(item);
    }

    public PaletteItem getItem(int position) {
        return items.get(position);
    }

    public void rewriteIndexes() {
        int i = 1;
        for (PaletteItem d : items) {
            d.setIndex(i);
            i++;
        }
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setPaletteType(String paletteType) {
        this.paletteType = paletteType;
    }

    public interface ItemClickListener {

        void onStartItemDrag(ItemViewHolder holder);

        void onItemClick(String solvedUrl);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public BaseViewHolder<PaletteItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<PaletteItem> holder, int position) {
        holder.unbindData();
        holder.bindData(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends BaseViewHolder<PaletteItem> {

        private ItemViewHolder holder;
        private PaletteItem item;
        private Palette palette;

        @Bind(R.id.txt_pos)
        TextView txtPos;

        @Bind(R.id.txt_title)
        TextView txtTitle;

        @Bind(R.id.reorder_item)
        ImageView reorderItem;

        @Bind(R.id.img_main)
        public ImageView imgMain;

        @Bind(R.id.aspect_ratio_layout)
        AspectRatioLayout aspectRatioLayout;

        private Drawable errorDrawable;
        private String solvedUrl;

        public ItemViewHolder(final ViewGroup parent) {
            super(parent, R.layout.item_palette);
            holder = this;

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadImage(true);
                    itemClickListener.onItemClick(solvedUrl);
                }
            });
        }

        private void loadImage(final boolean skipCache) {
            solvedUrl = String.format(item.getImageUrl(), imageResolution, imageResolution, keywordsToQuery());

            if (palette == null) {
                glide.load(solvedUrl).asBitmap().override(50, 50).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Palette.from(resource).generate(new PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette p) {
                                palette = p;
                                item.setTitle(String.format("#%06X", (0xFFFFFF & getPaletteType(palette))));
                                txtTitle.setText(item.getTitle());
                                errorDrawable = itemView.getContext().getDrawable(R.drawable.ic_broken_image);

                                if (errorDrawable != null)
                                    errorDrawable.mutate().setColorFilter(new PorterDuffColorFilter(getPaletteType(palette), Mode.SRC_IN));

                                if (reorderItem != null)
                                    reorderItem.setColorFilter(new PorterDuffColorFilter(getPaletteType(palette), Mode.SRC_IN));

                                glide.load(solvedUrl).skipMemoryCache(skipCache).error(errorDrawable).centerCrop().placeholder(new ColorDrawable(getPaletteType(palette))).into(ScaleErrorImageViewTarget.errorCenter(imgMain));
                            }
                        });
                    }
                });
            } else {
                item.setTitle(String.format("#%06X", (0xFFFFFF & getPaletteType(palette))));
                txtTitle.setText(item.getTitle());

                if (errorDrawable != null)
                    errorDrawable.mutate().setColorFilter(new PorterDuffColorFilter(getPaletteType(palette), Mode.SRC_IN));

                if (reorderItem != null)
                    reorderItem.setColorFilter(new PorterDuffColorFilter(getPaletteType(palette), Mode.SRC_IN));

                glide.load(solvedUrl).skipMemoryCache(skipCache).error(errorDrawable).centerCrop().placeholder(new ColorDrawable(getPaletteType(palette))).into(ScaleErrorImageViewTarget.errorCenter(imgMain));
            }
        }

        @Override
        public void bindData(PaletteItem i, int position) {
            aspectRatioLayout.setAspectRatio(aspectWidth, aspectHeight);

            this.item = i;

            txtPos.setText(String.format(itemView.getContext().getString(R.string.lbl_position), item.getIndex()));
            txtTitle.setText(item.getTitle());

            loadImage(false);

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

    private String keywordsToQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (String s : keywords) {
            if (!first)
                stringBuilder.append(",");
            stringBuilder.append(s);
            first = false;
        }

        return stringBuilder.toString();
    }

    public int getPaletteType(Palette p) {
        int color = 0;

        if (paletteType.equals("Vibrant"))
            color = p.getVibrantColor(Color.BLACK);

        if (paletteType.equals("Vibrant Dark"))
            color = p.getDarkVibrantColor(Color.BLACK);

        if (paletteType.equals("Vibrant Light"))
            color = p.getLightVibrantColor(Color.BLACK);

        if (paletteType.equals("Muted"))
            color = p.getMutedColor(Color.BLACK);

        if (paletteType.equals("Muted Dark"))
            color = p.getDarkMutedColor(Color.BLACK);

        if (paletteType.equals("Muted Light"))
            color = p.getLightMutedColor(Color.BLACK);

        return color;
    }

    public void setImageResolution(int imageResolution) {
        this.imageResolution = imageResolution;
    }

    public void setAspectWidth(int aspectWidth) {
        this.aspectWidth = aspectWidth;
    }

    public void setAspectHeight(int aspectHeight) {
        this.aspectHeight = aspectHeight;
    }
}
