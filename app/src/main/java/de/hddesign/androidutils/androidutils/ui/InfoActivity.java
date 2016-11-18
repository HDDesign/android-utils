package de.hddesign.androidutils.androidutils.ui;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.DrawerCompatActivity;

public class InfoActivity extends DrawerCompatActivity {

    @Bind(R.id.txt_info)
    TextView txtInfo;

    public static Intent newIntent() {
        return newIntent(InfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String type = (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE ? "Tablet" : "Phone");

        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            txtInfo.setText(String.format(getString(R.string.info_text), info.versionName, Build.MANUFACTURER, Build.BRAND, Build.MODEL, VERSION.RELEASE, type, memoryInfo.availMem / 1000000, memoryInfo.totalMem / 1000000));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_info;
    }
}
