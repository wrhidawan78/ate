package org.waw.project.ate.model;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.waw.project.ate.ui.AssetRegistrationInfoFragment;

import java.util.ArrayList;

public class AssetRegistrationInfoPage extends Page {

    public static final String ASSET_ID_DATA_KEY = "assetID";
    public static final String PROJECT_CODE_DATA_KEY = "projectCode";
    public static final String REMARK_DATA_KEY = "remark";



    public AssetRegistrationInfoPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return AssetRegistrationInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

        dest.add(new ReviewItem("Asset ID", mData. getString(ASSET_ID_DATA_KEY), getKey(), 0, null));
        dest.add(new ReviewItem("Project Code", mData.getString(PROJECT_CODE_DATA_KEY), getKey(),0,null));
        dest.add(new ReviewItem("Remark", mData.getString(REMARK_DATA_KEY), getKey(), 0, null));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(ASSET_ID_DATA_KEY));
    }
}
