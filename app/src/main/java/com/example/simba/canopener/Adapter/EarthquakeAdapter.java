package com.example.simba.canopener.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simba.canopener.R;
import com.example.simba.canopener.models.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {

    private static final String LOCATION_SEPARATOR = " of ";

    ArrayList<Earthquake> mEarthquakes = new ArrayList<>();
    Context mContext;

    public EarthquakeAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.earthquake_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new EarthquakeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.onBindView(i);
    }

    @Override
    public int getItemCount() {
        if(mEarthquakes == null)
            return 0;
        else return mEarthquakes.size();
    }

    public void addData(Earthquake earthquake){
        mEarthquakes.add(earthquake);
    }

    public void setData(ArrayList<Earthquake> manifestData){
        mEarthquakes = manifestData;
        notifyDataSetChanged();
    }

    public Earthquake getEarthquake(int position){
        return mEarthquakes.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView magnitudeTextView, locationOffSetTextView, primaryLocationTextView, dateTextView, timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            magnitudeTextView = itemView.findViewById(R.id.magnitude);
            locationOffSetTextView = itemView.findViewById(R.id.location_offset);
            primaryLocationTextView = itemView.findViewById(R.id.primary_location);
            dateTextView = itemView.findViewById(R.id.date);
            timeTextView = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void onBindView(int position){
            Earthquake earthquake = getEarthquake(position);
            setLocation(earthquake.getLocation());
            // Create a new Date object from the time in milliseconds of the earthquake
            Date dateObject = new Date(earthquake.getTimeInMilliseconds());
            timeTextView.setText(formatTime(dateObject));
            dateTextView.setText(formatDate(dateObject));
            magnitudeTextView.setText(formatMagnitude(earthquake.getMagnitude()));
            // Set the proper background color on the magnitude circle.
            // Fetch the background from the TextView, which is a GradientDrawable.
            GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);

        }

        private void setLocation(String location){
            // Get the original location string from the Earthquake object,
            // which can be in the format of "5km N of Cairo, Egypt" or "Pacific-Antarctic Ridge".
            String originalLocation = location;

            // If the original location string (i.e. "5km N of Cairo, Egypt") contains
            // a primary location (Cairo, Egypt) and a location offset (5km N of that city)
            // then store the primary location separately from the location offset in 2 Strings,
            // so they can be displayed in 2 TextViews.
            String primaryLocation;
            String locationOffset;

            // Check whether the originalLocation string contains the " of " text
            if (originalLocation.contains(LOCATION_SEPARATOR)) {
                // Split the string into different parts (as an array of Strings)
                // based on the " of " text. We expect an array of 2 Strings, where
                // the first String will be "5km N" and the second String will be "Cairo, Egypt".
                String[] parts = originalLocation.split(LOCATION_SEPARATOR);
                // Location offset should be "5km N " + " of " --> "5km N of"
                locationOffset = parts[0] + LOCATION_SEPARATOR;
                // Primary location should be "Cairo, Egypt"
                primaryLocation = parts[1];

                primaryLocationTextView.setText(primaryLocation);
                locationOffSetTextView.setText(locationOffset);
            } else {
                // Otherwise, there is no " of " text in the originalLocation string.
                // Hence, set the default location offset to say "Near the".
                locationOffset = mContext.getString(R.string.near_the);
                // The primary location will be the full location string "Pacific-Antarctic Ridge".
                primaryLocation = originalLocation;

                primaryLocationTextView.setText(primaryLocation);
                locationOffSetTextView.setText(locationOffset);
            }
        }

        private int getMagnitudeColor(double magnitude) {
            int magnitudeColorResourceId;
            int magnitudeFloor = (int) Math.floor(magnitude);
            switch (magnitudeFloor) {
                case 0:
                case 1:
                    magnitudeColorResourceId = R.color.magnitude1;
                    break;
                case 2:
                    magnitudeColorResourceId = R.color.magnitude2;
                    break;
                case 3:
                    magnitudeColorResourceId = R.color.magnitude3;
                    break;
                case 4:
                    magnitudeColorResourceId = R.color.magnitude4;
                    break;
                case 5:
                    magnitudeColorResourceId = R.color.magnitude5;
                    break;
                case 6:
                    magnitudeColorResourceId = R.color.magnitude6;
                    break;
                case 7:
                    magnitudeColorResourceId = R.color.magnitude7;
                    break;
                case 8:
                    magnitudeColorResourceId = R.color.magnitude8;
                    break;
                case 9:
                    magnitudeColorResourceId = R.color.magnitude9;
                    break;
                default:
                    magnitudeColorResourceId = R.color.magnitude10plus;
                    break;
            }

            return ContextCompat.getColor(mContext, magnitudeColorResourceId);
        }

        /**
         * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
         * from a decimal magnitude value.
         */
        private String formatMagnitude(double magnitude) {
            DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
            return magnitudeFormat.format(magnitude);
        }

        /**
         * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
         */
        private String formatDate(Date dateObject) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
            return dateFormat.format(dateObject);
        }

        /**
         * Return the formatted date string (i.e. "4:30 PM") from a Date object.
         */
        private String formatTime(Date dateObject) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            return timeFormat.format(dateObject);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
