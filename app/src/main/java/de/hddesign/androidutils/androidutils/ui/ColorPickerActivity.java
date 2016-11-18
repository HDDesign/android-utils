package de.hddesign.androidutils.androidutils.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.DrawerCompatActivity;
import de.hddesign.androidutils.androidutils.custom.ColorView;
import de.hddesign.androidutils.androidutils.custom.ColorView.ColorViewCallback;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener.SeekbarCallback;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutDialog;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class ColorPickerActivity extends DrawerCompatActivity implements SeekbarCallback, ColorViewCallback {

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

    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    @Bind(R.id.hex_view)
    TextView hexView;

    @Bind(R.id.sliders)
    ScrollView sliders;

    private MenuItem showSlider;

    private float[] hsv;

    @OnClick(R.id.hex_view)
    public void onHexViewClicked() {
        final TextInputLayoutDialog tilDialog = new TextInputLayoutDialog(this, R.style.AppCompatAlertDialogStyle);

        tilDialog.setTitle(R.string.hex_color);
        tilDialog.setTilHint(R.string.hint_hex_color);

        tilDialog.setTilErrorHelper(TilErrorType.EXACT_LENGTH, R.string.error_hex);
        tilDialog.getTilErrorHelper().setExactLength(6);
        tilDialog.setDigits("abcdefABCDEF0123456789", true);

        String currentColorString = colorview.getCurrentColorAsHEX();

        currentColorString = currentColorString.substring(1, currentColorString.length());

        tilDialog.setInputText(currentColorString);

        tilDialog.setOnPositiveButtonClickedListener(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tilDialog.getTilErrorHelper().hasError()) {
                    String hex = tilDialog.getInputText();
                    colorview.setColorFromHex(hex);
                    tilDialog.dismiss();

                    changeColors();
                }
            }
        });
        tilDialog.show();
    }

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
        data.putExtra(PICKED_COLOR, colorview.getCurrentColorAsInt());
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

        hsv = new float[3];

        Color.colorToHSV(color, hsv);
        colorview.setColor(hsv);

        changeColors();

        colorview.setColorViewCallback(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (index == -1) {
            btnOk.setVisibility(View.GONE);
            Color.RGBToHSV(0, 0, 0, hsv);
            colorview.setColor(hsv);
        } else {
            hideDrawer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.slider_controls_menu, menu);
        showSlider = menu.findItem(R.id.show_slider);

        changeColors();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_slider:
                showSlider();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        btnOk.getBackground().mutate().setTint(colorview.getCurrentColorAsInt());
        toolbar.setBackgroundColor(colorview.getCurrentColorAsInt());
        getWindow().setNavigationBarColor(colorview.getCurrentColorAsInt());
        mainContent.setStatusBarBackgroundColor(colorview.getCurrentColorAsInt());

        if (colorview.getHsv()[2] < 0.3f) {
            btnOk.setTextColor(Color.WHITE);
            hexView.setTextColor(Color.WHITE);
            toolbar.setTitleTextColor(Color.WHITE);
            if (showSlider != null)
                showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_IN));
            if (toolbar.getNavigationIcon() != null)
                toolbar.getNavigationIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_IN));
        } else if (colorview.getHsv()[1] < 0.3f) {
            btnOk.setTextColor(Color.BLACK);
            hexView.setTextColor(Color.BLACK);
            toolbar.setTitleTextColor(Color.BLACK);
            if (showSlider != null)
                showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.BLACK, Mode.SRC_IN));
            if (toolbar.getNavigationIcon() != null)
                toolbar.getNavigationIcon().setColorFilter(new PorterDuffColorFilter(Color.BLACK, Mode.SRC_IN));
        } else if (colorview.getHsv()[0] > 45 && colorview.getHsv()[0] < 200) {
            btnOk.setTextColor(Color.BLACK);
            hexView.setTextColor(Color.BLACK);
            toolbar.setTitleTextColor(Color.BLACK);
            if (showSlider != null)
                showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.BLACK, Mode.SRC_IN));
            if (toolbar.getNavigationIcon() != null)
                toolbar.getNavigationIcon().setColorFilter(new PorterDuffColorFilter(Color.BLACK, Mode.SRC_IN));
        } else {
            btnOk.setTextColor(Color.WHITE);
            hexView.setTextColor(Color.WHITE);
            toolbar.setTitleTextColor(Color.WHITE);
            if (showSlider != null)
                showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_IN));
            if (toolbar.getNavigationIcon() != null)
                toolbar.getNavigationIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_IN));
        }


        hexView.setText(colorview.getCurrentColorAsHEX());
        hexView.setBackgroundColor(colorview.getCurrentColorAsInt());
    }

    private void showSlider() {
        if (sliders.getVisibility() == View.GONE)
            sliders.setVisibility(View.VISIBLE);
        else
            sliders.setVisibility(View.GONE);
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

    @Override
    protected int getNavItemId() {
        return R.id.nav_colorpicker;
    }
}
