package de.hddesign.androidutils.androidutils.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hddesign.androidutils.androidutils.R;
import de.hddesign.androidutils.androidutils.adapter.MaterialAdapter;
import de.hddesign.androidutils.androidutils.adapter.MaterialAdapter.MaterialClickListener;
import de.hddesign.androidutils.androidutils.base.DrawerCompatActivity;
import de.hddesign.androidutils.androidutils.network.dto.PhotoV1Dto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MaterialActivity extends DrawerCompatActivity implements MaterialClickListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private MaterialAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public static Intent newIntent() {
        return newIntent(MaterialActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MaterialAdapter(Glide.with(this));
        recyclerView.setAdapter(adapter);

        showProgress(R.string.loading);
        getRestService().getPhotos().enqueue(new Callback<List<PhotoV1Dto>>() {
            @Override
            public void onResponse(Call<List<PhotoV1Dto>> call, Response<List<PhotoV1Dto>> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    ArrayList<PhotoV1Dto> shortList = new ArrayList<>(response.body().subList(0, 10));
                    adapter.setItems(shortList);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoV1Dto>> call, Throwable t) {
                hideProgress();
            }
        });

        adapter.setMaterialClickListener(this);
    }


    @Override
    protected int getNavItemId() {
        return R.id.nav_material;
    }

    @Override
    public void onItemClicked(PhotoV1Dto photoV1Dto, ImageView imgMain, TextView title) {
        Intent intent = MaterialDetailActivity.newIntent(photoV1Dto);

        Pair<View, String> p1 = Pair.create((View) imgMain, "img_main");
        Pair<View, String> p2 = Pair.create((View) title, "txt_title");

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }
}
