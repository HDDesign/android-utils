package de.hddesign.androidutils.androidutils.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class ScaleErrorImageViewTarget
        extends GlideDrawableImageViewTarget {
    ScaleType mErrorScaleType;

    public ScaleErrorImageViewTarget(ImageView view, ScaleType errorScaleType) {
        super(view);
        mErrorScaleType = errorScaleType;
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        ImageView imageView = getView();
        imageView.setScaleType(mErrorScaleType);
        Log.d("SCLS", String.format("onLoadFailed: %s", e));

        super.onLoadFailed(e, errorDrawable);
    }

    @NonNull
    public static ScaleErrorImageViewTarget errorCenter(ImageView imageView) {
        return new ScaleErrorImageViewTarget(imageView, ScaleType.CENTER);
    }
}