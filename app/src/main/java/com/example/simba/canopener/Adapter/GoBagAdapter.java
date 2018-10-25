package com.example.simba.canopener.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simba.canopener.R;
import com.example.simba.canopener.utils.MyDateUtils;

import java.util.Date;

public class GoBagAdapter extends RecyclerView.Adapter<GoBagAdapter.ViewHolder>{

    private Cursor mCursor;
    private Context mContext;

    private final GoBagAdapterOnclickHandler mOnclickHandler;
    private final GoBagAdapterOnLongClickLHandler mOnLongClickLHandler;

    public GoBagAdapter(Context context, Cursor cursor
            , GoBagAdapterOnLongClickLHandler onLongClickLHandler, GoBagAdapterOnclickHandler onclickHandler){
        mCursor = cursor;
        mContext = context;
        mOnLongClickLHandler = onLongClickLHandler;
        mOnclickHandler = onclickHandler;
    }

    //Interface that receives onClick messages;
    public interface GoBagAdapterOnclickHandler{
        void onClick(int position);
    }

    //Interface that receives onLongClick messages
    public interface GoBagAdapterOnLongClickLHandler {
        void onLongClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.list_go_bag;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(i);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor cursor) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == cursor) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = cursor; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    /**
     * View holder class to
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView mGoBagNameTextView, mTimeTextView, mWeightTextView, mQuantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mGoBagNameTextView = itemView.findViewById(R.id.go_bag_name_text_view);
            mTimeTextView = itemView.findViewById(R.id.time_created_text_view);
            mWeightTextView = itemView.findViewById(R.id.weight_text_veiw);
            mQuantityTextView = itemView.findViewById(R.id.quantity_of_items_text_view);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bindView(int position){
            //Move cursor to current position
            mCursor.moveToPosition(position);

            //Determine values of wanted data
            String goBagName = mCursor.getString(0);
            long timeCreated = mCursor.getLong(1);
            double goBagWeight = mCursor.getDouble(2);
            int itemCount = mCursor.getInt(3);

            // Create a new Date object from the time in milliseconds of the earthquake
            Date dateObject = new Date(timeCreated);
            //Get the date in date time format
            String date = MyDateUtils.formatDate(dateObject)+" "+MyDateUtils.formatTime(dateObject);

            String weight = goBagWeight+" kg";
            String items;
            if(itemCount < 2)items = itemCount+" "+mContext.getString(R.string.item);
            else items = itemCount+" "+mContext.getString(R.string.items);

            //Bind data gotten from cursor to views
            mGoBagNameTextView.setText(goBagName);
            mTimeTextView.setText(date);
            mWeightTextView.setText(weight);
            mQuantityTextView.setText(items);
        }

        @Override
        public void onClick(View v) {
            //Get position of Adapter
            int position = getAdapterPosition();
            //Handle the click
            mOnclickHandler.onClick(position);
        }

        @Override
        public boolean onLongClick(View v) {
            //Get position of Adapter
            int position = getAdapterPosition();
            //Handle the click
            mOnLongClickLHandler.onLongClick(position);

            return true;
        }
    }

}
