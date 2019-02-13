package com.plotgen.rramirez.plotgenerator.Common;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.R;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Utils extends Fragment{

    public static final String SP_HAS_BUY_IAP = "spHasBuyIap";

    public static void saveOnSharePreg(Context context, String variable, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(variable, value);
        editor.apply();
    }

    public static void saveOnSharePreg(Context context, String variable, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(variable, value);
        editor.apply();
    }

    public static void setSPIAP(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SP_HAS_BUY_IAP, value);
        editor.apply();
    }

    public static boolean getSPIAP(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SP_HAS_BUY_IAP, false);
    }


    public static String getStringSharePref(Context context, String variable) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String result = preferences.getString(variable, "");
        return result;
    }

    public static int getSharePref(Context context, String variable, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int result = preferences.getInt(variable, value);
        return result;
    }

    public static ArrayList<String> getProject(Context context, String project_name) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_PROJECT + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_name});
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            projects_list.add(cursor.getString(cursor.getColumnIndex("project")));
            projects_list.add(cursor.getString(cursor.getColumnIndex("genre")));
            projects_list.add(cursor.getString(cursor.getColumnIndex("plot")));
            projects_list.add(cursor.getString(cursor.getColumnIndex("_id")));
            if (cursor.getColumnIndex("image") != -1 && cursor.getString(cursor.getColumnIndex("image")) != null)
                projects_list.add(cursor.getString(cursor.getColumnIndex("image")));

            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return projects_list;
    }

    public static ArrayList<String> getProjects_list(Context context) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String project_name = cursor.getString(cursor.getColumnIndex("project"));
            /*if (cursor.getColumnIndex("image") != -1 && cursor.getString(cursor.getColumnIndex("image")) != null) {
                String image = cursor.getString(cursor.getColumnIndex("image"));
            }*/
            projects_list.add(id + "/&&/" + project_name);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return projects_list;
    }

    //This function to get projects by ID onces everybody saved the id.
    public static ArrayList<String> getCharListByID(Context context, String project_id){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE project_id = ?";
        ArrayList<String> projects_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_id});
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String charID = cursor.getString(cursor.getColumnIndex("_id"));
                String charName = cursor.getString(cursor.getColumnIndex("name"));
                String charRole = cursor.getString(cursor.getColumnIndex("role"));
                String charGender = cursor.getString(cursor.getColumnIndex("gender"));
                Integer challengesDone = cursor.getInt(cursor.getColumnIndex("challengesCompleted"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                projects_list.add(charID + "/&&/" + charName + "/&&/" + charRole + "/&&/" + charGender + "/&&/" +challengesDone +  "/&&/" + image);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", "Project ID is null" + e.toString());
        }
        sqLiteDatabase.close();
        return projects_list;
    }

    public static ArrayList<String> getCharList(Context context, String project_name) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_name});
        ArrayList<String> char_list = new ArrayList<String>();
        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String charname = cursor.getString(cursor.getColumnIndex("name"));
                String charRole = cursor.getString(cursor.getColumnIndex("role"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                char_list.add(charname + " - " + charRole + " - " + image);
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();

        return char_list;
    }

    public static Boolean isHaveStoryFromDB(Context context, String project_id) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_STORY + " WHERE project_id = ?";
        ArrayList<String> story_list = new ArrayList<String>();
        String s = "";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_id});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                story_list.add(cursor.getString(cursor.getColumnIndex("project")));
                story_list.add(cursor.getString(cursor.getColumnIndex("project_id")));
                story_list.add(cursor.getString(cursor.getColumnIndex("stories")));
                s = cursor.getString(cursor.getColumnIndex("stories"));
                cursor.moveToNext();
            }
            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e){
            Log.v("matilda", "IshaveStory got n exception " + e.toString());
        }
        if (s != "")
            return true;
        else
            return false;
    }


    public static void deleteFromDB(Context context, String projectID) {
        SQLiteDatabase database = new mySQLiteDBHelper(context).getWritableDatabase();
        database.delete(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, "_id = ?", new String[]{projectID});
        database.close();
    }


    public static void loadAd(AdView mAdView) {
        if (!Common.isPAU) {
            //Display the ad
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                    .build();
            mAdView.loadAd(adRequest);
        }
    }


    public static void changeFragment(Fragment nextFragment, FragmentTransaction transaction) {
        //Make the transaction
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }



//    public static void changeFragment(Fragment nextFragment, FragmentTransaction transaction, String bundleName, String bundleValue) {
//        Bundle bundle = new Bundle();
//        bundle.putString(bundleName, bundleValue);
//        nextFragment.setArguments(bundle);
//        //Make the transaction
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
//        transaction.replace(R.id.flMain, nextFragment);
//        transaction.commit();
//    }

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
                .setNegativeButton(context.getString(R.string.rate_cancel), null);
        builder.show();
    }


    public static void displayDialog(Context context, String title, String body, String positiveBTN){
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton(positiveBTN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Got it!");
                    }
                })
                .show();
    }


    public static void showComingSoonPopup(final Context context) {

        String[] themes = context.getResources().getStringArray(R.array.themes);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Choose your theme")
                .setMessage(context.getString(R.string.iap_desc))
                .setNeutralButton(context.getString(R.string.iap_btn), null);
        builder.show();
    }


    //get image file path
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, uri, selection, selectionArgs);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
                Log.e("file path", getDataColumn(context, uri, selection, selectionArgs));
                return getDataColumn(context, uri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri)) {
                Log.e("file path", uri.getLastPathSegment());
                return uri.toString();
            }

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    Log.e("file path", cursor.getString(column_index));
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e("file path", uri.getPath());

            return uri.getPath();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, null, selection, selectionArgs,
                    null);
            if (cursor != null)
                if (cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow("_data");
                    return cursor.getString(index);
                }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static void populateChallenges(){
        //TODO Move the function to populate BIO here
    }

}
