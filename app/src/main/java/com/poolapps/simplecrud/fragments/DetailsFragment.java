package com.poolapps.simplecrud.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.poolapps.simplecrud.R;
import com.poolapps.simplecrud.activities.DetailsActivity;
import com.poolapps.simplecrud.database.PersonContract;
import com.poolapps.simplecrud.dialog.ConfirmDeletionDialog;
import com.poolapps.simplecrud.listeners.DeletionListener;

import static android.app.Activity.RESULT_OK;


public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        DeletionListener {


    private static final int LOADER_DETAIL_PERSON_ID = 0;

    private TextView mFirstNameView;
    private TextView mLastNameView;
    private String oldFirstName;
    private String oldLastName;
    private Uri mUri;
    private ContentValues values = new ContentValues();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_fragment, container, false);
        mFirstNameView = (TextView) view.findViewById(R.id.tvFirstName);
        mLastNameView = (TextView) view.findViewById(R.id.tvLastName);
        oldFirstName = "";
        oldLastName = "";

        Button btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPerson();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOADER_DETAIL_PERSON_ID, null, this);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null ) {
            mUri = extras.getParcelable(DetailsActivity.EXTRA_PERSON_URI);
        } else {
            mUri = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mUri != null) {
            inflater.inflate(R.menu.detail_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_remove:
                showDeletionDialog();
                break;
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return true;
    }


    private void deletePerson(){

        getContext().getContentResolver().delete(mUri, null, null);
        getActivity().getIntent().putExtra(MainFragment.ACTION_KEY, MainFragment.PERSON_DELETED);
        getActivity().setResult(RESULT_OK, getActivity().getIntent());
        getActivity().finish();
    }

    private void registerPerson() {

        mFirstNameView.setError(null);
        mLastNameView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();

        if (oldFirstName.equals(firstName) && oldLastName.equals(lastName)){
            getActivity().finish();
            return;
        }

        View emptyView = checkForEmptyView(firstName, lastName);



        if(emptyView != null){
            emptyView.requestFocus();
        } else {
            values.clear();
            values.put(PersonContract.Person.COLUMN_FIRST_NAME, firstName);
            values.put(PersonContract.Person.COLUMN_LAST_NAME, lastName);

            if(mUri != null){
                getActivity().getIntent().putExtra(MainFragment.ACTION_KEY, MainFragment.PERSON_UPDATED);
                getContext().getContentResolver().update(mUri, values, null, null);
            }else {
                getActivity().getIntent().putExtra(MainFragment.ACTION_KEY, MainFragment.PERSON_INSERTED);
                getContext().getContentResolver().insert(PersonContract.Person.CONTENT_URI, values);
            }

            getActivity().setResult(RESULT_OK, getActivity().getIntent());
            getActivity().finish();
        }
    }

    private View checkForEmptyView(String firstName, String lastName){

        if(TextUtils.isEmpty(firstName)){
            mFirstNameView.setError(getString(R.string.empty_first_name));
            return mFirstNameView;
        }

        if(TextUtils.isEmpty(lastName)){
            mLastNameView.setError(getString(R.string.empty_last_name));
            return mLastNameView;
        }

        return null;
    }


    //*** Cursor Loader implementation ***//

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       if (mUri == null){
           return null;
       }

        String[] projection =  {PersonContract.Person.COLUMN_FIRST_NAME,
                                PersonContract.Person.COLUMN_LAST_NAME};
        return new CursorLoader(getContext(),
                mUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
       if (c != null && c.moveToFirst()){
           oldFirstName = c.getString(c.getColumnIndex(PersonContract.Person.COLUMN_FIRST_NAME));
           oldLastName = c.getString(c.getColumnIndex(PersonContract.Person.COLUMN_LAST_NAME));
           mFirstNameView.setText(oldFirstName);
           mLastNameView.setText(oldLastName);
       }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //*** DeletionListener implementation ***//

    public void showDeletionDialog() {
        DialogFragment dialog = new ConfirmDeletionDialog();
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }


    @Override
    public void onDialogPositiveClick() {
        deletePerson();
    }

    @Override
    public void onDialogNegativeClick() {
        Log.d(DetailsFragment.class.getSimpleName(), " Deletion canceled!");
    }



}
