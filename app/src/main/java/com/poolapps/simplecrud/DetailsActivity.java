package com.poolapps.simplecrud;

import android.support.v4.app.Fragment;
import android.view.Menu;

import com.poolapps.simplecrud.utilities.DefaultActivity;

public class DetailsActivity extends DefaultActivity {

    @Override
    protected Fragment createFragment() { return new DetailsFragment(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }
}
