package de.hddesign.androidutils.androidutils.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.BaseViewHolder;

public class StringAdapter extends RecyclerView.Adapter<BaseViewHolder<String>> {

    private ArrayList<String> strings;


    public StringAdapter() {
        strings = new ArrayList<>();
    }


    public void addStrings(List<String> strings) {
        this.strings.clear();
        this.strings.addAll(strings);
    }

    public void addStringsWithoutClear(List<String> strings) {
        this.strings.addAll(strings);
    }

    public void addString(String string) {
        strings.add(string);
    }

    public void addStringAtPosition(int position, String string) {
        strings.add(position, string);
    }

    public void removeString(String string) {
        strings.remove(string);
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    @Override
    public BaseViewHolder<String> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StringViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<String> holder, int position) {
        holder.unbindData();
        holder.bindData(strings.get(position), position);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public String getString(int position) {
        return strings.get(position);
    }

    public class StringViewHolder extends BaseViewHolder<String> {


        @Bind(R.id.txt_string)
        TextView txtString;

        public StringViewHolder(final ViewGroup parent) {
            super(parent, R.layout.item_string);
        }

        @Override
        public void bindData(String s, int position) {
            txtString.setText(s);
        }
    }
}
