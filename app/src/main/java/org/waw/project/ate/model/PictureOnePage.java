package org.waw.project.ate.model;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.waw.project.ate.ui.PictureOneFragment;

import java.util.ArrayList;

public class PictureOnePage extends Page {
    public static final String PICTURE_NAME = "picture";



    public PictureOnePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);

    }

    @Override
    public Fragment createFragment() {
        return PictureOneFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Picture", mData.getString(PICTURE_NAME + "_" + mTitle.trim()), getKey(), 0, getPicture()));

    }


}
