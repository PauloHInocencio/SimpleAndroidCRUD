package com.poolapps.simplecrud.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.poolapps.simplecrud.database.PersonContract;


public class Person implements Parcelable {

    private long id;
    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public Person(String firstName, String lastName, long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public long getId(){
        return id;
    }


    public ContentValues toValues(){

        ContentValues values = new ContentValues();
        values.put(PersonContract.Person.COLUMN_FIRST_NAME, this.firstName);
        values.put(PersonContract.Person.COLUMN_LAST_NAME, this.lastName);
        values.put(PersonContract.Person._ID, this.id);

        return values;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName + '\'' +
                ", id='" + this.id + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeLong(this.id);
    }

    protected Person(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.id = in.readLong();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
