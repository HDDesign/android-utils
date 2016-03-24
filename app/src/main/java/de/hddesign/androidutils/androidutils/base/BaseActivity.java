package de.hddesign.androidutils.androidutils.base;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hddesign.androidutils.androidutils.BuildConfig;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.utils.ProgressDialog;

public class BaseActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    protected ProgressDialog progressDialog;

    protected static Intent newIntent(Class<? extends AppCompatActivity> activityClass) {
        Intent intent = new Intent();
        intent.setClassName(BuildConfig.APPLICATION_ID, activityClass.getCanonicalName());
        return intent;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    public interface FragmentTransactionAdapter {
        void adapt(FragmentTransaction transaction);
    }

    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    protected int showFragment(BaseFragment fragment) {
        return showFragment(fragment, true);
    }

    protected int showFragment(BaseFragment fragment, boolean addToBackStack) {
        return showFragment(fragment, addToBackStack, null);
    }

    protected int showFragment(BaseFragment fragment, boolean addToBackStack, FragmentTransactionAdapter adapter) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (adapter != null)
            adapter.adapt(transaction);

        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);

        int id = transaction.commit();
        updateTitle(fragment);

        return id;
    }

    protected final void updateTitle() {
        BaseFragment current = getCurrentFragment();
        if (current != null) {
            updateTitle(current);
        }
    }

    protected void updateTitle(BaseFragment current) {
        String title = current.getDynamicTitle();
        if (title == null) {
            int titleRes = current.getTitleRes();
            if (titleRes > 0)
                showTitle(titleRes);
        } else
            showTitle(title);
    }

    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showKeyboard() {
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    protected void showTitle(String s) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(s);
            getSupportActionBar().invalidateOptionsMenu();
        }
    }

    protected void showTitle(@StringRes int titleRes) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(titleRes));
            getSupportActionBar().invalidateOptionsMenu();
        }
    }

    public void showSubtitle(CharSequence subtitle) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(subtitle);
    }

    public void showSubtitle(int subtitleId) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(subtitleId);
    }

    protected void showProgress(@StringRes int message) {
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);

        if (!isFinishing()) {
            progressDialog.show();
        }
    }

    protected void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            if (!isFinishing()) {
                progressDialog.dismiss();
            }
        }
    }
}
