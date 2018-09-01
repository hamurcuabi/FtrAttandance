package com.emrehmrc.ftrattendance.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emrehmrc.ftrattendance.R;
import com.emrehmrc.ftrattendance.helper.Methodes;
import com.emrehmrc.ftrattendance.model.Detail;

import java.util.ArrayList;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.MyviewHolder> implements Filterable {

    private static final String TAG = "DetailRecyclerAdapter";
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    public ArrayList<Detail> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;
    DetailFilterAdapter filter;


    public DetailRecyclerAdapter(Context context, ArrayList<Detail> data) {
        Log.d(TAG, "DetailRecyclerAdapter: ");
        layoutInflater = LayoutInflater.from(context);
        this.datalist = data;
        this.mContentxt = context;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: ");
        switch (position % 2) {
            case 0:
                return TYPE_ONE;
            case 1:
                return TYPE_TWO;
            default:
                return TYPE_ONE;
        }
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = null;
        switch (viewType) {
            case TYPE_ONE:
                view = LayoutInflater
                        .from(mContentxt)
                        .inflate(R.layout.detail_item_recycler, parent, false);
                return new MyviewHolder(view);
            case TYPE_TWO:
                view = LayoutInflater
                        .from(mContentxt)
                        .inflate(R.layout.detail_item_recycler_second, parent, false);
                return new MyviewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Detail clicked = datalist.get(position);
        holder.setData(clicked, position);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new DetailFilterAdapter(this, datalist);
        }
        return filter;
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView txtState;

        public MyviewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtState = itemView.findViewById(R.id.txtState);
        }

        @SuppressLint("NewApi")
        public void setData(final Detail clicked, int position) {
            Log.d(TAG, "setData");
            String state = "";
            int color = 1;
            this.txtDate.setText(clicked.getDate());
            if (clicked.getState() == 1) {
                state = "GELDİ";
                color = mContentxt.getResources().getColor(R.color.colorPrimaryDark);
            } else if (clicked.getState() == 2) {
                state = "GELMEDİ";
                color = Color.RED;
            } else if (clicked.getState() == 3) {
                state = "TELAFİ";
                color = Color.BLUE;
            }
            this.txtState.setText(Methodes.ColorString(state, color));

        }
    }
}
