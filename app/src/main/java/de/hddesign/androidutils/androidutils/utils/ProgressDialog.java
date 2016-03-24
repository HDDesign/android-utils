package de.hddesign.androidutils.androidutils.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import de.hddesign.androidutils.androidutils.R;


public class ProgressDialog extends AlertDialog {

    private ProgressDialog alertDialog;
    private Context context;
    private TextView message;

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);

        View v = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        message = (TextView) v.findViewById(R.id.txt_message);

        this.setView(v);

        this.context = context;
        alertDialog = this;
    }

    public void setMessage(@StringRes int messageRes) {
        message.setText(context.getString(messageRes));
    }
}
