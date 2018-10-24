package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

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

    public static ArrayList<String> getProject(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_PROJECT  + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_name});
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            projects_list.add(cursor.getString(cursor.getColumnIndex("project")));
            projects_list.add(cursor.getString(cursor.getColumnIndex("genre")));
            projects_list.add(cursor.getString(cursor.getColumnIndex("plot")));
            cursor.moveToNext();
        }
        cursor.close();
        return projects_list;
    }

    public static ArrayList<String> getProjects_list(Context context){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            projects_list.add(cursor.getString(cursor.getColumnIndex("project")));
            cursor.moveToNext();
        }
        cursor.close();
        return projects_list;
    }

    public static ArrayList<String> getCharList(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_name});
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            String charname = cursor.getString(cursor.getColumnIndex("name"));
            String charRole = cursor.getString(cursor.getColumnIndex("role"));
            projects_list.add(charname + " - " + charRole);
            cursor.moveToNext();
        }
        cursor.close();
        return projects_list;
    }

    public static void deleteFromDB(Context context, String project_name) {
        SQLiteDatabase database = new mySQLiteDBHelper(context).getWritableDatabase();
        database.delete(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT,  "project = ?", new String[]{project_name});
    }


    public static void loadAd(AdView mAdView){
        //Display the ad
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                .build();
        mAdView.loadAd(adRequest);
    }


    public static void changeFragment(Fragment nextFragment,FragmentTransaction transaction, String bundleName, String bundleValue){
        Bundle bundle = new Bundle();
        bundle.putString(bundleName,bundleValue);
        nextFragment.setArguments(bundle);
        //Make the transaction
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain,nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
