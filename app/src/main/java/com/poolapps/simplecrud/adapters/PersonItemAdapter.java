package com.poolapps.simplecrud.adapters;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poolapps.simplecrud.R;
import com.poolapps.simplecrud.activities.DetailsActivity;
import com.poolapps.simplecrud.fragments.MainFragment;

public class PersonItemAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private Cursor personCursor;
    private Fragment mFragment;
    private SparseArray<Uri> mSelectedPersonURIs;

    public PersonItemAdapter(Fragment fragment){
        mFragment = fragment;
        mSelectedPersonURIs = new SparseArray<>();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item_view, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        if (personCursor != null && !personCursor.isClosed() && personCursor.moveToPosition(position)){
            holder.setData(personCursor);
            final Uri uri = holder.uri;
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mFragment.getActivity(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.EXTRA_PERSON_URI, uri);
                    mFragment.startActivityForResult(intent, MainFragment.DETAILS_REQUEST_WITH_SEARCH_OPEN);
                }
            });

            holder.view.setBackgroundColor(mSelectedPersonURIs.get(position) != null ?  0x9934B5E4 : Color.TRANSPARENT);

        }
    }

    @Override
    public int getItemCount() {
        return (personCursor == null ? 0 : personCursor.getCount());
    }

    public void swapCursor(Cursor newPersonCursor) {
        if (personCursor != null) {
            personCursor.close();
        }
        personCursor = newPersonCursor;
        notifyDataSetChanged();
    }

    public void toggleSelection(int position, Uri uri){
        boolean isSelected = mSelectedPersonURIs.get(position) != null;
        if (isSelected){
            mSelectedPersonURIs.delete(position);
        } else {
            mSelectedPersonURIs.put(position, uri);
        }
        notifyDataSetChanged();
    }

    public void removeSelection(){
        mSelectedPersonURIs = new SparseArray<>();
        notifyDataSetChanged();
    }


    public int getSelectedURIsCount(){
        return mSelectedPersonURIs.size();
    }

    public SparseArray<Uri>  getSelectedPersonURIs(){
        return mSelectedPersonURIs;
    }



}
