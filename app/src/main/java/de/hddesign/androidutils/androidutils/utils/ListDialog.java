package de.hddesign.androidutils.androidutils.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.adapter.StringAdapter;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class ListDialog extends AlertDialog {

    private ListDialog alertDialog;
    private Context context;
    private TextInputLayout til;
    private TextInputEditText input;
    private TextInputLayoutErrorHelper tilErrorHelper;
    private RecyclerView recyclerView;
    private ImageView addButton;
    private CoordinatorLayout mainContent;

    private StringAdapter stringAdapter;
    private ItemTouchHelper itemTouchHelper;

    public ListDialog(final Context context, int themeResId) {
        super(context, themeResId);

        View v = getLayoutInflater().inflate(R.layout.dialog_textinputlayout_list, null);
        til = (TextInputLayout) v.findViewById(R.id.til);
        input = (TextInputEditText) v.findViewById(R.id.et);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_items);
        addButton = (ImageView) v.findViewById(R.id.iv_add);
        mainContent = (CoordinatorLayout) v.findViewById(R.id.main_content);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerDividerItemDecoration(context));

        stringAdapter = new StringAdapter();

        recyclerView.setAdapter(stringAdapter);

        initTouchHelper();

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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getTilErrorHelper().hasError()) {
                    stringAdapter.addString(input.getText().toString());
                    stringAdapter.notifyItemInserted(stringAdapter.getItemCount());
                    input.setText("");
                }
            }
        });
    }

    private void initTouchHelper() {
        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final String string = stringAdapter.getString(position);
                stringAdapter.removeString(string);
                stringAdapter.notifyItemRemoved(position);

                int count = stringAdapter.getItemCount() - position;
                stringAdapter.notifyItemRangeChanged(position, count);

                Snackbar snackbar = Snackbar
                        .make(mainContent, R.string.deleted_entry, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_redo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stringAdapter.addStringAtPosition(position, string);
                                stringAdapter.notifyItemInserted(position);

                                int count = stringAdapter.getItemCount() - position;
                                stringAdapter.notifyItemRangeChanged(position, count);

                                Snackbar snackbar1 = Snackbar.make(mainContent, R.string.restored_entry, Snackbar.LENGTH_SHORT);

                                View snackbarLayout = snackbar1.getView();
                                snackbarLayout.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.snackbar_restore), Mode.SRC_ATOP));

                                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_undo, 0, 0, 0);
                                textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setBackgroundColor(context.getResources().getColor(R.color.snackbar_restore));

                                snackbar1.show();
                            }
                        });

                snackbar.setActionTextColor(Color.WHITE);

                View snackbarLayout = snackbar.getView();
                snackbarLayout.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.snackbar_delete), Mode.SRC_ATOP));

                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);
                textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setBackgroundColor(context.getResources().getColor(R.color.snackbar_delete));

                snackbar.show();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

    public ArrayList<String> getStrings() {
        return stringAdapter.getStrings();
    }

    public void addStrings(ArrayList<String> keywords) {
        stringAdapter.addStrings(keywords);
        stringAdapter.notifyDataSetChanged();
    }
}
