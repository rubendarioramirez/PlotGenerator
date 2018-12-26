package com.plotgen.rramirez.plotgenerator;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

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
        sqLiteDatabase.close();
        return projects_list;
    }

    public static ArrayList<String> getProjects_list(Context context){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String project_name = cursor.getString(cursor.getColumnIndex("project"));
            projects_list.add(id + "_" + project_name);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return projects_list;
    }

//    This function to get projects by ID onces everybody saved the id.
//    public static ArrayList<String> getCharList(Context context, String project_id){
//        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
//        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
//        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE project_id = ?";
//        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_id});
//        cursor.moveToFirst();
//        ArrayList<String> projects_list = new ArrayList<String>();
//        while(!cursor.isAfterLast()) {
//            String charname = cursor.getString(cursor.getColumnIndex("name"));
//            String charRole = cursor.getString(cursor.getColumnIndex("role"));
//            projects_list.add(charname + " - " + charRole);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        sqLiteDatabase.close();
//        return projects_list;
//    }

    public static ArrayList<String> getCharList(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_name});
        cursor.moveToFirst();
        ArrayList<String> char_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            String charname = cursor.getString(cursor.getColumnIndex("name"));
            String charRole = cursor.getString(cursor.getColumnIndex("role"));
            char_list.add(charname + " - " + charRole);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return char_list;
    }

    public static Boolean isHaveStoryFromDB(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_STORY  + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_name});
        cursor.moveToFirst();
        String s = "";
        ArrayList<String> story_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            story_list.add(cursor.getString(cursor.getColumnIndex("project")));
            story_list.add(cursor.getString(cursor.getColumnIndex("project_id")));
            story_list.add(cursor.getString(cursor.getColumnIndex("stories")));

            s = cursor.getString(cursor.getColumnIndex("stories"));
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        if(s != "")
            return true;
        else
            return false;
    }


    public static void deleteFromDB(Context context, String project_name) {
        SQLiteDatabase database = new mySQLiteDBHelper(context).getWritableDatabase();
        database.delete(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT,  "project = ?", new String[]{project_name});
        database.close();
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

    public static void showRateDialogForRate(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.rate_app))
                .setMessage(context.getString(R.string.rate_app_des))
                .setPositiveButton(context.getString(R.string.rate_submit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            ////////////////////////////////
                            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            // To count with Play market backstack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("rated", 1);
                            editor.apply();
                            try {
                                context.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            }


                        }
                    }
                })
                .setNegativeButton( context.getString(R.string.rate_cancel), null);
        builder.show();
    }

    public static void showComingSoonPopup(final Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("It's Coming Soon!")
                .setMessage("Pro version with no Ads will be available on next update :)")
                .setNeutralButton("Yess, I'll wait!", null);
        builder.show();
    }

}
