package com.poolapps.simplecrud.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.poolapps.simplecrud.database.DBHelper;
import com.poolapps.simplecrud.database.PersonContract;

public class PersonProvider extends ContentProvider {

    private static final int CODE_ALL_PERSONS = 0;
    private static final int CODE_PERSON_ID = 1;
    private static final int CODE_PERSON_NAME = 2;


    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        URI_MATCHER.addURI(PersonContract.AUTHORITY, PersonContract.Person.PATH, CODE_ALL_PERSONS);
        URI_MATCHER.addURI(PersonContract.AUTHORITY, PersonContract.Person.PATH + "/#", CODE_PERSON_ID);
        URI_MATCHER.addURI(PersonContract.AUTHORITY, PersonContract.VirtualPerson.PATH + "/*", CODE_PERSON_NAME);

    }

    private DBHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        if (projection == null){
            throw new IllegalArgumentException("projection can't be null");
        }

        sortOrder = (sortOrder == null ? PersonContract.Person.COLUMN_FIRST_NAME : sortOrder);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        final int code = URI_MATCHER.match(uri);
        switch (code){
            case CODE_ALL_PERSONS:
                cursor = db.query(PersonContract.Person.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                updatePersonVirtualTable(cursor);
                break;
            case CODE_PERSON_ID:
                if (selection == null){
                    selection = BaseColumns._ID + " = " + uri.getLastPathSegment();
                } else {
                    throw new IllegalArgumentException("selection must be null when" +
                            "specifying ID as part of uri");
                }
                cursor = db.query(PersonContract.Person.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_PERSON_NAME:
                if (selection == null && selectionArgs == null){

                    //FTS tables actually has a HIDDEN column with the same name as the table itself.
                    selection = PersonContract.VirtualPerson.TABLE_NAME + " MATCH ? ";
                    selectionArgs = new String[]{ uri.getLastPathSegment() + "*" };


                } else {
                    throw new IllegalArgumentException("selection must be null whe "+
                            "specifying an text as part of uri.");
                }
                cursor = db.query(PersonContract.VirtualPerson.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        long id;
        final int code = URI_MATCHER.match(uri);
        switch (code){
            case CODE_ALL_PERSONS:
                id = dbHelper
                        .getWritableDatabase()
                        .insert(PersonContract.Person.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }


        notifyUris(uri);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int rowCont;

        final int code = URI_MATCHER.match(uri);
        switch (code){
            case CODE_ALL_PERSONS:
                rowCont = dbHelper
                        .getWritableDatabase()
                        .delete(PersonContract.Person.TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_PERSON_ID:
                if (selection == null && selectionArgs == null){
                        selection = BaseColumns._ID + " = ?";
                        selectionArgs = new String[]{uri.getLastPathSegment()};
                        rowCont = dbHelper
                            .getWritableDatabase()
                            .delete(PersonContract.Person.TABLE_NAME, selection, selectionArgs);
                } else {
                    throw new IllegalArgumentException("Selection must be null when"
                            + "specifying ID as part of uri.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }

        notifyUris(uri);
        return rowCont;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereClause, String[] whereArgs) {

        int rowCount;

        final int code = URI_MATCHER.match(uri);
        switch (code){
            case CODE_ALL_PERSONS:
                rowCount = dbHelper
                        .getWritableDatabase()
                        .update(PersonContract.Person.TABLE_NAME, values, whereClause, whereArgs);
                break;
            case CODE_PERSON_ID:
                if (whereClause == null && whereArgs == null){
                    whereClause = BaseColumns._ID + " = ?";
                    whereArgs = new String[]{ uri.getLastPathSegment() };
                } else {
                    throw new IllegalArgumentException("whereClause must be null when " +
                    "specifying ID as part of uri.");
                }

                rowCount = dbHelper
                        .getWritableDatabase()
                        .update(PersonContract.Person.TABLE_NAME, values, whereClause, whereArgs);

                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }

        notifyUris(uri);
        return rowCount;
    }



    private void notifyUris(Uri affectedUri){
        final ContentResolver contentResolver = getContext().getContentResolver();
        if (contentResolver != null){
            contentResolver.notifyChange(affectedUri, null);
        }
    }

    private void updatePersonVirtualTable(Cursor data){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        db.delete(PersonContract.VirtualPerson.TABLE_NAME, null, null);

        try {
            final String COLUMN_FIRST_NAME = PersonContract.VirtualPerson.COLUMN_FIRST_NAME;
            final String COLUMN_LAST_NAME = PersonContract.VirtualPerson.COLUMN_LAST_NAME;

            for (int i =0; i < data.getCount(); i++){
                if (data.moveToPosition(i)){
                    ContentValues values = new ContentValues(2);
                    values.put(BaseColumns._ID, data.getInt(data.getColumnIndex(BaseColumns._ID)));
                    values.put(COLUMN_FIRST_NAME, data.getString(data.getColumnIndex(COLUMN_FIRST_NAME)));
                    values.put(COLUMN_LAST_NAME, data.getString(data.getColumnIndex(COLUMN_LAST_NAME)));
                    db.insert(PersonContract.VirtualPerson.TABLE_NAME, null, values);
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

}
