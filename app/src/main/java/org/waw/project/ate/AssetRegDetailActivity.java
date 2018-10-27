package org.waw.project.ate;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
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

import org.waw.project.ate.adapter.AssetAdapter;
import org.waw.project.ate.adapter.AssetDetailAdapter;
import org.waw.project.ate.helper.DividerItemDecoration;
import org.waw.project.ate.model.Asset;
import org.waw.project.ate.model.AssetDetail;
import org.waw.project.ate.network.ApiClient;
import org.waw.project.ate.network.ApiInterface;
import org.waw.project.ate.utils.Constant;
import org.waw.project.ate.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetRegDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<AssetDetail> assetDetailList = new ArrayList<>();
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String deviceId;
    private String month_, year_;
    AssetDetailAdapter assetDetailAdapter;
    private AssetRegDetailActivity.ActionModeCallback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_reg_detail);

        getSupportActionBar().setTitle("Asset Registration Detail");
        setupActionBar();

        Intent intent = getIntent();
        month_ = intent.getStringExtra(Constant.KEY_MONTH_);
        year_  = intent.getStringExtra(Constant.KEY_YEAR_);

        recyclerView = (RecyclerView) findViewById(R.id.recylcerViewDetailAsset);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_detail);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        assetDetailAdapter = new AssetDetailAdapter(this, assetDetailList);
        recyclerView.setAdapter(assetDetailAdapter);
        actionModeCallback = new AssetRegDetailActivity.ActionModeCallback();
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getAssetDetailList();
            }
        });

    }


    private void getAssetDetailList() {

        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<AssetDetail>> call = apiService.getAssetRegistrationDetailData(deviceId, month_, year_ );
        call.enqueue(new Callback<List<AssetDetail>>() {
            @Nullable
            @Override
            public void onResponse(Call<List<AssetDetail>> call, Response<List<AssetDetail>> response) {
                assetDetailList.clear();
                for (AssetDetail assetDetail : response.body()) {
                    //asset.setColor(getRandomMaterialColor("400"));
                    assetDetailList.add(assetDetail);
                }


                assetDetailAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<List<AssetDetail>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch Data " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), AssetRegActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getAssetDetailList();
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
