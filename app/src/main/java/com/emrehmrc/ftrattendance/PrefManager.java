package com.emrehmrc.ftrattendance;

import android.content.Context;
import android.content.SharedPreferences;

import com.emrehmrc.ftrattendance.model.Detail;

import java.util.ArrayList;

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "Welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String MEMBER_ID = "MemberId";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setMemberId(String id) {
        editor.putString(MEMBER_ID, id);
        editor.commit();
    }

    public String MemberId() {
        return pref.getString(MEMBER_ID, "");
    }




}
