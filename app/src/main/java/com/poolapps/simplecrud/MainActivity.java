package com.poolapps.simplecrud;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.poolapps.simplecrud.utilities.DefaultActivity;

public  class MainActivity extends DefaultActivity {

    @Override
    protected Fragment createFragment() { return new MainFragment(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                startActivity(new Intent(MainActivity.this, DetailsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
