package com.example.anga_.canopener.fragments.earthquake;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.anga_.canopener.Adapter.EarthquakeAdapter;
import com.example.anga_.canopener.R;
import com.example.anga_.canopener.helpers.MySingleton;
import com.example.anga_.canopener.models.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakeDataFragment extends Fragment {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    ProgressBar mProgressBar;
    EarthquakeAdapter mAdapter;
    RecyclerView mRecyclerView;

    public EarthquakeDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earthquake_data, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mAdapter = new EarthquakeAdapter(getContext());

        mRecyclerView = view.findViewById(R.id.recycler_view);
        //Creating a Layout for our RecycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //Assigning the layout to our RecycleView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        getEarthquakeData();

        return view;
    }

    private String getUrlString(String url){
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "25");
        uriBuilder.appendQueryParameter("minmag", "1");
        uriBuilder.appendQueryParameter("orderby", "time");

        return uriBuilder.toString();
    }

    private void getEarthquakeData(){
        mProgressBar.setVisibility(View.VISIBLE);
        String url = getUrlString(USGS_REQUEST_URL);
        final ArrayList<Earthquake> earthquakes = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            JSONArray earthquakeArray = response.getJSONArray("features");

                            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
                            for (int i = 0; i < earthquakeArray.length(); i++) {

                                // Get a single earthquake at position i within the list of earthquakes
                                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                                // For a given earthquake, extract the JSONObject associated with the
                                // key called "properties", which represents a list of all properties
                                // for that earthquake.
                                JSONObject properties = currentEarthquake.getJSONObject("properties");

                                // Extract the value for the key called "mag"
                                double magnitude = properties.getDouble("mag");

                                // Extract the value for the key called "place"
                                String location = properties.getString("place");

                                // Extract the value for the key called "time"
                                long time = properties.getLong("time");

                                // Extract the value for the key called "url"
                                String url = properties.getString("url");

                                // Create a new {@link Earthquake} object with the magnitude, location, time,
                                // and url from the JSON response.
                                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                                earthquakes.add(earthquake);

                                // Add the new {@link Earthquake} to the list of earthquakes.
                                mAdapter.setData(earthquakes);
                                //Toast.makeText(getContext().getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getContext().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                    }
                });


        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

}
