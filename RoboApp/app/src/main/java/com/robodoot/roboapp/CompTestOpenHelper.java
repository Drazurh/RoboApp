package com.robodoot.roboapp;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
/**
 * Created by John on 11/12/2015.
 */
public class CompTestOpenHelper extends SQLiteOpenHelper {

    public static final String LOGTAG="RoboApp";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "compTest.db";
    private static final String TABLE_NAME = "compTests";
    private static final String COLUMN_ID = "testId";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_NEXT_ID = "nextId";

    private static final String COMPTESTS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_NEXT_ID + " INTEGER " +

                    ");";


    CompTestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMPTESTS_TABLE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_ver, int new_ver) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL(COMPTESTS_TABLE_CREATE);
    }
}
