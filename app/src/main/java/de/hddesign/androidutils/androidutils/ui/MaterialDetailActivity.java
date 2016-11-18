package de.hddesign.androidutils.androidutils.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.base.DrawerCompatActivity;
import de.hddesign.androidutils.androidutils.network.dto.PhotoV1Dto;


public class MaterialDetailActivity extends DrawerCompatActivity {

    public static final String EXTRA_PHOTO = "EXTRA_PHOTO";

    @Bind(R.id.header_img)
    ImageView headerImg;

    @Bind(R.id.txt_main)
    TextView txtMain;

    @Bind(R.id.txt_title)
    TextView txtTitle;

    public static Intent newIntent(PhotoV1Dto photo) {
        Intent intent = newIntent(MaterialDetailActivity.class);
        intent.putExtra(EXTRA_PHOTO, photo);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_detail);

        if (getIntent().getExtras() != null && getIntent().getExtras().get(EXTRA_PHOTO) != null) {
            PhotoV1Dto photo = (PhotoV1Dto) getIntent().getExtras().get(EXTRA_PHOTO);
            Glide.with(this).load((photo.getUrl())).into(headerImg);
            showTitle(photo.getTitle());
            txtTitle.setText(photo.getTitle());
        }

        txtMain.setText(Html.fromHtml(getString(R.string.lorem_ipsum)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_material;
    }
}
