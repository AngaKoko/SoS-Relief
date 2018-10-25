package com.example.simba.canopener.fragments.gobag;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simba.canopener.Adapter.GoBagAdapter;
import com.example.simba.canopener.GoBagListActivity;
import com.example.simba.canopener.R;
import com.example.simba.canopener.data.GoBagContract;
import com.example.simba.canopener.data.TaskContract;
import com.example.simba.canopener.dialog.NewGoBagDialogActivity;
import com.example.simba.canopener.loaders.FragmentsCursorLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoBagFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
        , GoBagAdapter.GoBagAdapterOnclickHandler, GoBagAdapter.GoBagAdapterOnLongClickLHandler{

    //loader id
    private static final int ITEM_LOADER_ID = 0;

    RecyclerView mRecyclerView;
    GoBagAdapter mAdapter;
    Cursor mCursor;

    FloatingActionButton mFab;
    TextView mEmptyStateTextView;


    public GoBagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_go_bag, container, false);

        mFab = view.findViewById(R.id.fab);
        mEmptyStateTextView = view.findViewById(R.id.empty_state_text_view);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new GoBagAdapter(getContext(), mCursor ,this, this);
        mRecyclerView.setAdapter(mAdapter);

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getLoaderManager().initLoader(ITEM_LOADER_ID, null, this);

        //onClick action to start NewGoBagDialog
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBagDialog();
            }
        });

        return view;
    }

    /**
     * Starts NewGoBagDialog to add a new go bag
     */
    private void goBagDialog(){
        Intent intent = new Intent(getContext(), NewGoBagDialogActivity.class);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        //Projection for query
        String mProjection = "g."+ GoBagContract.GoBagEntery.COLUMN_NAME+", "
                +"g."+ GoBagContract.GoBagEntery.COLUMN_TIME_STAMP+", "
                +"SUM(i."+ GoBagContract.GoBagEntery.COLUMN_WEIGHT+"), "
                +"COUNT(i."+ GoBagContract.GoBagEntery.COLUMN_ITEM+") as quantity " +
                "";

        //raw query to get list of all go bags in our database
        String rawQuery = "SELECT " + mProjection +
                "FROM " + GoBagContract.GoBagEntery.GO_BAG_TABLE_NAME + " g " +
                "LEFT JOIN " + GoBagContract.GoBagEntery.ITEMS_TABLE_NAME +" i "
                + "ON " + "g."+ GoBagContract.GoBagEntery.COLUMN_TIME_STAMP + " = "+ "i."+ GoBagContract.GoBagEntery.COLUMN_GO_BAG_ID +
                " GROUP BY 1 " +
                " ORDER BY 2 DESC";

        return new FragmentsCursorLoader(getContext(), null, rawQuery, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        mCursor = cursor;
        mAdapter.swapCursor(mCursor);
        //If result is empty show empty state text view
        if(mCursor.getCount() < 1) mEmptyStateTextView.setVisibility(View.VISIBLE);
        //if result is not empty hide empty sate text view
        else mEmptyStateTextView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursor = null;
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int position) {
        //Move cursor to position
        mCursor.moveToPosition(position);
        //get name of selected go bag
        String goBagName = mCursor.getString(0);
        long timeCreated = mCursor.getLong(1);

        //Start GoBagListActivity with go bag name and time created
        Intent intent = new Intent(getContext(), GoBagListActivity.class);
        intent.putExtra(TaskContract.GO_BAG_NAME, goBagName);
        intent.putExtra(TaskContract.GO_BAG_TIME_STAMP, timeCreated);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {

    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(ITEM_LOADER_ID, null, this);
    }
}
