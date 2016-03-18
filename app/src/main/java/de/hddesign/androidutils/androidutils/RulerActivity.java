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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.base.DrawerActivity;
import de.hddesign.androidutils.androidutils.custom.RulerView;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener.SeekbarCallback;

public class RulerActivity extends DrawerActivity implements SeekbarCallback {

    private static int PICK_COLOR = 1999;

    private static int PRIMARY_COLOR = 100;
    private static int ACCENT_COLOR = 101;
    private static int BACKGROUND_COLOR = 103;

    private static int TEXT_SIZE = 201;

    @Bind(R.id.ruler_view)
    RulerView rulerView;

    @Bind(R.id.v_primary_color)
    View vPrimaryColor;

    @Bind(R.id.v_accent_color)
    View vAccentColor;

    @Bind(R.id.v_backgroundcolor)
    View vBackgroundcolor;

    @Bind(R.id.chk_imperial)
    CheckBox chkImperial;

    @Bind(R.id.controls)
    ScrollView controls;

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

    @OnClick(R.id.v_primary_color)
    public void onPrimaryClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(PRIMARY_COLOR, rulerView.getIndicatorColor()), PICK_COLOR);
    }

    @OnClick(R.id.v_accent_color)
    public void onAccentClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(ACCENT_COLOR, rulerView.getTextColor()), PICK_COLOR);
    }

    @OnClick(R.id.v_backgroundcolor)
    public void onBackgroundClicked() {
        startActivityForResult(ColorPickerActivity.newIntent(BACKGROUND_COLOR, rulerView.getBackgroundColor()), PICK_COLOR);
    }

    public static Intent newIntent() {
        return newIntent(RulerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);

        if (savedInstanceState != null) {
            rulerView.setIndicatorColor(savedInstanceState.getInt("PRIMARY_COLOR"));
            rulerView.setTextColor(savedInstanceState.getInt("ACCENT_COLOR"));
            rulerView.setBackgroundColor(savedInstanceState.getInt("BACKGROUND_COLOR"));
            rulerView.setTextSize(savedInstanceState.getFloat("TEXT_SIZE"));
        }

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.slider_controls_menu, menu);
        MenuItem showSlider = menu.findItem(R.id.show_slider);
        showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_IN));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_slider:
                toggleControls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleControls() {
        if (controls.getVisibility() == View.GONE)
            controls.setVisibility(View.VISIBLE);
        else
            controls.setVisibility(View.GONE);
    }

    private void initViews() {
        vPrimaryColor.setBackgroundColor(rulerView.getIndicatorColor());
        vAccentColor.setBackgroundColor(rulerView.getTextColor());
        vBackgroundcolor.setBackgroundColor(rulerView.getBackgroundColor());

        chkImperial.setChecked(rulerView.isImperialUnits());

        chkImperial.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rulerView.setImperialUnits(isChecked);
            }
        });

        LableSliderSeekBarChangeListener textSizeSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtTextSize, R.string.lbl_text_size, 0, TEXT_SIZE, 10);
        seekbarTextSize.setOnSeekBarChangeListener(textSizeSliderChangeListener);
        textSizeSliderChangeListener.setSeekbarCallback(this);

        seekbarTextSize.setMax(50);
        seekbarTextSize.setProgress((int) (rulerView.getTextSize() - 10));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_COLOR && resultCode == ColorPickerActivity.RESULT_OK) {
            int field = data.getIntExtra(ColorPickerActivity.INDEX, 0);
            int color = data.getIntExtra(ColorPickerActivity.PICKED_COLOR, 0);

            if (field == PRIMARY_COLOR) {
                vPrimaryColor.setBackgroundColor(color);
                rulerView.setIndicatorColor(color);
            }

            if (field == ACCENT_COLOR) {
                vAccentColor.setBackgroundColor(color);
                rulerView.setTextColor(color);
            }

            if (field == BACKGROUND_COLOR) {
                vBackgroundcolor.setBackgroundColor(color);
                rulerView.setBackgroundColor(color);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("PRIMARY_COLOR", rulerView.getIndicatorColor());
        outState.putInt("ACCENT_COLOR", rulerView.getTextColor());
        outState.putInt("BACKGROUND_COLOR", rulerView.getBackgroundColor());
        outState.putFloat("TEXT_SIZE", rulerView.getTextSize());
    }

    @Override
    public void onProgressChanged(long id, int value, boolean fromUser) {
        if (id == TEXT_SIZE)
            rulerView.setTextSize(value);
    }

    @Override
    public void onStopTrackingTouch(long id, int value) {
        if (id == TEXT_SIZE)
            rulerView.setTextSize(value);
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_ruler;
    }
}
