package de.hddesign.androidutils.androidutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import de.hddesign.androidutils.androidutils.adapter.PaletteAdapter;
import de.hddesign.androidutils.androidutils.adapter.PaletteAdapter.ItemClickListener;
import de.hddesign.androidutils.androidutils.adapter.PaletteAdapter.ItemViewHolder;
import de.hddesign.androidutils.androidutils.base.DrawerActivity;
import de.hddesign.androidutils.androidutils.model.PaletteItem;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener;
import de.hddesign.androidutils.androidutils.utils.LableSliderSeekBarChangeListener.SeekbarCallback;
import de.hddesign.androidutils.androidutils.utils.ListDialog;
import de.hddesign.androidutils.androidutils.utils.Preferences;
import de.hddesign.androidutils.androidutils.utils.TextInputLayoutErrorHelper.TilErrorType;

public class PaletteActivity extends DrawerActivity implements ItemClickListener, SeekbarCallback {

    private static long SPAN_COUNT = 101;
    private static long ASPECT_WIDTH = 102;
    private static long ASPECT_HEIGHT = 103;
    private static long IMAGE_RES = 104;

    private PaletteAdapter paletteAdapter;
    private ItemTouchHelper itemTouchHelper;
    private GridLayoutManager gridLayoutManager;

    private Preferences preferences;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.sliders)
    LinearLayout sliders;

    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @Bind(R.id.image)
    ImageView image;

    @Bind(R.id.txt_span_count)
    TextView txtSpanCount;

    @Bind(R.id.seekbar_span_count)
    SeekBar seekbarSpanCount;

    @Bind(R.id.spinner_palettetype)
    AppCompatSpinner spinnerPalettetype;

    @OnClick(R.id.iv_increase_span_count)
    public void increaseSpanCount() {
        seekbarSpanCount.setProgress(seekbarSpanCount.getProgress() + 1);
    }

    @OnClick(R.id.iv_decrease_span_count)
    public void decreaseSpanCount() {
        seekbarSpanCount.setProgress(seekbarSpanCount.getProgress() - 1);
    }

    @Bind(R.id.txt_aspect_width)
    TextView txtAspectWidth;

    @Bind(R.id.seekbar_aspect_width)
    SeekBar seekbarAspectWidth;

    @OnClick(R.id.iv_increase_aspect_width)
    public void increaseAspectWidth() {
        seekbarAspectWidth.setProgress(seekbarAspectWidth.getProgress() + 1);
    }

    @OnClick(R.id.iv_decrease_aspect_width)
    public void decreaseAspectWidth() {
        seekbarAspectWidth.setProgress(seekbarAspectWidth.getProgress() - 1);
    }

    @Bind(R.id.txt_aspect_height)
    TextView txtAspectHeight;

    @Bind(R.id.seekbar_aspect_height)
    SeekBar seekbarAspectHeight;

    @OnClick(R.id.iv_increase_aspect_height)
    public void increaseAspectHeight() {
        seekbarAspectHeight.setProgress(seekbarAspectHeight.getProgress() + 1);
    }

    @OnClick(R.id.iv_decrease_aspect_height)
    public void decreaseAspectHeight() {
        seekbarAspectHeight.setProgress(seekbarAspectHeight.getProgress() - 1);
    }

    @Bind(R.id.txt_img_resolution)
    TextView txtImageResolution;

    @Bind(R.id.seekbar_img_resolution)
    SeekBar seekBarImageResolution;

    @OnClick(R.id.iv_increase_img_resolution)
    public void increaseImageResolution() {
        seekBarImageResolution.setProgress(seekBarImageResolution.getProgress() + 1);
    }

    @OnClick(R.id.iv_decrease_img_resolution)
    public void decreaseImageResolution() {
        seekBarImageResolution.setProgress(seekBarImageResolution.getProgress() - 1);
    }

    @OnClick(R.id.btn_keywords)
    public void onEditKeywordsClicked() {
        final ListDialog tilDialog = new ListDialog(this, R.style.AppCompatAlertDialogStyle);

        tilDialog.setTilHint(getString(R.string.item_name));
        tilDialog.setTitle(R.string.title_mainitem_create);
        tilDialog.setTilErrorHelper(TilErrorType.MIN_MAX_LENGTH, R.string.error_min_max_length);
        tilDialog.getTilErrorHelper().setMinLength(3);
        tilDialog.getTilErrorHelper().setMaxLength(15);

        tilDialog.addStrings(paletteAdapter.getKeywords());

        tilDialog.setOnPositiveButtonClickedListener(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> paletteQuery = new HashSet<>();
                paletteQuery.addAll(tilDialog.getStrings());

                paletteAdapter.addKeywords(paletteQuery);

                ClearDiskCacheAsync clearDiskCacheAsync = new ClearDiskCacheAsync(getApplicationContext());
                clearDiskCacheAsync.execute();

                preferences.setPaletteQuery(paletteQuery);
                tilDialog.dismiss();
            }
        });

        tilDialog.show();
    }

    public class ClearDiskCacheAsync extends AsyncTask<Void, Void, Void> {
        private Context context;

        public ClearDiskCacheAsync(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(R.string.clearing_cache);
            Glide.get(context).clearMemory();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgress();
            paletteAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Glide.get(context).clearDiskCache();
            return null;
        }
    }

    public static Intent newIntent() {
        return newIntent(PaletteActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        preferences = new Preferences(this);

        paletteAdapter = new PaletteAdapter(Glide.with(this));
        paletteAdapter.setItemClickListener(this);

        initRecycler();
        initSeekbars();
        initSpinner();

        showTitle(R.string.palette);
    }

    private void initSpinner() {
        spinnerPalettetype.setSelection(3);

        spinnerPalettetype.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paletteAdapter.setPaletteType((String) spinnerPalettetype.getAdapter().getItem(position));
                paletteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.slider_controls_menu, menu);
        MenuItem showSlider = menu.findItem(R.id.show_slider);
        showSlider.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_ATOP));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_slider:
                showSlider();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSlider() {
        if (sliders.getVisibility() == View.GONE) {
            sliders.setVisibility(View.VISIBLE);
            appBarLayout.setExpanded(true);
            image.setVisibility(View.INVISIBLE);
            collapsingToolbar.setTitleEnabled(false);
        } else {
            sliders.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            appBarLayout.setExpanded(false);
            collapsingToolbar.setTitleEnabled(true);
        }
    }

    private void initRecycler() {
        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                paletteAdapter.reorderItems(fromPosition, toPosition);
                paletteAdapter.notifyItemMoved(fromPosition, toPosition);

                int count;
                if (fromPosition < toPosition) {
                    count = toPosition - fromPosition;
                    paletteAdapter.notifyItemRangeChanged(fromPosition, count + 1);
                } else {
                    count = fromPosition - toPosition;
                    paletteAdapter.notifyItemRangeChanged(toPosition, count + 1);
                }

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final PaletteItem item = paletteAdapter.getItem(position);
                paletteAdapter.removeItem(item);
                paletteAdapter.notifyItemRemoved(position);
                paletteAdapter.rewriteIndexes();

                int count = paletteAdapter.getItemCount() - position;
                paletteAdapter.notifyItemRangeChanged(position, count);

                Snackbar snackbar = Snackbar
                        .make(mainContent, R.string.deleted_entry, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_redo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                paletteAdapter.addItemAtPosition(position, item);
                                paletteAdapter.notifyItemInserted(position);
                                paletteAdapter.rewriteIndexes();

                                int count = paletteAdapter.getItemCount() - position;
                                paletteAdapter.notifyItemRangeChanged(position, count);

                                Snackbar snackbar1 = Snackbar.make(mainContent, R.string.restored_entry, Snackbar.LENGTH_SHORT);

                                View snackbarLayout = snackbar1.getView();
                                snackbarLayout.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.snackbar_restore), Mode.SRC_IN));

                                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_undo, 0, 0, 0);
                                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                                textView.setGravity(Gravity.CENTER_VERTICAL);

                                snackbar1.show();
                            }
                        });

                snackbar.setActionTextColor(Color.WHITE);

                View snackbarLayout = snackbar.getView();
                snackbarLayout.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.snackbar_delete), Mode.SRC_IN));

                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);
                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                textView.setGravity(Gravity.CENTER_VERTICAL);

                snackbar.show();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        gridLayoutManager = new GridLayoutManager(this, 4);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(paletteAdapter);

        paletteAdapter.addKeywords(preferences.getPaletteQuery());

        paletteAdapter.addItems(generateItems(20));
        paletteAdapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (gridLayoutManager.findLastVisibleItemPosition() > ((paletteAdapter.getItemCount() - gridLayoutManager.getSpanCount() * 2))) {
                    paletteAdapter.addItemsWithoutClear(generateItems(20));
                }
            }
        });
    }

    private void initSeekbars() {
        LableSliderSeekBarChangeListener spanCountSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtSpanCount, R.string.lbl_span_count, 0, SPAN_COUNT, 1);
        seekbarSpanCount.setOnSeekBarChangeListener(spanCountSliderChangeListener);
        spanCountSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener aspectWidthSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtAspectWidth, R.string.lbl_aspect_width, 0, ASPECT_WIDTH, 1);
        seekbarAspectWidth.setOnSeekBarChangeListener(aspectWidthSliderChangeListener);
        aspectWidthSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener aspectHeightSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtAspectHeight, R.string.lbl_aspect_height, 0, ASPECT_HEIGHT, 1);
        seekbarAspectHeight.setOnSeekBarChangeListener(aspectHeightSliderChangeListener);
        aspectHeightSliderChangeListener.setSeekbarCallback(this);

        LableSliderSeekBarChangeListener imageResSliderChangeListener = new LableSliderSeekBarChangeListener(this, txtImageResolution, R.string.lbl_img_resolution, 0, IMAGE_RES, 50, 50);
        seekBarImageResolution.setOnSeekBarChangeListener(imageResSliderChangeListener);
        imageResSliderChangeListener.setSeekbarCallback(this);

        seekbarSpanCount.setProgress(gridLayoutManager.getSpanCount() - 1);
        seekbarSpanCount.setMax(9);

        seekbarAspectWidth.setProgress(0);
        seekbarAspectWidth.setMax(21);

        seekbarAspectHeight.setProgress(0);
        seekbarAspectHeight.setMax(21);

        seekBarImageResolution.setMax(1450 / 50);
    }

    private ArrayList<PaletteItem> generateItems(int count) {
        ArrayList<PaletteItem> tmpList = new ArrayList<>();

        for (int i = 0; i < count; i++)
            tmpList.add(new PaletteItem(paletteAdapter.getItemCount() + i + 1, "#FFFFFF", getRandomUrl(paletteAdapter.getItemCount() + i + 1)));

        return tmpList;
    }

    @Override
    public void onStartItemDrag(ItemViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }

    @Override
    public void onItemClick(String url) {
        Toast.makeText(this, String.format(getString(R.string.reload_image), url), Toast.LENGTH_SHORT).show();
    }

    public String getRandomUrl(int count) {
        String randomUrl = "http://loremflickr.com/%d/%d/%s?cache=";
        randomUrl = randomUrl.concat(String.valueOf(count));

        return randomUrl;
    }

    @Override
    protected int getNavItemId() {
        return R.id.nav_palette;
    }

    @Override
    public void onProgressChanged(long id, int value, boolean fromUser) {
        if (id == SPAN_COUNT) {
            gridLayoutManager.setSpanCount(value);
            gridLayoutManager.requestLayout();
        }

        if (id == ASPECT_WIDTH) {
            paletteAdapter.setAspectWidth(value);
            paletteAdapter.notifyDataSetChanged();
        }

        if (id == ASPECT_HEIGHT) {
            paletteAdapter.setAspectHeight(value);
            paletteAdapter.notifyDataSetChanged();
        }

        if (id == IMAGE_RES) {
            paletteAdapter.setImageResolution(value);
            paletteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStopTrackingTouch(long id, int value) {
        if (id == SPAN_COUNT) {
            gridLayoutManager.setSpanCount(value);
            gridLayoutManager.requestLayout();
        }

        if (id == ASPECT_WIDTH) {
            paletteAdapter.setAspectWidth(value);
            paletteAdapter.notifyDataSetChanged();
        }

        if (id == ASPECT_HEIGHT) {
            paletteAdapter.setAspectHeight(value);
            paletteAdapter.notifyDataSetChanged();
        }

        if (id == IMAGE_RES) {
            paletteAdapter.setImageResolution(value);
            paletteAdapter.notifyDataSetChanged();
        }
    }
}
