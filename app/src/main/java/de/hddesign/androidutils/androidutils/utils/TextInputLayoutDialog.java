package de.hddesign.androidutils.androidutils.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.WindowManager;

import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class TextInputLayoutDialog extends AlertDialog {

    private TextInputLayoutDialog alertDialog;
    private Context context;
    private TextInputLayout til;
    private TextInputEditText input;
    private TextInputLayoutErrorHelper tilErrorHelper;

    public TextInputLayoutDialog(final Context context, int themeResId) {
        super(context, themeResId);

        View v = getLayoutInflater().inflate(R.layout.dialog_textinputlayout, null);
        til = (TextInputLayout) v.findViewById(R.id.til);
        input = (TextInputEditText) v.findViewById(R.id.et);

        setMultiline(false);

        this.setView(v);

        this.context = context;
        alertDialog = this;

        this.setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }

    public void setMultiline(boolean multiline) {
        input.setSingleLine(!multiline);
    }

    public void setInputType(int inputType) {
        input.setInputType(inputType);
    }

    public void setDigits(final String chars, boolean showNormalKeyboard) {
        input.setKeyListener(DigitsKeyListener.getInstance(chars));

        if (showNormalKeyboard) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            InputFilter filter = new InputFilter() {
                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!charAllowed(chars, source.charAt(i))) {
                            return source.subSequence(0, i);
                        }
                    }
                    return null;
                }
            };
            input.setFilters(new InputFilter[]{filter});
        }
    }

    private boolean charAllowed(String chars, Character c) {
        return chars.contains(c.toString());
    }

    public void setInputText(String string) {
        input.setText(string);
    }

    public String getInputText() {
        if (!input.getText().toString().isEmpty())
            return input.getText().toString();
        else
            return "";
    }

    public void setTilHint(@StringRes int hintRes) {
        til.setHint(context.getString(hintRes));
    }

    public void setTilHint(String string) {
        til.setHint(string);
    }

    public void setTilErrorHelper(TilErrorType tilErrorType, @StringRes int errorRes) {
        tilErrorHelper = new TextInputLayoutErrorHelper(context, input, til, tilErrorType, errorRes);
    }

    public TextInputLayoutErrorHelper getTilErrorHelper() {
        return tilErrorHelper;
    }

    public void setOnPositiveButtonClickedListener(@StringRes int buttonLable, final View.OnClickListener onClickListener) {
        this.setButton(BUTTON_POSITIVE, context.getString(buttonLable), ((OnClickListener) null));
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener(onClickListener);
            }
        });
    }
}
