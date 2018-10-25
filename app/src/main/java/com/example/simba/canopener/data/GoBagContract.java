package com.example.simba.canopener.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class GoBagContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.simba.canopener";

    // The base content URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "Go bad" directory
    public static final String PATH_GO_BAG = "go_bag";
    // This path is for the "items" directory
    public static final String PATH_ITEMS = "items";

    public static final class GoBagEntery implements BaseColumns{
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_GOBAG_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GO_BAG).build();
        public static final Uri CONTENT_ITEM_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

        // Task table and column names for go bag
        public static final String GO_BAG_TABLE_NAME = "go_bag";
        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION= "description";
        public static final String COLUMN_TIME_STAMP = "time_stamp";

        //Table and column names for items
        public static final String ITEMS_TABLE_NAME = "items";
        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_ITEM = "item";
        public static final String COLUMN_GO_BAG_ID = "go_bag_id";
        public static final String COLUMN_WEIGHT = "weight";
        //"time_stamp" in addition to items column

    }
}
