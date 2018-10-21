package com.example.anga_.canopener;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anga_.canopener.data.GoBagContract;
import com.example.anga_.canopener.loaders.GoBagLoader;
import com.example.anga_.canopener.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class NewGoBagActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER_ID = 0;

    Button mButton;
    RecyclerView mRecyclerView;
    Spinner mSpinner;

    TextView mWeightOfItemTextView, mWeightOfBagTextView;
    EditText mGoBagNameEditText;

    ArrayList<String> mGoBagArray;
    double mWeightOfItem = 0;
    double mWeightOfBag = 0;

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

    private void addItem(){

        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_ITEM, mSpinner.getSelectedItem().toString());
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_NAME, mGoBagNameEditText.getText().toString());
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_WEIGHT, mWeightOfItem);
        contentValues.put(GoBagContract.GoBagEntery.COLUMN_TIME_STAMP, ts);

        if(!mGoBagNameEditText.getText().toString().trim().isEmpty()) {
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
            try {
                // Insert the content values via a ContentResolver
                Uri uri = getContentResolver().insert(GoBagContract.GoBagEntery.CONTENT_GOBAG_URI, contentValues);
                if (uri != null) {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    //finish();
                }
            } catch (Exception e) {
            }
        }else{
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
    }

    private void getGoBags(String goBag){

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
                    (NewGoBagActivity.this, android.R.layout.simple_spinner_dropdown_item, mGoBagArray));


        }catch (JSONException e){} catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String rawQuery = "SELECT *"+
                " FROM " + GoBagContract.GoBagEntery.GO_BAG_TABLE_NAME
                +" WHERE "+  GoBagContract.GoBagEntery.COLUMN_NAME+"  = ? "
                +" ORDER BY "+ GoBagContract.GoBagEntery.COLUMN_TIME_STAMP+" DESC "
                +"" ;

        return new GoBagLoader(this, null, rawQuery, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Toast.makeText(getApplicationContext(), data.getCount()+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
