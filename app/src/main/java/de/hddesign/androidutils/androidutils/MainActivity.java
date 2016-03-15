package de.hddesign.androidutils.androidutils;

import android.content.Intent;
import android.os.Bundle;

import de.hddesign.androidutils.androidutils.base.BaseActivity;

public class MainActivity extends BaseActivity {

    public static Intent newIntent() {
        return newIntent(MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showTitle(R.string.app_name);
        showFragment(MainFragment.newInstance());
    }
}
