package com.example.anga_.canopener.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class GoBagContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.anga_.canopener";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "item" directory
    public static final String PATH_GO_BAG = "go_bag";

    public static final class GoBagEntery implements BaseColumns{
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_GOBAG_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GO_BAG).build();

        // Task table and column names
        public static final String GO_BAG_TABLE_NAME = "go_bag";
        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ITEM= "item";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_TIME_STAMP = "time_stamp";
    }
}
