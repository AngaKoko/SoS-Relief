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

public class GoBagItemsAdapter extends RecyclerView.Adapter<GoBagItemsAdapter.ViewHolder>{

    private Cursor mCursor;
    private Context mContext;

    private final GoBagItemsAdapterOnclickHandler mOnclickHandler;
    private final GoBagItemsAdapterOnLongClickLHandler mOnLongClickLHandler;

    public GoBagItemsAdapter(Context context, Cursor cursor,
                             GoBagItemsAdapterOnclickHandler onclickHandler,
                             GoBagItemsAdapterOnLongClickLHandler onLongClickLHandler){
        mContext = context;
        mCursor = cursor;
        mOnclickHandler = onclickHandler;
        mOnLongClickLHandler = onLongClickLHandler;
    }

    //Interface that receives onClick messages;
    public interface GoBagItemsAdapterOnclickHandler{
        void onClick(int position);
    }

    //Interface that receives onLongClick messages
    public interface GoBagItemsAdapterOnLongClickLHandler {
        void onLongClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GoBagItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.onBindView(i);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView mItemTextView, mWeightTextView, mQuantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemTextView = itemView.findViewById(R.id.item_text_view);
            mWeightTextView = itemView.findViewById(R.id.weight_text_view);
            mQuantityTextView = itemView.findViewById(R.id.quantity_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void onBindView(int position){
            mCursor.moveToPosition(position);

            String item = mCursor.getString(0);
            double weight = mCursor.getDouble(1);
            String quantity = mCursor.getString(2);

            String itemWeight = weight+" kg";

            mItemTextView.setText(item);
            mWeightTextView.setText(itemWeight);
            mQuantityTextView.setText(quantity);
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
