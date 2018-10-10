package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {

    public static void saveOnSharePreg(Context context, String variable, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(variable, value);
        editor.apply();
    }


    public static int getSharePref(Context context, String variable, int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int result = preferences.getInt(variable,value);
        return result;
    }

}
