package com.poolapps.simplecrud.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class PersonContract {

    public static final String AUTHORITY = "com.poolapps.simplecrud.personprovider";
    public static final Uri AUTHORITY_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();


    public interface Person extends BaseColumns {

        String PATH = "person";
        Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);

        String TABLE_NAME = PATH;
        String COLUMN_FIRST_NAME = "first_name";
        String COLUMN_LAST_NAME = "last_name";

        String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                        COLUMN_LAST_NAME  + " TEXT NOT NULL " +
                        ");";

        String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface VirtualPerson extends Person {
        String TABLE_NAME = "virtual_person";

        String SQL_CREATE =
                "CREATE VIRTUAL TABLE " + TABLE_NAME + " USING fts4( " +
                        _ID + " INTEGER, " +
                        COLUMN_FIRST_NAME + " TEXT, " +
                        COLUMN_LAST_NAME + " TEXT );";

        String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


}
