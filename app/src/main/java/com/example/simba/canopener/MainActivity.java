package com.example.simba.canopener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mEarthquakeTextView, mVolcanoTextView, mFloodTextView, mWildfireTextView, mGoBagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEarthquakeTextView = findViewById(R.id.earth_quake_text_view);
        mVolcanoTextView = findViewById(R.id.volcano_text_view);
        mFloodTextView = findViewById(R.id.flood_text_view);
        mWildfireTextView = findViewById(R.id.wildfire_text_view);
        mGoBagButton = findViewById(R.id.go_bag_button);

        mEarthquakeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEarthQuakeActivity();
            }
        });
        mVolcanoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVolcanoActivity();
            }
        });
        mFloodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloodActivity();
            }
        });
        mWildfireTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWildfireActivity();
            }
        });
        mGoBagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyGoBagActivity();
            }
        });
    }

    private void startEarthQuakeActivity(){
        Intent intent = new Intent(this, EarthquakeActivity.class);
        startActivity(intent);
    }

    private void startVolcanoActivity(){
        Intent intent = new Intent(this, VolcanoActivity.class);
        startActivity(intent);
    }

    private void startFloodActivity(){
        Intent intent = new Intent(this, FloodingActivity.class);
        startActivity(intent);
    }

    private void startWildfireActivity(){
        Intent intent = new Intent(this, WildfireActivity.class);
        startActivity(intent);
    }

    private void startMyGoBagActivity(){
        Intent intent = new Intent(this, GoBagActivity.class);
        startActivity(intent);
    }
}
