package com.example.simba.canopener.dialog;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simba.canopener.R;
import com.example.simba.canopener.data.GoBagContract;

public class NewGoBagDialogActivity extends AppCompatActivity {

    EditText mGoBagNameEditText;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_go_bag_dialog);

        mGoBagNameEditText = findViewById(R.id.go_bag_name_edit_text);
        mButton = findViewById(R.id.submit_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mGoBagNameEditText.getText().toString().trim().isEmpty())
                    submitGoBag(mGoBagNameEditText.getText().toString());
            }
        });
    }

    private void submitGoBag(String name){
        //Get time stamp in milli seconds
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        ContentValues values = new ContentValues();
        values.put(GoBagContract.GoBagEntery.COLUMN_NAME, name);
        values.put(GoBagContract.GoBagEntery.COLUMN_TIME_STAMP, ts);

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(GoBagContract.GoBagEntery.CONTENT_GOBAG_URI, values);
        if (uri != null) {
            //Toast.makeText(getBaseContext(), mDate, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
