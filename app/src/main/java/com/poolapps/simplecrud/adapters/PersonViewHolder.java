package com.poolapps.simplecrud.adapters;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.poolapps.simplecrud.R;
import com.poolapps.simplecrud.database.PersonContract;


public class PersonViewHolder extends RecyclerView.ViewHolder  {
    private TextView textName;
    public Uri uri;
    public View view;


    PersonViewHolder(View itemView) {
        super(itemView);
        textName = (TextView) itemView.findViewById(R.id.tvName);
        view = itemView;
    }

    void setData(Cursor c){
        int id = c.getInt(c.getColumnIndex(PersonContract.Person._ID));
        String firstName = c.getString(c.getColumnIndex(PersonContract.Person.COLUMN_FIRST_NAME));
        String lastName = c.getString(c.getColumnIndex(PersonContract.Person.COLUMN_LAST_NAME));
        textName.setText(String.format(" %s  %s", firstName, lastName));
        uri = ContentUris.withAppendedId(PersonContract.Person.CONTENT_URI, id);
    }


}
