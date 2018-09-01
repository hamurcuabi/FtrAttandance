package com.emrehmrc.ftrattendance.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emrehmrc.ftrattendance.R;
import com.emrehmrc.ftrattendance.model.Member;

import java.util.ArrayList;

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.MyviewHolder> implements Filterable {

    private static final String TAG = "MemberRecyclerAdapter";
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    ArrayList<Member> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;
    MemberFilterAdapter filter;


    public MemberRecyclerAdapter(Context context, ArrayList<Member> data) {
        Log.d(TAG, "MemberRecyclerAdapter");
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
                        .inflate(R.layout.member_item_recycler, parent, false);
                return new MyviewHolder(view);
            case TYPE_TWO:
                view = LayoutInflater
                        .from(mContentxt)
                        .inflate(R.layout.member_item_recycler_second, parent, false);
                return new MyviewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Member clicked = datalist.get(position);
        holder.setData(clicked, position);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new MemberFilterAdapter(this, datalist);
        }
        return filter;
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView txtMember;
        public MyviewHolder(View itemView) {
            super(itemView);
            txtMember = itemView.findViewById(R.id.txtNameSurname);
        }
        @SuppressLint("NewApi")
        public void setData(final Member clicked, int position) {
            Log.d(TAG, "setData");
            this.txtMember.setText(clicked.getName());

        }
    }
}
