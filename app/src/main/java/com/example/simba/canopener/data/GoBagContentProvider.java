package com.example.simba.canopener.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.simba.canopener.data.GoBagContract.GoBagEntery.GO_BAG_TABLE_NAME;
import static com.example.simba.canopener.data.GoBagContract.GoBagEntery.ITEMS_TABLE_NAME;

public class GoBagContentProvider extends ContentProvider {

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int GO_BAG = 100;
    public static final int ITEMS = 101;

    //WITH ID
    public static final int GO_BAG_WITH_ID = 200;
    public static final int ITEMS_WITH_ID = 201;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(GoBagContract.AUTHORITY, GoBagContract.PATH_GO_BAG, GO_BAG);
        uriMatcher.addURI(GoBagContract.AUTHORITY, GoBagContract.PATH_ITEMS, ITEMS);

        uriMatcher.addURI(GoBagContract.AUTHORITY, GoBagContract.PATH_GO_BAG + "/#", GO_BAG_WITH_ID);
        uriMatcher.addURI(GoBagContract.AUTHORITY, GoBagContract.PATH_ITEMS + "/#", ITEMS_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a DailySalesDbHelper that's initialized in the onCreate() method
    private DatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new DatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case GO_BAG:
                retCursor =  db.query(GO_BAG_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            //case items
            case ITEMS:
                retCursor =  db.query(ITEMS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        long id ; //id of inserted value

        switch (match) {
            case GO_BAG:
                // Insert new values into the database
                // Inserting values into items table
                id = db.insert(GO_BAG_TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(GoBagContract.GoBagEntery.CONTENT_GOBAG_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case ITEMS:
                // Insert new values into the database
                // Inserting values into items table
                id = db.insert(ITEMS_TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(GoBagContract.GoBagEntery.CONTENT_ITEM_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        String id;

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case GO_BAG_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(GO_BAG_TABLE_NAME, "_id=?", new String[]{id});
                break;
            //Case item
            case ITEMS_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(ITEMS_TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        int mRowsUpdated = 0; // Defines a variable to contain the number of updated rows

        switch (match){

            case GO_BAG:
                mRowsUpdated = db.update(GO_BAG_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (mRowsUpdated != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //Return number of rows updated
        return mRowsUpdated;
    }
}
