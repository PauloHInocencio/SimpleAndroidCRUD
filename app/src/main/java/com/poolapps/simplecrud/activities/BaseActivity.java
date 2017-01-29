package com.poolapps.simplecrud.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.poolapps.simplecrud.R;
import com.poolapps.simplecrud.dialog.ConfirmDeletionDialog;
import com.poolapps.simplecrud.listeners.DeletionListener;


public abstract class BaseActivity extends AppCompatActivity
        implements ConfirmDeletionDialog.ConfirmDeletionDialogListener {

    protected abstract Fragment createFragment();

    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);

        if(mFragment == null){
            mFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("BaseActivity", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    //*** ConfirmDeletionDialogListener implementation ***//

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        try {
            ((DeletionListener) mFragment).onDialogPositiveClick();
        } catch (ClassCastException e) {
            throw new ClassCastException(mFragment.toString() + " Must implement the DeletionListener");
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        try {
            ((DeletionListener) mFragment).onDialogNegativeClick();
        } catch (ClassCastException e) {
            throw new ClassCastException(mFragment.toString() + " Must implement the DeletionListener");
        }
    }




}
