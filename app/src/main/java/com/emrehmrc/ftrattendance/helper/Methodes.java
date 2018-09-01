package com.emrehmrc.ftrattendance.helper;

import android.content.ContentProvider;
import android.content.Context;
import android.content.res.Configuration;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.Locale;

public class Methodes {

    public static SpannableString ColorString(String out, Integer state) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String string=out;
        SpannableString string1 = new SpannableString(string);
        string1.setSpan(new ForegroundColorSpan(state), 0, string.length(), 0);
        builder.append(string1);
        return string1;
    }
    public static void setLangCalendar(Context m) {
        String languageToLoad  = "tr"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
       m.getResources().updateConfiguration(config,
                m.getResources().getDisplayMetrics());
    }

}
