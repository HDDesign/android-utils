package de.hddesign.androidutils.androidutils.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<DATA> extends RecyclerView.ViewHolder {

    public int position;

    public BaseViewHolder(ViewGroup parent, int viewItemResourceId) {
        super(LayoutInflater.from(parent.getContext()).inflate(viewItemResourceId, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public abstract void bindData(DATA data, int position);

    public void unbindData() {

    }
}
