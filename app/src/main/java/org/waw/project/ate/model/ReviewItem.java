/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.waw.project.ate.model;

import android.graphics.Bitmap;

public class ReviewItem {
    public static final int DEFAULT_WEIGHT = 0;

    private int mWeight;
    private String mTitle;
    private String mDisplayValue;
    private String mPageKey;
    private Bitmap mPicture;

    public ReviewItem(String title, String displayValue, String pageKey, Bitmap picture) {
        this(title, displayValue, pageKey, DEFAULT_WEIGHT, picture);
    }

    public ReviewItem(String title, String displayValue, String pageKey, int weight, Bitmap picture) {
        mTitle = title;
        mDisplayValue = displayValue;
        mPageKey = pageKey;
        mWeight = weight;
        mPicture = picture;
    }

    public String getDisplayValue() {
        return mDisplayValue;
    }

    public void setDisplayValue(String displayValue) {
        mDisplayValue = displayValue;
    }

    public String getPageKey() {
        return mPageKey;
    }

    public void setPageKey(String pageKey) {
        mPageKey = pageKey;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap mPicture) {
        this.mPicture = mPicture;
    }
}
