package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.base.DrawerActivity;
import de.hddesign.androidutils.androidutils.custom.DPad;
import de.hddesign.androidutils.androidutils.custom.DPad.DPadCallback;
import de.hddesign.androidutils.androidutils.custom.DPad.DPadTouchListener;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener.SeekbarCallback;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutDialog;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class DPadActivity extends DrawerActivity implements DPadTouchListener, SeekbarCallback, DPadCallback {

    private static int PICK_COLOR = 1999;

    private static int PRIMARY_COLOR = 100;
    private static int ACCENT_COLOR = 101;
    private static int CHEVRON_COLOR = 102;
    private static int BACKGROUND_COLOR = 103;
    private static int WINDOW_COLOR = 104;

    private static int SIZE = 200;
    private static int PADDING = 201;
    private static int CHEVRON = 202;
    private static int CHEVRON_STROKE = 203;
    private static int CHEVRON_PADDING = 204;
    private static int TEXT_SIZE = 205;

    @Bind(R.id.sliders)
    ScrollView sliders;

    @Bind(R.id.dpad)
    DPad dpad;

    @Bind(R.id.txt_center_button_size)
    TextView txtSize;

    @Bind(R.id.seekbar_size)
    SeekBar seekbarSize;

    @OnClick(R.id.iv_increase_size)
    public void increaseSize() {
        seekbarSize.setProgress(seekbarSize.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_size)
    public void decreaseSize() {
        seekbarSize.setProgress(seekbarSize.getProgress() - 5);
    }

    @Bind(R.id.txt_padding_size)
    TextView txtPadding;

    @Bind(R.id.seekbar_padding)
    SeekBar seekbarPadding;

    @OnClick(R.id.iv_increase_padding)
    public void increasePadding() {
        seekbarPadding.setProgress(seekbarPadding.getProgress() + 1);
    }

    @OnClick(R.id.iv_decrease_padding)
    public void decreasePadding() {
        seekbarPadding.setProgress(seekbarPadding.getProgress() - 1);
    }

    @Bind(R.id.txt_chevron_size)
    TextView txtChevronSize;

    @Bind(R.id.seekbar_chevron)
    SeekBar seekbarChevron;

    @OnClick(R.id.iv_increase_chevron)
    public void increaseChevron() {
        seekbarChevron.setProgress(seekbarPadding.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_chevron)
    public void decreaseChevron() {
        seekbarChevron.setProgress(seekbarPadding.getProgress() - 5);
    }

    @Bind(R.id.txt_chevron_stroke)
    TextView txtChevronStroke;

    @Bind(R.id.seekbar_chevron_stroke)
    SeekBar seekbarChevronStroke;

    @OnClick(R.id.iv_increase_chevron_stroke)
    public void increaseChevronStroke() {
        seekbarChevronStroke.setProgress(seekbarChevronStroke.getProgress() + 1);
    }

    @OnClick(R.id.iv_decrease_chevron_stroke)
    public void decreaseChevronStroke() {
        seekbarChevronStroke.setProgress(seekbarChevronStroke.getProgress() - 1);
    }

    @Bind(R.id.txt_chevron_padding)
    TextView txtChevronPadding;

    @Bind(R.id.seekbar_chevron_padding)
    SeekBar seekbarChevronPadding;

    @OnClick(R.id.iv_increase_chevron_padding)
    public void increaseChevronPadding() {
        seekbarChevronPadding.setProgress(seekbarChevronPadding.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_chevron_padding)
    public void decreaseChevronPadding() {
        seekbarChevronPadding.setProgress(seekbarChevronPadding.getProgress() - 5);
    }

    @Bind(R.id.txt_text_size)
    TextView txtTextSize;

    @Bind(R.id.seekbar_text_size)
    SeekBar seekbarTextSize;

    @OnClick(R.id.iv_increase_text_size)
    public void increaseTextSize() {
        seekbarTextSize.setProgress(seekbarTextSize.getProgress() + 5);
    }

    @OnClick(R.id.iv_decrease_text_size)
    public void decreaseTextSize() {
        seekbarTextSize.setProgress(seekbarTextSize.getProgress() - 5);
    }

    @Bind(R.id.v_primary_color)
    View vPrimaryColor;

    @Bind(R.id.v_accent_color)
    View vAccentColor;

    @Bind(R.id.v_chevron_color)
    View vChevronColor;

    @Bind(R.id.v_backgroundcolor)
    View vBackgroundcolor;

    @Bind(R.id.v_windowcolor)
    View vWindowcolor;

    @OnClick(R.id.v_primary_color)
    public void onPrimaryClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(PRIMARY_COLOR, dpad.getPrimaryColor()), PICK_COLOR);
    }

    @OnClick(R.id.v_accent_color)
    public void onAccentClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(ACCENT_COLOR, dpad.getAccentColor()), PICK_COLOR);
    }

    @OnClick(R.id.v_chevron_color)
    public void onChevronClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(CHEVRON_COLOR, dpad.getChevronColor()), PICK_COLOR);
    }

    @OnClick(R.id.v_backgroundcolor)
    public void onBackgroundClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(BACKGROUND_COLOR, dpad.getBackgroundColor()), PICK_COLOR);
    }

    @OnClick(R.id.v_windowcolor)
    public void onWindowClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(WINDOW_COLOR, dpad.getWindowColor()), PICK_COLOR);
    }

    @OnClick(R.id.btn_edit_center_text)
    public void editCenterText() {
        final TextInputLayoutDialog tilDialog = new TextInputLayoutDialog(this, R.style.AppCompatAlertDialogStyle);

        tilDialog.setTilHint(getString(R.string.item_name));
        tilDialog.setTitle(R.string.title_mainitem_change);
        tilDialog.setTilErrorHelper(TilErrorType.MIN_MAX_LENGTH, R.string.error_min_max_length);
        tilDialog.getTilErrorHelper().setMinLength(3);
        tilDialog.getTilErrorHelper().setMaxLength(40);
        tilDialog.setMultiline();
        tilDialog.setInputText(dpad.getCenterText());

        tilDialog.setOnPositiveButtonClickedListener(R.string.change, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tilDialog.getTilErrorHelper().hasError()) {
                    String name = tilDialog.getInputText();
                    dpad.setCenterText(name);
                    tilDialog.dismiss();
                }
            }
        });
        tilDialog.show();
    }

    public static Intent newIntent() {
        return newIntent(DPadActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpad);

        initSeekbars();

        dpad.setdPadTouchListener(this);
        dpad.setdPadCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.color_picker_menu, menu);
        MenuItem showSlider = menu.findItem(R.id.show_slider);
        showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_ATOP));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_slider:
                toggleSlider();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleSlider() {
        if (sliders.getVisibility() == View.GONE)
            sliders.setVisibility(View.VISIBLE);
        else
            sliders.setVisibility(View.GONE);
    }

    private void initSeekbars() {
        LableSliderSeekBarChangeListener sizeSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtSize, R.string.lbl_center_button_size, 0, SIZE, 100);
        seekbarSize.setOnSeekBarChangeListener(sizeSliderChangeListener);
        sizeSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener paddingSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtPadding, R.string.lbl_padding_size, 0, PADDING);
        seekbarPadding.setOnSeekBarChangeListener(paddingSliderChangeListener);
        paddingSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener chevronSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtChevronSize, R.string.lbl_chevron_size, 0, CHEVRON, 5);
        seekbarChevron.setOnSeekBarChangeListener(chevronSliderChangeListener);
        chevronSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener chevronStrokeSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtChevronStroke, R.string.lbl_chevron_stroke, 0, CHEVRON_STROKE, 1);
        seekbarChevronStroke.setOnSeekBarChangeListener(chevronStrokeSliderChangeListener);
        chevronStrokeSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener chevronPaddingSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtChevronPadding, R.string.lbl_chevron_padding, 0, CHEVRON_PADDING);
        seekbarChevronPadding.setOnSeekBarChangeListener(chevronPaddingSliderChangeListener);
        chevronPaddingSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener textSizeSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtTextSize, R.string.lbl_text_size, 0, TEXT_SIZE, 5);
        seekbarTextSize.setOnSeekBarChangeListener(textSizeSliderChangeListener);
        textSizeSliderChangeListener.setSeekbarCallback(this);
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_dpad;
    }

    @Override
    public void onCenterButtonDown() {

    }

    @Override
    public void onTopButtonDown() {

    }

    @Override
    public void onBottomButtonDown() {

    }

    @Override
    public void onRightButtonDown() {

    }

    @Override
    public void onLeftButtonDown() {

    }

    @Override
    public void onDPadButtonUp() {

    }

    @Override
    public void onProgressChanged(long id, int value, boolean fromUser) {
        if (id == SIZE)
            dpad.setCenterButtonRadius(value);

        if (id == PADDING)
            dpad.setButtonPadding(value);

        if (id == CHEVRON)
            dpad.setChevronSize(value);

        if (id == CHEVRON_STROKE)
            dpad.setChevronStrokeWidth(value);

        if (id == CHEVRON_PADDING)
            dpad.setChevronPadding(value);

        if (id == TEXT_SIZE)
            dpad.setCenterTextSize(value);
    }

    @Override
    public void onStopTrackingTouch(long id, int value) {
        if (id == SIZE)
            dpad.setCenterButtonRadius(value);

        if (id == PADDING)
            dpad.setButtonPadding(value);

        if (id == CHEVRON)
            dpad.setChevronSize(value);

        if (id == CHEVRON_STROKE)
            dpad.setChevronStrokeWidth(value);

        if (id == CHEVRON_PADDING)
            dpad.setChevronPadding(value);

        if (id == TEXT_SIZE)
            dpad.setCenterTextSize(value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_COLOR && resultCode == ColorPickerActivity.RESULT_OK) {
            int field = data.getIntExtra(ColorPickerActivity.INDEX, 0);
            int color = data.getIntExtra(ColorPickerActivity.PICKED_COLOR, 0);

            if (field == PRIMARY_COLOR) {
                vPrimaryColor.setBackgroundColor(color);
                dpad.setPrimaryColor(color);
            }

            if (field == ACCENT_COLOR) {
                vAccentColor.setBackgroundColor(color);
                dpad.setAccentColor(color);
            }

            if (field == CHEVRON_COLOR) {
                vChevronColor.setBackgroundColor(color);
                dpad.setChevronColor(color);
            }

            if (field == BACKGROUND_COLOR) {
                vBackgroundcolor.setBackgroundColor(color);
                dpad.setBackgroundColor(color);
            }

            if (field == WINDOW_COLOR) {
                vWindowcolor.setBackgroundColor(color);
                dpad.setWindowColor(color);
            }
        }
    }

    @Override
    public void layoutReady() {
        int centerButtonRadius = (int) dpad.getCenterButtonRadius();
        int padding = (int) dpad.getButtonPadding();
        int chevron = (int) dpad.getChevronSize();
        int chevronStrokeWidth = (int) dpad.getChevronStrokeWidth();
        int chevronPadding = (int) dpad.getChevronPadding();
        int textSize = (int) dpad.getCenterTextSize();

        seekbarSize.setMax((((int) dpad.getSize()) - 250) / 2);
        seekbarSize.setProgress((centerButtonRadius - 100));

        seekbarPadding.setMax(50);
        seekbarPadding.setProgress(padding);

        seekbarChevron.setMax(150);
        seekbarChevron.setProgress(chevron - 5);

        seekbarChevronStroke.setMax(25);
        seekbarChevronStroke.setProgress(chevronStrokeWidth - 1);

        seekbarChevronPadding.setMax(200);
        seekbarChevronPadding.setProgress(chevronPadding);

        seekbarTextSize.setMax(300);
        seekbarTextSize.setProgress(textSize - 5);

        vPrimaryColor.setBackgroundColor(dpad.getPrimaryColor());
        vAccentColor.setBackgroundColor(dpad.getAccentColor());
        vChevronColor.setBackgroundColor(dpad.getChevronColor());
        vBackgroundcolor.setBackgroundColor(dpad.getBackgroundColor());
        vWindowcolor.setBackgroundColor(dpad.getWindowColor());

        toggleSlider();
    }
}
