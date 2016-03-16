package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Button;
import android.widget.ScrollView;
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

    public static final String PICKED_COLOR = "pickedColor";
    public static final String INDEX = "index";

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

    @Bind(R.id.btn_ok)
    Button btnOk;

    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    @Bind(R.id.fragment_container)
    ScrollView fragmentContainer;

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

    @OnClick(R.id.btn_ok)
    public void onOkayClicked() {
        Intent data = new Intent();
        data.putExtra(PICKED_COLOR, colorview.getCurrentColor());
        data.putExtra(INDEX, index);
        setResult(RESULT_OK, data);
        this.finish();
    }

    private int index;
    private int color;

    public static Intent newIntent(int index, int color) {
        Intent intent = newIntent(ColorPickerActivity.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PICKED_COLOR, color);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);

        index = getIntent().getIntExtra(INDEX, 0);
        color = getIntent().getIntExtra(PICKED_COLOR, 0);

        showTitle(R.string.colorpicker);

        initSeekbars();

        colorview.setColorViewCallback(this);

        fragmentContainer.setOnTouchListener(colorview.getColorViewTouchListener());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        float hsv[] = new float[3];

        Color.colorToHSV(color, hsv);
        colorview.setColor(hsv);
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

        changeColors();
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

        changeColors();
    }

    private void changeColors() {
        btnOk.setBackgroundColor(colorview.getCurrentColor());
        toolbar.setBackgroundColor(colorview.getCurrentColor());
        getWindow().setNavigationBarColor(colorview.getCurrentColor());
        mainContent.setStatusBarBackgroundColor(colorview.getCurrentColor());

        if (colorview.getHsv()[2] < 0.3f) {
            btnOk.setTextColor(Color.WHITE);
            toolbar.setTitleTextColor(Color.WHITE);
        } else if (colorview.getHsv()[1] < 0.3f) {
            btnOk.setTextColor(Color.BLACK);
            toolbar.setTitleTextColor(Color.BLACK);
        } else {
            btnOk.setTextColor(Color.WHITE);
            toolbar.setTitleTextColor(Color.WHITE);
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
