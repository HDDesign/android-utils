package de.hddesign.androidutils.androidutils.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.ui.ColorPickerActivity;
import de.hddesign.androidutils.androidutils.ui.DPadActivity;
import de.hddesign.androidutils.androidutils.ui.DialogActivity;
import de.hddesign.androidutils.androidutils.ui.InfoActivity;
import de.hddesign.androidutils.androidutils.ui.MaterialActivity;
import de.hddesign.androidutils.androidutils.ui.PaletteActivity;
import de.hddesign.androidutils.androidutils.ui.RecyclerActivity;
import de.hddesign.androidutils.androidutils.ui.RulerActivity;


public class DrawerCompatActivity extends BaseCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupDrawerLayout();
        setItemChecked(getNavItemId());
    }

    protected void setItemChecked(int id) {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            int currentItemId = navigationView.getMenu().getItem(i).getItemId();
            if (currentItemId == id) {
                navigationView.getMenu().getItem(i).setChecked(true);
            }
        }
    }


    private void setupDrawerLayout() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return chooseMenuItem(menuItem);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

            }
        };
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        actionBarDrawerToggle.syncState();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        onBackStackChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onPause() {
        drawerLayout.removeDrawerListener(actionBarDrawerToggle);
        super.onPause();
    }

    protected void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    private boolean chooseMenuItem(final MenuItem item) {
        navigationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavViewItem(item.getItemId());
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        closeDrawer();
        return true;
    }

    protected void goToNavViewItem(int menuItemId) {
        setItemChecked(getNavItemId());
        if (menuItemId != getNavItemId()) {
            switch (menuItemId) {
                case R.id.nav_recycler_view:
                    startActivity(RecyclerActivity.newIntent());
                    break;
                case R.id.nav_colorpicker:
                    startActivity(ColorPickerActivity.newIntent(-1, -1));
                    break;
                case R.id.nav_dpad:
                    startActivity(DPadActivity.newIntent());
                    break;
                case R.id.nav_dialog:
                    startActivity(DialogActivity.newIntent());
                    break;
                case R.id.nav_ruler:
                    startActivity(RulerActivity.newIntent());
                    break;
                case R.id.nav_palette:
                    startActivity(PaletteActivity.newIntent());
                    break;
                case R.id.nav_material:
                    startActivity(MaterialActivity.newIntent());
                    break;
                case R.id.nav_info:
                    startActivity(InfoActivity.newIntent());
                    break;
            }
        }
    }

    protected int getNavItemId() {
        throw new UnsupportedOperationException("Please override this method.");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 1) {
            showDrawer();
        } else if (backStackCount == 2) {
            hideDrawer();
        }
        updateTitle();
        actionBarDrawerToggle.syncState();
        supportInvalidateOptionsMenu();
    }

    protected void showDrawer() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    protected void hideDrawer() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        super.onDestroy();
    }
}
