package org.waw.project.ate;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import org.waw.project.ate.adapter.AssetAdapter;
import org.waw.project.ate.helper.DividerItemDecoration;
import org.waw.project.ate.model.Asset;
import org.waw.project.ate.network.ApiClient;
import org.waw.project.ate.network.ApiInterface;
import org.waw.project.ate.utils.Constant;
import org.waw.project.ate.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetRegActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Asset> assetList = new ArrayList<>();

    RecyclerView recyclerView;
    AssetAdapter assetAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AssetRegActivity.ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private String deviceId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_reg);

        /*
         * Generate Toolbar
         */

        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Asset Registration List");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //onBackPressed();

            }
        });


        /*
         * Generate Floating Action Bar
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(), AssetRegTakeDataActivity.class));
                //startActivity(new Intent(view.getContext(), AssetRegTakePictureActivity.class));
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recylcerViewAsset);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        assetAdapter = new AssetAdapter(this, assetList);
        recyclerView.setAdapter(assetAdapter);
        actionModeCallback = new AssetRegActivity.ActionModeCallback();
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // show loader and fetch data
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getAssetList();
                    }
                }
        );
    }


    private void getAssetList() {

        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Asset>> call = apiService.getAssetRegistrationData(deviceId);
        call.enqueue(new Callback<List<Asset>>() {
            @Nullable
            @Override
            public void onResponse(Call<List<Asset>> call, Response<List<Asset>> response) {
                assetList.clear();
                for (Asset asset : response.body()) {
                    asset.setColor(getRandomMaterialColor("400"));
                    assetList.add(asset);
                }


                assetAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

                initDataIntent(assetList);
            }

            @Override
            public void onFailure(Call<List<Asset>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch Data " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initDataIntent(final List<Asset> assetList){

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        String assetType = assetList.get(position).getAssetTypeName();
                        String month_ = assetList.get(position).getMonth_();
                        String year_ = assetList.get(position).getYear_();

                        Intent assetRegDetailActivity = new Intent(getApplicationContext(), AssetRegDetailActivity.class);
                        assetRegDetailActivity.putExtra(Constant.KEY_MONTH_, month_);
                        assetRegDetailActivity.putExtra(Constant.KEY_YEAR_, year_);

                        startActivity(assetRegDetailActivity);

                    }
                }));

    }


    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    @Override
    public void onRefresh() {
        getAssetList();
    }

    public class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}
