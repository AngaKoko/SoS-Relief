package com.example.simba.canopener;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simba.canopener.Adapter.GoBagItemsAdapter;
import com.example.simba.canopener.data.DatabaseHelper;
import com.example.simba.canopener.data.GoBagContract;
import com.example.simba.canopener.data.TaskContract;
import com.example.simba.canopener.loaders.GoBagLoader;
import com.example.simba.canopener.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GoBagListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        GoBagItemsAdapter.GoBagItemsAdapterOnLongClickLHandler, GoBagItemsAdapter.GoBagItemsAdapterOnclickHandler {

    private static final int ITEM_LOADER_ID = 0;

    Cursor mCursor;
    GoBagItemsAdapter mAdapter;

    Button mButton;
    RecyclerView mRecyclerView;
    Spinner mSpinner;

    TextView mWeightOfItemTextView, mWeightOfBagTextView, mNumberOfItemsTextView;
    EditText mGoBagNameEditText;

    ArrayList<String> mGoBagArray;
    double mWeightOfItem = 0;
    double mWeightOfBag = 0;
    int mNumberOfItems = 0;
    String mGoBagName;
    static long mTimeCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_go_bag);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoBagArray = new ArrayList<>();

        mButton = findViewById(R.id.add_button);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSpinner = findViewById(R.id.go_bag_spinner);
        mWeightOfBagTextView = findViewById(R.id.weight_of_go_bag_text_view);
        mWeightOfItemTextView = findViewById(R.id.weight_of_item_text_view);
        mGoBagNameEditText = findViewById(R.id.go_bag_name_edit_text);
        mNumberOfItemsTextView = findViewById(R.id.number_of_items_text_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            mGoBagName = bundle.getString(TaskContract.GO_BAG_NAME);
            mTimeCreated = bundle.getLong(TaskContract.GO_BAG_TIME_STAMP);
            mGoBagNameEditText.setText(mGoBagName);
        }

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new GoBagItemsAdapter(this, mCursor ,this, this);
        mRecyclerView.setAdapter(mAdapter);

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getLoaderManager().initLoader(ITEM_LOADER_ID, null, this);

        loadSpinner();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    mWeightOfItem = 0;
                    mWeightOfItemTextView.setText("0g");
                    mButton.setVisibility(View.GONE);
                } else {
                    mWeightOfItem = getWeightOfItem(mSpinner.getSelectedItem().toString());
                    mWeightOfItemTextView.setText(mWeightOfItem+"");
                    mButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    /**
     * add a go-bag item to the database
     */
    private void addItem(){

        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_ITEM, mSpinner.getSelectedItem().toString());
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_GO_BAG_ID, mTimeCreated);
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_WEIGHT, mWeightOfItem);
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_TIME_STAMP, ts);

        if(!mGoBagNameEditText.getText().toString().trim().isEmpty()) {
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
            try {
                // Insert the content values via a ContentResolver
                Uri uri = getContentResolver().insert(GoBagContract.GoBagEntery.CONTENT_ITEM_URI, contentValues);
                if (uri != null) {
                    mSpinner.setSelection(0);
                    mButton.setVisibility(View.GONE);
                    //restarts the loader to get updated data
                    restartLoader();
                }
            } catch (Exception e) {
            }
        }else{
        }
    }

    /**
     * Restarts loader to update recycler view
     */
    private void restartLoader(){
        getLoaderManager().restartLoader(ITEM_LOADER_ID, null, this);
    }

    private double getWeightOfItem(String item){
        try{
            JSONObject countryRoot = new JSONObject(Utils.readJsonFile(getApplicationContext(), R.raw.gobag));
            JSONArray jsonArray = countryRoot.getJSONArray("items");
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject itemsName = jsonArray.getJSONObject(i);
                if(item.compareTo(itemsName.getString("name")) == 0){
                    return itemsName.getDouble("weight");
                }
            }
        }catch (Exception e){}

        return 0;
    }

    public void loadSpinner(){
        try{
            JSONObject countryRoot = new JSONObject(Utils.readJsonFile(getApplicationContext(), R.raw.gobag));
            JSONArray jsonArray = countryRoot.getJSONArray("items");
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject itemsName = jsonArray.getJSONObject(i);
                String item = itemsName.getString("name");
                mGoBagArray.add(item);
            }

            mSpinner.setAdapter(new ArrayAdapter<String>
                    (GoBagListActivity.this, android.R.layout.simple_spinner_dropdown_item, mGoBagArray));


        }catch (JSONException e){} catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete all items relating to the go bag
     * @param param time stamp of go bag
     */
    private void deleteGoBag(long param){
        int i, j;

        String args [] = {param+""};

        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        //Delete all items from items_table relating the go bag
        i = db.delete(GoBagContract.GoBagEntery.ITEMS_TABLE_NAME,
                GoBagContract.GoBagEntery.COLUMN_GO_BAG_ID+ "= ?", args);

        //Delete goBag
        j = db.delete(GoBagContract.GoBagEntery.GO_BAG_TABLE_NAME,
                GoBagContract.GoBagEntery.COLUMN_TIME_STAMP+ " = ?", args);

        if(j > 0) finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] selectionArgs = {mTimeCreated+""};

        //Projection for query
        String mProjection = GoBagContract.GoBagEntery.COLUMN_ITEM+", "
                +"SUM("+  GoBagContract.GoBagEntery.COLUMN_WEIGHT+"), "
                +"COUNT("+  GoBagContract.GoBagEntery.COLUMN_ITEM+"), "
                + GoBagContract.GoBagEntery.COLUMN_TIME_STAMP+
                "";

        String rawQuery = "SELECT "+ mProjection +
                " FROM " + GoBagContract.GoBagEntery.ITEMS_TABLE_NAME
                +" WHERE "+  GoBagContract.GoBagEntery.COLUMN_GO_BAG_ID+"  = ? "
                +" GROUP BY 1 "
                +" ORDER BY "+ GoBagContract.GoBagEntery.COLUMN_TIME_STAMP+" DESC "
                +"" ;

        return new GoBagLoader(this, null, rawQuery, selectionArgs);
    }

    /**
     * Loop through cursor
     * Calculate total number of items in go bad, and weight of go bag
     */
    private void setGoBagProperties(){
        double weight = 0;
        int quantity = 0;

        //loop through cursor
        for(int i = 0; i < mCursor.getCount(); i++){
            //move cursor to next position
            mCursor.moveToPosition(i);

            weight += mCursor.getDouble(1);
            quantity += mCursor.getInt(2);

            //Set values of result
            mNumberOfItemsTextView.setText(getString(R.string.numbers_of_items)+":  "+quantity);
            mWeightOfBagTextView.setText(getString(R.string.weight_of_go_bag)+":  "+weight+"kg");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mAdapter.swapCursor(mCursor);
        setGoBagProperties();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onLongClick(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.go_bag_list_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_delete:
                DialogFragment deleteDialog = new ConfirmDeleteDialog();
                deleteDialog.show(getFragmentManager(), "deletePassenger");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Dialog class
     * dialog to confirm delete action
     */
    public static class ConfirmDeleteDialog extends DialogFragment {
        GoBagListActivity activity;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Get Instance of Manifest Activity
            activity = (GoBagListActivity) getActivity();
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_delete_of_go_bag)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // delete passenger
                            activity.deleteGoBag(mTimeCreated);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public void onStart() {
            super.onStart();
            ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        }

        @Override
        public void onStop() {
            dismiss();
            super.onStop();
        }
    }
}
