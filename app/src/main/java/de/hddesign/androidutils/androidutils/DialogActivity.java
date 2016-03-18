package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.base.DrawerActivity;
import de.hddesign.androidutils.androidutils.utils.MaterialProgressDialog;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutDialog;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class DialogActivity extends DrawerActivity {

    private String title;
    private String hint;
    private String preInput = "";

    private int min = 3;
    private int max = 10;

    private boolean multiline = false;

    private TilErrorType errorType = TilErrorType.MIN_MAX_LENGTH;
    private int errorRes = R.string.error_min_max_length;

    @Bind(R.id.controls)
    ScrollView controls;

    @Bind(R.id.et_title)
    TextInputEditText etTitle;

    @Bind(R.id.et_hint)
    TextInputEditText etHint;

    @Bind(R.id.et_pre)
    TextInputEditText etPreInput;

    @Bind(R.id.et_min)
    TextInputEditText etMinInput;

    @Bind(R.id.et_max)
    TextInputEditText etMaxInput;

    @Bind(R.id.chk_multiline)
    CheckBox chkMultiline;

    @Bind(R.id.chk_error_min)
    CheckBox chkErrorMin;

    @Bind(R.id.chk_error_max)
    CheckBox chkErrorMax;

    @OnClick(R.id.btn_open_dialog)
    public void openTextDialog() {
        updateValues();

        final TextInputLayoutDialog tilDialog = new TextInputLayoutDialog(this, R.style.AppCompatAlertDialogStyle);

        tilDialog.setTitle(title);
        tilDialog.setTilHint(hint);

        if (errorType != null) {
            tilDialog.setTilErrorHelper(errorType, errorRes);
            tilDialog.getTilErrorHelper().setMinLength(min);
            tilDialog.getTilErrorHelper().setMaxLength(max);
        }

        tilDialog.setMultiline(multiline);

        tilDialog.setInputText(preInput);

        tilDialog.setOnPositiveButtonClickedListener(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tilDialog.getTilErrorHelper().hasError()) {
                    String name = tilDialog.getInputText();
                    Toast.makeText(DialogActivity.this, String.format("%s bestätigt", name), Toast.LENGTH_SHORT).show();
                    tilDialog.dismiss();
                }
            }
        });
        tilDialog.show();
    }

    @OnClick(R.id.btn_open_progress_dialog)
    public void openProgressDialog() {
        updateValues();
        final MaterialProgressDialog materialProgressDialog = new MaterialProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        materialProgressDialog.setMessage("Bitte Warten");
        materialProgressDialog.show();
    }

    public static Intent newIntent() {
        return newIntent(DialogActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        initValues();
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

    private void initValues() {
        title = getString(R.string.title);
        hint = getString(R.string.hint);

        etTitle.setText(title);
        etHint.setText(hint);
        etPreInput.setText(preInput);
        etMinInput.setText(String.valueOf(min));
        etMaxInput.setText(String.valueOf(max));

        chkMultiline.setChecked(multiline);

        if (errorType == TilErrorType.MIN_MAX_LENGTH) {
            chkErrorMin.setChecked(true);
            chkErrorMax.setChecked(true);
        } else if (errorType == TilErrorType.MIN_LENGTH)
            chkErrorMin.setChecked(true);
        else if (errorType == TilErrorType.MAX_LENGTH)
            chkErrorMax.setChecked(true);
    }

    private void updateValues() {
        if (!etTitle.getText().toString().isEmpty())
            title = etTitle.getText().toString();

        if (!etHint.getText().toString().isEmpty())
            hint = etHint.getText().toString();

        if (!etPreInput.getText().toString().isEmpty())
            preInput = etPreInput.getText().toString();

        if (!etMinInput.getText().toString().isEmpty())
            min = Integer.valueOf(etMinInput.getText().toString());

        if (!etMaxInput.getText().toString().isEmpty())
            max = Integer.valueOf(etMaxInput.getText().toString());

        multiline = chkMultiline.isChecked();

        if (chkErrorMin.isChecked() && chkErrorMax.isChecked()) {
            errorType = TilErrorType.MIN_MAX_LENGTH;
            errorRes = R.string.error_min_max_length;
        } else if (chkErrorMin.isChecked()) {
            errorType = TilErrorType.MIN_LENGTH;
            errorRes = R.string.error_min_length;
        } else if (chkErrorMax.isChecked()) {
            errorType = TilErrorType.MAX_LENGTH;
            errorRes = R.string.error_max_length;
        } else
            errorType = null;
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_dialog;
    }
}
