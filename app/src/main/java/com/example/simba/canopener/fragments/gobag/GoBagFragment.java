package com.example.simba.canopener.fragments.gobag;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simba.canopener.NewGoBagActivity;
import com.example.simba.canopener.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoBagFragment extends Fragment {

    FloatingActionButton mFab;


    public GoBagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_go_bag, container, false);

        mFab = view.findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGoBagActivity();
            }
        });

        return view;
    }

    private void newGoBagActivity(){
        Intent intent = new Intent(getContext(), NewGoBagActivity.class);
        startActivity(intent);
    }

}
