package com.example.simba.canopener.fragments.gobag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simba.canopener.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicSupplyKitFragment extends Fragment {


    public BasicSupplyKitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_supply_kit, container, false);
    }

}
