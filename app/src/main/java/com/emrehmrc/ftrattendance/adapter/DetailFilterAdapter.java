package com.emrehmrc.ftrattendance.adapter;

import android.util.Log;
import android.widget.Filter;

import com.emrehmrc.ftrattendance.model.Detail;
import com.emrehmrc.ftrattendance.model.Member;
import com.emrehmrc.ftrattendance.utils.SingletonCurrentValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailFilterAdapter extends Filter {
    private static final String TAG = "DetailFilterAdapter";
    DetailRecyclerAdapter adapter;
    ArrayList<Detail> filterList;


    public DetailFilterAdapter(DetailRecyclerAdapter adapter, ArrayList<Detail> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
           ArrayList<Detail> filtered = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {

                if(compareDate(filterList.get(i).getDate(),constraint))filtered.add(filterList.get(i));
            }

            results.count = filtered.size();
            results.values = filtered;
            SingletonCurrentValues.getInstance().setDetails(filtered);


        } else {
            results.count = filterList.size();
            results.values = filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        Log.d(TAG, "publishResults:");
        adapter.datalist = (ArrayList<Detail>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
    private boolean compareDate(String valid_until, CharSequence constraint){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM EEEE");
        Date strDate = null;
        Date strEndDay = null;
        Date strStartDay = null;
        Calendar c = Calendar.getInstance();

        boolean isValid=false;
        try {
            strDate = sdf.parse(valid_until);
            strEndDay = sdf.parse(constraint.toString());
            strStartDay = sdf.parse(constraint.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(strDate);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        strEndDay.setDate(c.getActualMaximum(Calendar.DAY_OF_MONTH));
        strStartDay.setDate(1);
        if (strEndDay.getTime() >= strDate.getTime() && strStartDay.getTime()<= strDate.getTime()) {
            isValid= true;
        }
        return  isValid;
    }

}