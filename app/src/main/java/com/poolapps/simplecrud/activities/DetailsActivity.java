package com.poolapps.simplecrud.activities;

import android.support.v4.app.Fragment;

import com.poolapps.simplecrud.fragments.DetailsFragment;

public class DetailsActivity extends BaseActivity {

    public static final String EXTRA_PERSON_URI = "person_uri";

    @Override
    protected Fragment createFragment() { return new DetailsFragment(); }

}
