package com.example.simba.canopener.loaders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.AsyncTaskLoader;

import com.example.simba.canopener.data.DatabaseHelper;

public class GoBagLoader extends AsyncTaskLoader {
    // Member variable for a DailySalesDbHelper that's initialized in the onCreate() method
    private String mRawQuery;
    private String [] mSelectionArgs;
    DatabaseHelper mDBHelper;
    Cursor mCursor;

    public GoBagLoader(Context context, Cursor cursor, String rawQuery, String [] selectionArgs) {
        super(context);
        mCursor = cursor;
        mRawQuery = rawQuery;
        mSelectionArgs = selectionArgs;

        mDBHelper = new DatabaseHelper(context);
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mCursor);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Override
    public Object loadInBackground() {

        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        try{
            return db.rawQuery(
                    mRawQuery,
                    mSelectionArgs
            );
        }catch (Exception e){
            return null;
        }
    }

    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        mCursor = data;
        super.deliverResult(data);
    }
}
