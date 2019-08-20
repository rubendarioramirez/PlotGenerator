package com.plotgen.rramirez.plotgenerator.Common;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

public class SQLUtils extends Fragment {


    //This function to get Outline by ID.
    public static ArrayList<String> getOutlineByID(Context context, String project_id){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_OUTLINE  + " WHERE project_id = ?";
        ArrayList<String> projects_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_id});
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String outID = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.OUTLINE_COLUMN_ID));
                String outTitle = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.OUTLINE_COLUMN_NAME));
                String outDescription = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.OUTLINE_COLUMN_DESCRIPTION));
                String position = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.OUTLINE_COLUMN_POSITION));
                String characters = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.OUTLINE_COLUMN_CHARACTERS));
                projects_list.add(outID + "/&&/" + outTitle + "/&&/" + outDescription + "/&&/" + characters + "/&&/" + position);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", "Project ID is null" + e.toString());
        }
        sqLiteDatabase.close();
        return projects_list;
    }

    //This function to get projects by ID onces everybody saved the id.
    public static ArrayList<String> getCharNamesByID(Context context, String project_id){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE project_id = ?";
        ArrayList<String> projects_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_id});
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String charName = cursor.getString(cursor.getColumnIndex("name"));
                projects_list.add(charName);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", "Project ID is null" + e.toString());
        }
        sqLiteDatabase.close();
        return projects_list;
    }


}
