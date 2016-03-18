package de.hddesign.androidutils.androidutils.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import de.hddesign.androidutils.androidutils.R;


public class TextInputLayoutErrorHelper {

    private Context context;
    private EditText et;
    private TextInputLayout til;
    private TilErrorType tilErrorType;

    private Animation shake;

    private int minLength;
    private int maxLength;

    @StringRes
    private int errorResource;

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public TextInputLayoutErrorHelper(Context context, EditText et, TextInputLayout til, TilErrorType tilErrorType, @StringRes int errorResource) {
        this.context = context;
        this.et = et;
        this.til = til;
        this.tilErrorType = tilErrorType;
        this.errorResource = errorResource;

        til.setErrorEnabled(true);
        til.setError(null);

        addTextChangedListener(et);

        shake = AnimationUtils.loadAnimation(context, R.anim.shake);
    }

    private void addTextChangedListener(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (tilErrorType) {
                    case MIN_LENGTH:
                    case MAX_LENGTH:
                    case MIN_MAX_LENGTH:
                        if (!s.toString().isEmpty())
                            showError(checkLength(s.length()));
                        break;
                }
            }
        });
    }

    private boolean showError(boolean showError) {
        if (showError) {
            if (til.getError() == null)
                til.setError(getFormattedErrorString());
            return true;
        } else {
            til.setError(null);
            return false;
        }
    }

    private boolean checkLength(int length) {
        switch (tilErrorType) {
            case MIN_LENGTH:
                return length < minLength;
            case MAX_LENGTH:
                return length > maxLength;
            case MIN_MAX_LENGTH:
                return length < minLength || length > maxLength;
            default:
                return false;
        }
    }

    private String getFormattedErrorString() {
        switch (tilErrorType) {
            case MIN_LENGTH:
                return String.format(getErrorString(), minLength);
            case MAX_LENGTH:
                return String.format(getErrorString(), maxLength);
            case MIN_MAX_LENGTH:
                return String.format(getErrorString(), minLength, maxLength);
            default:
                return getErrorString();
        }
    }

    private String getErrorString() {
        return context.getString(errorResource);
    }

    public enum TilErrorType {
        MIN_LENGTH, MAX_LENGTH, MIN_MAX_LENGTH
    }

    public boolean hasError() {
        switch (tilErrorType) {
            case MIN_LENGTH:
            case MAX_LENGTH:
            case MIN_MAX_LENGTH:
                if (!et.getText().toString().isEmpty()) {
                    if (showError(checkLength(et.getText().length()))) {
                        animateError();
                        return true;
                    } else
                        return false;
                } else {
                    showError(true);
                    animateError();
                    return true;
                }
        }

        return false;
    }

    private void animateError() {
        til.setAnimation(shake);
        til.startAnimation(shake);
    }
}
