package de.hddesign.androidutils.androidutils.utils;

import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;

public class LableSliderSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    public interface SeekbarCallback {
        void onProgressChanged(long id, int value, boolean fromUser);
        void onStopTrackingTouch(long id, int value);
    }

    private Context context;
    private TextView label;
    private int resourceId;
    private long id;

    private SeekbarCallback seekbarCallback;

    public void setSeekbarCallback(SeekbarCallback seekbarCallback) {
        this.seekbarCallback = seekbarCallback;
    }

    public LableSliderSeekBarChangeListener(Context context, TextView label, int resourceId, int initValue, long id) {
        this.context = context;
        this.label = label;
        this.resourceId = resourceId;
        this.id = id;

        label.setText(String.format(context.getString(resourceId), initValue));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        label.setText(String.format(context.getString(resourceId), progress));

        if(seekbarCallback != null)
            seekbarCallback.onProgressChanged(id, progress, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        label.setText(String.format(context.getString(resourceId), seekBar.getProgress()));
        if(seekbarCallback != null)
            seekbarCallback.onStopTrackingTouch(id, seekBar.getProgress());
    }
}
