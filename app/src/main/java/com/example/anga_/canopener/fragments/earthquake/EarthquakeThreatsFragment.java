package com.example.anga_.canopener.fragments.earthquake;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anga_.canopener.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakeThreatsFragment extends Fragment {


    public EarthquakeThreatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earthquake_threats, container, false);
    }

}
