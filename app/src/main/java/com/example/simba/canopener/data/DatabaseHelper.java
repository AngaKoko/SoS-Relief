package com.example.simba.canopener.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "SpaceApp.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    final String CREATE_GO_BAG_TABLE = "CREATE TABLE "  + GoBagContract.GoBagEntery.GO_BAG_TABLE_NAME+ " (" +
            GoBagContract.GoBagEntery._ID + " INTEGER PRIMARY KEY, " +
            GoBagContract.GoBagEntery.COLUMN_NAME + " TEXT NOT NULL, " +
            GoBagContract.GoBagEntery.COLUMN_ITEM+ " TEXT NOT NULL, " +
            GoBagContract.GoBagEntery.COLUMN_WEIGHT + " REAL, " +
            GoBagContract.GoBagEntery.COLUMN_TIME_STAMP+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
            ");";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)

        db.execSQL(CREATE_GO_BAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GoBagContract.GoBagEntery.GO_BAG_TABLE_NAME);
    }
}
