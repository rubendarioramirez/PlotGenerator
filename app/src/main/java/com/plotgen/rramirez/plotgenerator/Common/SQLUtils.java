package com.plotgen.rramirez.plotgenerator.Common;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.fragment.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

public class SQLUtils extends Fragment {


    //region SQL Functions for Outline
    //This function to get Outline by ID.
    public static ArrayList<String> getOutlineByProjectID(Context context, String project_id){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_OUTLINE + " WHERE project_id = ?";
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
    //endregion

    //region Get project
    public static ArrayList<String> getProjectByID(Context context, String id) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_PROJECT + " WHERE " + mySQLiteDBHelper.PROJECT_COLUMN_ID + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{id});
        cursor.moveToFirst();
        ArrayList<String> projects = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            projects.add(cursor.getString(cursor.getColumnIndex("project")));
            projects.add(cursor.getString(cursor.getColumnIndex("genre")));
            projects.add(cursor.getString(cursor.getColumnIndex("plot")));
            projects.add(cursor.getString(cursor.getColumnIndex("_id")));
            if (cursor.getColumnIndex("image") != -1 && cursor.getString(cursor.getColumnIndex("image")) != null)
                projects.add(cursor.getString(cursor.getColumnIndex("image")));

            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return projects;
    }
    //endregion

    //This function to get Outline by ID.
    public static ArrayList<String> getOutlineByID(Context context, String outlineID){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_OUTLINE + " WHERE _id = ?";
        ArrayList<String> projects_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{outlineID});
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

    public static void deleteOutlineFromDB(Context context, String outlineID) {
        SQLiteDatabase database = new mySQLiteDBHelper(context).getWritableDatabase();
        database.delete(mySQLiteDBHelper.TABLE_OUTLINE, "_id = ?", new String[]{outlineID});
        database.close();
    }
    //endregion

    //Gets only Character names by Project ID.
    public static ArrayList<String> getCharNamesByID(Context context, String project_id){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_CHARACTER + " WHERE project_id = ?";
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

    //This function to get Timeline by Character ID.
    public static ArrayList<String> getTimelineByCharacterID(Context context, String character_id){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_TIMELINE + " WHERE character_id = ?" + " ORDER BY CAST(date AS INTEGER) ASC" ;
        ArrayList<String> timeline_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{character_id});
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String timeID = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_ID));
                String timeTitle = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_TITLE));
                String timeDescription = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_DESCRIPTION));
                String position = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_POSITION));
                String date = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_DATE));
                timeline_list.add(timeID + "/&&/" + timeTitle + "/&&/" + timeDescription + "/&&/" + position + "/&&/" + date);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", "Character ID is null" + e.toString());
        }
        sqLiteDatabase.close();
        return timeline_list;
    }


    //This function to get Timeline by ID.
    public static ArrayList<String> getTimelineByID(Context context, String outlineID){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_TIMELINE + " WHERE _id = ?";
        ArrayList<String> timeline_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{outlineID});
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String outID = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_ID));
                String outTitle = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_TITLE));
                String outDescription = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_DESCRIPTION));
                String position = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_POSITION));
                String date = cursor.getString(cursor.getColumnIndex(mySQLiteDBHelper.TIMELINE_COLUMN_DATE));
                timeline_list.add(outID + "/&&/" + outTitle + "/&&/" + outDescription + "/&&/" + position + "/&&/" + date);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", "Project ID is null" + e.toString());
        }
        sqLiteDatabase.close();
        return timeline_list;
    }

    public static void deleteTimelineFromDB(Context context, String timelineID) {
        SQLiteDatabase database = new mySQLiteDBHelper(context).getWritableDatabase();
        database.delete(mySQLiteDBHelper.TABLE_TIMELINE, "_id = ?", new String[]{timelineID});
        database.close();
    }


}
