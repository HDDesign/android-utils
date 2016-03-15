package de.hddesign.androidutils.androidutils.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;

public class BaseFragment<CONTROLLER> extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutResource = getViewLayoutId();
        if (layoutResource != 0) {
            View root = inflater.inflate(layoutResource, container, false);
            ButterKnife.bind(this, root);
            return root;
        } else {
            TextView textView = new TextView(container.getContext());
            textView.setText("This is an empty fragment. \n\nIt is going to display the content of \n" + getClass().getSimpleName());
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            textView.setTextSize(20);
            return textView;
        }
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    protected CONTROLLER getController() {
        return (CONTROLLER) getActivity();
    }

    protected void showTitle(String title) {
        ((BaseActivity) getController()).showTitle(title);
    }

    protected void showTitle(@StringRes int titleRes) {
        ((BaseActivity) getController()).showTitle(titleRes);
    }

    protected void showSubTitle(String title) {
        ((BaseActivity) getController()).showSubtitle(title);
    }

    protected void showSubTitle(@StringRes int titleRes) {
        ((BaseActivity) getController()).showSubtitle(titleRes);
    }

    @LayoutRes
    protected int getViewLayoutId() {
        return 0;
    }

    public int getTitleRes() {
        return -1;
    }

    @Nullable
    public String getDynamicTitle() {
        return null;
    }
}