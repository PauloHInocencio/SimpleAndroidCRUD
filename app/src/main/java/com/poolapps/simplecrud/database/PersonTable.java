package com.poolapps.simplecrud.database;

/**
 * Created by paulo on 29/11/2016.
 */

public class PersonTable {

    public static final String TABLE_NAME = "person";

    public static final String COLUMN_ID = "person_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    COLUMN_LAST_NAME  + " TEXT NOT NULL, " +
                    COLUMN_AGE + " INTEGER NOT NULL, " +
                    COLUMN_GENDER + " TEXT NOT NULL" +
            ");";

    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NAME;
}
