package com.robodoot.roboapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by John on 11/12/2015.
 */
public class CompTestDataSource {

    public static final String LOGTAG="RoboApp";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public CompTestDataSource(Context context){
        dbhelper = new CompTestOpenHelper(context);
        database = dbhelper.getWritableDatabase();
    }

    public void open(){
        Log.i(LOGTAG, "Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database closed");
        dbhelper.close();
    }
}
