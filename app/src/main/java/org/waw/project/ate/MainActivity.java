package org.waw.project.ate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.waw.project.ate.adapter.DashAdapter;
import org.waw.project.ate.model.Dashboard;
import org.waw.project.ate.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    RecyclerView recyclerView;
    ArrayList<Dashboard> dashModelArrayList;
    DashAdapter dashAdapter;
    Context mContext;
    String currentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleHome(R.id.toolbar, R.id.iv_title, R.drawable.ic_burger, R.drawable.logo_actbar);
        mContext = this;

        checkApplicationCurrentVersion();

        dashModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv1);

        String heads[] = {"Asset Reg", "Tasks", "Presensi", "CA Approval", "PO Tracking", "Settings"};

        String subs[] = {"Asset Registration", "View My Tasks", "My Presensi", "CA Approval", "Track My Request", "Set preferences"};

        int images[] = {R.drawable.ic_devices_white_24dp, R.drawable.ic_assignment_ind_black_24dp, R.drawable.ic_presensi_24dp, R.drawable.ic_ca_approval_24dp,
                R.drawable.ic_local_grocery_store_black_24dp,R.drawable.settings};

        for (int count = 0; count < heads.length; count++) {
            Dashboard Dashboard = new Dashboard();
            Dashboard.setHead(heads[count]);
            Dashboard.setSub(subs[count]);
            Dashboard.setImage(images[count]);
            dashModelArrayList.add(Dashboard);
        }


        recyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 2));
        dashAdapter = new DashAdapter(mContext, dashModelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(dashAdapter);

        initDataIntent(dashModelArrayList);

    }

    public void checkApplicationCurrentVersion() {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
        Log.e("currentVersion", currentVersion + " Package Name " + mContext.getPackageName());
    }

    private void initDataIntent(final List<Dashboard> dashModelArrayList){

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        // String assetType = dashModelArrayList.get(position).getHead();
                        //Toast.makeText(mContext, position, Toast.LENGTH_LONG).show();

                        //Intent intent = new Intent(mContext, AssetRegActivity.class);
                        //startActivity(intent);
                        String inActiveMenu = "Inactive Menu";
                        final Intent intent;
                        switch (position){
                            case 0:
                                intent =  new Intent(mContext, AssetRegActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 3:
                                intent =  new Intent(mContext, ApprovalActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), inActiveMenu, Toast.LENGTH_SHORT).show();
                                break;
                        }

                        //startActivity(intent);


                        /*
                        Intent detailMatkul = new Intent(mContext, MatkulDetailActivity.class);
                        detailMatkul.putExtra(Constant.KEY_ID_MATKUL, id);
                        detailMatkul.putExtra(Constant.KEY_NAMA_DOSEN, namadosen);
                        detailMatkul.putExtra(Constant.KEY_MATKUL, matkul);
                        startActivity(detailMatkul);
                        */
                    }
                }));

    }
}
