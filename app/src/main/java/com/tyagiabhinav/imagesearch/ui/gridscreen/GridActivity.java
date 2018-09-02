package com.tyagiabhinav.imagesearch.ui.gridscreen;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tyagiabhinav.imagesearch.R;
import com.tyagiabhinav.imagesearch.model.ImagesViewModel;
import com.tyagiabhinav.imagesearch.util.DividerLine;
import com.tyagiabhinav.imagesearch.util.PrefHelper;
import com.tyagiabhinav.imagesearch.util.Utils;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridActivity extends AppCompatActivity {

    private static final String TAG = GridActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.photoRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.warning)
    FrameLayout warningLayout;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.no_data)
    TextView noData;

    private ImagesViewModel imagesViewModel;
    private GridLayoutManager imagesLayoutManager;
    private RecyclerViewAdapter adapter;
    private int currentGridColumns = PrefHelper.getInstance().getGridColumns();
    private int currentPageIndex = 1;
    int currentItems, totalItems, scrollOutItems;
    private boolean isScrolling = false;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: toolbar set");
        toolbar.setTitle(getTitle());
//        toolbar.inflateMenu(R.menu.menu_items);

        // to improve performance as changes in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        // use grid layout manager for positioning of items in the list in grid formation
        imagesLayoutManager = new GridLayoutManager(GridActivity.this, currentGridColumns);

        // Get a new or existing ViewModel from the ViewModelProvider.
        imagesViewModel = ViewModelProviders.of(this).get(ImagesViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String category) {
                displayToast("Fetching images for: " + category);
                category = URLEncoder.encode(category);
                GridActivity.this.category = category;
                // clear previous search
                if (adapter != null) {
                    Log.d(TAG, "onQueryTextSubmit: Resetting recyclerview.....");
                    adapter.clearImages();
                    totalItems = 0;
                    currentPageIndex = 0;
                    recyclerView.invalidate();

                }
                recyclerView.setLayoutManager(imagesLayoutManager);
                recyclerView.addItemDecoration(new DividerLine(GridActivity.this));

                adapter = new RecyclerViewAdapter(GridActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnScrollListener(new ScrollListener());


                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();

                imagesViewModel.getPhotos(category, currentPageIndex).observe(GridActivity.this, (imagesList) -> {
                    progress.setVisibility(View.GONE);
                    adapter.setImages(imagesList);
//                    Log.d(TAG, "onQueryTextSubmit: Notify@"+totalItems);
//                    adapter.notifyItemInserted(totalItems);
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_2_cols:
                displayToast(getString(R.string.action_2_columns));
                if (currentGridColumns != 2) {
                    currentGridColumns = 2;
                    PrefHelper.getInstance().setGridColumns(currentGridColumns);
                    imagesLayoutManager.setSpanCount(currentGridColumns);
                }
                return true;
            case R.id.action_3_cols:
                displayToast(getString(R.string.action_3_columns));
                if (currentGridColumns != 3) {
                    currentGridColumns = 3;
                    PrefHelper.getInstance().setGridColumns(currentGridColumns);
                    imagesLayoutManager.setSpanCount(currentGridColumns);
                }
                return true;
            case R.id.action_4_cols:
                displayToast(getString(R.string.action_4_columns));
                if (currentGridColumns != 4) {
                    currentGridColumns = 4;
                    PrefHelper.getInstance().setGridColumns(currentGridColumns);
                    imagesLayoutManager.setSpanCount(currentGridColumns);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            currentItems = imagesLayoutManager.getChildCount();
            totalItems = imagesLayoutManager.getItemCount();
            scrollOutItems = imagesLayoutManager.findFirstVisibleItemPosition();


            if (dy > 0 && isScrolling && (currentItems + scrollOutItems == totalItems)) {
                if (Utils.isOnline()) {
                    isScrolling = false;
                    progress.setVisibility(View.VISIBLE);
                    imagesViewModel.getPhotos(category, ++currentPageIndex);
                } else {
                    displayToast(getString(R.string.no_network));
                }
            }
        }
    }
}

