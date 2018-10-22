package com.plotgen.rramirez.plotgenerator;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

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
