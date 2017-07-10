package com.poolapps.simplecrud.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.poolapps.simplecrud.R;
import com.poolapps.simplecrud.activities.DetailsActivity;
import com.poolapps.simplecrud.adapters.PersonItemAdapter;
import com.poolapps.simplecrud.adapters.PersonViewHolder;
import com.poolapps.simplecrud.database.PersonContract;
import com.poolapps.simplecrud.dialog.ConfirmDeletionDialog;
import com.poolapps.simplecrud.listeners.DeletionListener;
import com.poolapps.simplecrud.listeners.RecyclerClickListener;
import com.poolapps.simplecrud.listeners.RecyclerTouchListener;
import com.poolapps.simplecrud.utilities.SimpleNames;

import static android.app.Activity.RESULT_OK;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
                                                      ActionMode.Callback,
        DeletionListener{

    private static final int LOADER_PERSONS_ID = 0;
    private static final int LOADER_PERSONS_NAME = 1;
    private static final String QUERY_KEY = "query_key";

    public static final int DETAILS_REQUEST_WITH_SEARCH_OPEN = 1001;

    public static final String ACTION_KEY = "action_key";
    public static final String PERSON_INSERTED = "person_inserted";
    public static final String PERSON_DELETED = "person_deleted";
    public static final String PERSON_UPDATED = "person_updated";


    RecyclerView mRVPerson;
    PersonItemAdapter mAdapter;
    TextView mEmptyTextView;
    ActionMode mActionMode;

    MenuItem mSearchItem;
     SearchView mSearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOADER_PERSONS_ID, null, this);
        updateViews();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mRVPerson = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRVPerson.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRVPerson.setHasFixedSize(true);

        mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_new_person);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivityForResult(intent, 2002);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == DETAILS_REQUEST_WITH_SEARCH_OPEN){
            getActivity().getSupportLoaderManager().restartLoader(LOADER_PERSONS_ID, null, MainFragment.this);
        }

        if (data != null){


            String action = data.getExtras().getString(ACTION_KEY);
            if (action != null) {
                switch (action) {
                    case PERSON_DELETED:
                        Toast.makeText(getActivity(), "Person deleted", Toast.LENGTH_SHORT).show();
                        break;
                    case PERSON_INSERTED:
                        Toast.makeText(getActivity(), "Person inserted", Toast.LENGTH_SHORT).show();
                        break;
                    case PERSON_UPDATED:
                        Toast.makeText(getActivity(), "Person updated", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        mSearchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryPerson(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryPerson(newText);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("*****", "onMenuItemActionCollapse");
                getActivity().getSupportLoaderManager().restartLoader(LOADER_PERSONS_ID, null, MainFragment.this);
                mSearchItem = null;
                mSearchView = null;
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    void queryPerson(String query){

        if (TextUtils.isEmpty(query)){
            Log.d("******", "query is Empty.");
            getActivity().getSupportLoaderManager().restartLoader(LOADER_PERSONS_ID, null, this);
            return;
        }

        Bundle args = new Bundle();
        args.putString(QUERY_KEY, query);
        getActivity().getSupportLoaderManager().restartLoader(LOADER_PERSONS_NAME, args, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_sample:
                insertSampleData();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateViews(){
        mAdapter = new PersonItemAdapter(this);
        mRVPerson.setAdapter(mAdapter);
        mRVPerson.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRVPerson, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mActionMode != null) {
                    onListPersonSelect(position, view);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                onListPersonSelect(position, view);
            }
        }));

    }


    private void onListPersonSelect(int position, View view){

        PersonViewHolder holder = (PersonViewHolder) mRVPerson.getChildViewHolder(view);
        Uri uri = holder.uri;

        mAdapter.toggleSelection(position, uri);

        boolean hasCheckItems = mAdapter.getSelectedURIsCount() > 0;

        if (hasCheckItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(this);
        } else if (!hasCheckItems && mActionMode != null) {
            mActionMode.finish();
        }

        if (mActionMode != null){
            mActionMode.setTitle(String.valueOf(mAdapter.getSelectedURIsCount()) + " items selected.");
        }
    }


    public void onDeleteRows(){
        SparseArray<Uri> selectedURIs = mAdapter.getSelectedPersonURIs();

        for (int i = (selectedURIs.size() - 1); i >= 0; i--){
            Uri uri = selectedURIs.get( selectedURIs.keyAt(i) );
            Log.d("MainFragment", "URI deleted : " + uri.toString());
            getContext().getContentResolver().delete(uri, null, null);
            mAdapter.notifyDataSetChanged();
        }
        Toast.makeText(getActivity(), selectedURIs.size() + " registers deleted.", Toast.LENGTH_SHORT).show();

        if (mSearchItem != null){
            getActivity().getSupportLoaderManager().restartLoader(LOADER_PERSONS_ID, null, this);
        }

        mActionMode.finish();
    }

    public void insertSampleData(){
        ContentValues values = new ContentValues(2);
        SimpleNames.Name name;


        for (int i = 0; i < SimpleNames.names.size(); i++) {
            name = SimpleNames.names.get(i);
            values.put(PersonContract.Person.COLUMN_FIRST_NAME, name.getFirstName());
            values.put(PersonContract.Person.COLUMN_LAST_NAME, name.getListName());
            getContext().getContentResolver().insert(PersonContract.Person.CONTENT_URI, values);
            values.clear();
        }
        getActivity().getSupportLoaderManager().restartLoader(LOADER_PERSONS_ID, null, this);
    }


    //*** Cursor Loader implementation ***//

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        String[] projection = {
                PersonContract.Person._ID,
                PersonContract.Person.COLUMN_FIRST_NAME,
                PersonContract.Person.COLUMN_LAST_NAME
        };

        switch (id){
            case LOADER_PERSONS_ID:
                loader = new CursorLoader(getContext(),
                        PersonContract.Person.CONTENT_URI,
                        projection,
                        null,
                        null,
                        PersonContract.Person.COLUMN_FIRST_NAME);
                break;

            case LOADER_PERSONS_NAME:
                String textQuery = args.getString(QUERY_KEY);
                Uri uri = Uri.withAppendedPath(PersonContract.Person.CONTENT_URI, textQuery);
                loader = new CursorLoader(getContext(),
                        uri,
                        projection,
                        null,
                        null,
                        null);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() == 0){
            mEmptyTextView.setVisibility(View.VISIBLE);
            mRVPerson.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.GONE);
            mRVPerson.setVisibility(View.VISIBLE);
            ((PersonItemAdapter) mRVPerson.getAdapter()).swapCursor(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((PersonItemAdapter) mRVPerson.getAdapter()).swapCursor(null);
    }


    //*** Action Mode implementation ***//

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.action_remove).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_remove:
                showDeletionDialog();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
       mAdapter.removeSelection();
       mActionMode = null;
    }


    //*** DeletionListener implementation ***//

    public void showDeletionDialog() {
        DialogFragment dialog = new ConfirmDeletionDialog();
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void onDialogPositiveClick() {
        onDeleteRows();
    }

    @Override
    public void onDialogNegativeClick() {
        mActionMode.finish();
    }
}
