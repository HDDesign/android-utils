package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.base.BaseActivity;
import de.hddesign.androidutils.androidutils.custom.ColorView;
import de.hddesign.androidutils.androidutils.custom.ColorView.ColorViewCallback;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener.SeekbarCallback;

public class ColorPickerActivity extends BaseActivity implements SeekbarCallback, ColorViewCallback {

    @Bind(R.id.colorView)
    ColorView colorview;

    @Bind(R.id.txt_h)
    TextView txtH;

    @Bind(R.id.txt_s)
    TextView txtS;

    @Bind(R.id.txt_v)
    TextView txtV;

    @Bind(R.id.txt_r)
    TextView txtR;

    @Bind(R.id.txt_g)
    TextView txtG;

    @Bind(R.id.txt_b)
    TextView txtB;

    @Bind(R.id.seekbar_h)
    SeekBar seekbarH;

    @Bind(R.id.seekbar_s)
    SeekBar seekbarS;

    @Bind(R.id.seekbar_v)
    SeekBar seekbarV;

    @Bind(R.id.seekbar_r)
    SeekBar seekbarR;

    @Bind(R.id.seekbar_g)
    SeekBar seekbarG;

    @Bind(R.id.seekbar_b)
    SeekBar seekbarB;

    @OnClick(R.id.iv_decrease_h)
    public void decreaseH() {
        seekbarH.setProgress(seekbarH.getProgress() - 5);
    }

    @OnClick(R.id.iv_increase_h)
    public void increaseH() {
        seekbarH.setProgress(seekbarH.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_s)
    public void decreaseS() {
        seekbarS.setProgress(seekbarS.getProgress() - 5);
    }

    @OnClick(R.id.iv_increase_s)
    public void increaseS() {
        seekbarS.setProgress(seekbarS.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_v)
    public void decreaseV() {
        seekbarV.setProgress(seekbarV.getProgress() - 5);
    }

    @OnClick(R.id.iv_increase_v)
    public void increaseV() {
        seekbarV.setProgress(seekbarV.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_r)
    public void decreaseR() {
        seekbarR.setProgress(seekbarR.getProgress() - 5);
    }

    @OnClick(R.id.iv_increase_r)
    public void increaseR() {
        seekbarR.setProgress(seekbarR.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_g)
    public void decreaseG() {
        seekbarG.setProgress(seekbarG.getProgress() - 5);
    }

    @OnClick(R.id.iv_increase_g)
    public void increaseG() {
        seekbarG.setProgress(seekbarG.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_b)
    public void decreaseB() {
        seekbarB.setProgress(seekbarB.getProgress() - 5);
    }

    @OnClick(R.id.iv_increase_b)
    public void increaseB() {
        seekbarB.setProgress(seekbarB.getProgress() + 5);
    }

    public static Intent newIntent() {
        return newIntent(ColorPickerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);

        showTitle(R.string.colorpicker);

        initSeekbars();

        colorview.setColorViewCallback(this);
    }

    private void initSeekbars() {
        LableSliderSeekBarChangeListener hueSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtH, R.string.lbl_h, 0, ColorView.HUE);
        seekbarH.setOnSeekBarChangeListener(hueSliderChangeListener);
        hueSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener saturationSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtS, R.string.lbl_s, 0, ColorView.SATURATION);
        seekbarS.setOnSeekBarChangeListener(saturationSliderChangeListener);
        saturationSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener valueSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtV, R.string.lbl_v, 0, ColorView.VALUE);
        seekbarV.setOnSeekBarChangeListener(valueSliderChangeListener);
        valueSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener redSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtR, R.string.lbl_r, 0, ColorView.RED);
        seekbarR.setOnSeekBarChangeListener(redSliderChangeListener);
        redSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener greenSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtG, R.string.lbl_r, 0, ColorView.GREEN);
        seekbarG.setOnSeekBarChangeListener(greenSliderChangeListener);
        greenSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener blueSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtB, R.string.lbl_r, 0, ColorView.BLUE);
        seekbarB.setOnSeekBarChangeListener(blueSliderChangeListener);
        blueSliderChangeListener.setSeekbarCallback(this);

        seekbarH.setMax(360);
        seekbarS.setMax(100);
        seekbarV.setMax(100);

        seekbarR.setMax(255);
        seekbarG.setMax(255);
        seekbarB.setMax(255);
    }

    @Override
    public void onProgressChanged(long id, int value, boolean fromUser) {
        if (fromUser) {
            if (id == ColorView.RED) {
                colorview.setRed(value);
            }

            if (id == ColorView.GREEN) {
                colorview.setGreen(value);
            }

            if (id == ColorView.BLUE) {
                colorview.setBlue(value);
            }

            if (id == ColorView.HUE) {
                colorview.setHSV(seekbarH.getProgress(), seekbarS.getProgress(), seekbarV.getProgress());
            }

            if (id == ColorView.SATURATION) {
                colorview.setHSV(seekbarH.getProgress(), seekbarS.getProgress(), seekbarV.getProgress());
            }

            if (id == ColorView.VALUE) {
                colorview.setHSV(seekbarH.getProgress(), seekbarS.getProgress(), seekbarV.getProgress());
            }
        }
    }

    @Override
    public void onStopTrackingTouch(long id, int value) {
        if (id == ColorView.RED) {
            colorview.setRed(value);
        }

        if (id == ColorView.GREEN) {
            colorview.setGreen(value);
        }

        if (id == ColorView.BLUE) {
            colorview.setBlue(value);
        }

        if (id == ColorView.HUE) {
            colorview.setHSV(seekbarH.getProgress(), seekbarS.getProgress(), seekbarV.getProgress());
        }

        if (id == ColorView.SATURATION) {
            colorview.setHSV(seekbarH.getProgress(), seekbarS.getProgress(), seekbarV.getProgress());
        }

        if (id == ColorView.VALUE) {
            colorview.setHSV(seekbarH.getProgress(), seekbarS.getProgress(), seekbarV.getProgress());
        }
    }

    @Override
    public void rgbChanged(int r, int g, int b) {
        seekbarR.setProgress(r);
        seekbarG.setProgress(g);
        seekbarB.setProgress(b);
    }

    @Override
    public void hsvChanged(float hue, float saturation, float value) {
        seekbarH.setProgress(((int) hue));
        seekbarS.setProgress(((int) saturation));
        seekbarV.setProgress(((int) value));
    }
}
