package com.poolapps.simplecrud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.poolapps.simplecrud.database.DBHelper;
import com.poolapps.simplecrud.database.PersonTable;
import com.poolapps.simplecrud.model.Person;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertTrue;

/**
 * Created by paulo on 29/11/2016.
 */

@RunWith(AndroidJUnit4.class)
public class DBHelperTest {

    DBHelper dbHelper;

    @Before
    public void setup(){
        getTargetContext().deleteDatabase(DBHelper.DB_FILE_NAME);
        dbHelper = new DBHelper(getTargetContext());
    }


    @Test
    public void createDatabaseTest(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    @Test
    public void insertingDataTest(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Person person = new Person("Paulo", "Henrique", 26, getTargetContext().getString(R.string.male_gender));
        long personRow = db.insert(PersonTable.TABLE_NAME, null, person.toValues());
        assertTrue(personRow != - 1);
    }

    @Test
    public void readDataTest(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Person person = new Person("Paulo", "Henrique", 26, getTargetContext().getString(R.string.male_gender));
        db.insert(PersonTable.TABLE_NAME, null, person.toValues());

        Cursor cursor = db.query(PersonTable.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        String firstName = cursor.getString(cursor.getColumnIndex(PersonTable.COLUMN_FIRST_NAME));
        assertTrue(firstName.equals("Paulo"));
        cursor.close();
    }
}
