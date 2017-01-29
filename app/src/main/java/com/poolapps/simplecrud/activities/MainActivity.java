package com.poolapps.simplecrud.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.poolapps.simplecrud.fragments.MainFragment;

public  class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() { return new MainFragment(); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
