package de.hddesign.androidutils.androidutils.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.BaseViewHolder;
import de.hddesign.androidutils.androidutils.network.dto.PhotoV1Dto;

public class MaterialAdapter extends RecyclerView.Adapter<BaseViewHolder<PhotoV1Dto>> {

    private final RequestManager glide;
    private ArrayList<PhotoV1Dto> items;
    private MaterialClickListener materialClickListener;

    public interface MaterialClickListener {
        void onItemClicked(PhotoV1Dto photoV1Dto, ImageView imgMain, TextView title);
    }

    public void setMaterialClickListener(MaterialClickListener materialClickListener) {
        this.materialClickListener = materialClickListener;
    }

    public MaterialAdapter(RequestManager glide) {
        this.glide = glide;
    }

    public void setItems(ArrayList<PhotoV1Dto> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder<PhotoV1Dto> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MaterialViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<PhotoV1Dto> holder, int position) {
        holder.unbindData();
        holder.bindData(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class MaterialViewHolder extends BaseViewHolder<PhotoV1Dto> {

        @Bind(R.id.img_main)
        ImageView imgMain;

        @Bind(R.id.title)
        TextView title;

        public MaterialViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_material);
        }

        @Override
        public void bindData(final PhotoV1Dto photoV1Dto, final int position) {
            title.setText(photoV1Dto.getTitle());
            glide.load(photoV1Dto.getUrl()).centerCrop().into(imgMain);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (materialClickListener != null)
                        materialClickListener.onItemClicked(photoV1Dto, imgMain, title);
                }
            });
        }
    }
}
