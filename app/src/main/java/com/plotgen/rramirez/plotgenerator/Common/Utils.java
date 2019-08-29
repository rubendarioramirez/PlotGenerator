package com.plotgen.rramirez.plotgenerator.Common;

import android.app.Activity;
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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.plotgen.rramirez.plotgenerator.Fragment.PremiumFragment;
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
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_PROJECT + " WHERE project = ?";
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
        Cursor cursor = sqLiteDatabase.query(mySQLiteDBHelper.TABLE_PROJECT, null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        String image = "";
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String project_name = cursor.getString(cursor.getColumnIndex("project"));
            String genre = cursor.getString(cursor.getColumnIndex("genre"));
            if (cursor.getColumnIndex("image") != -1 && cursor.getString(cursor.getColumnIndex("image")) != null) {
                 image = cursor.getString(cursor.getColumnIndex("image"));
            }
            projects_list.add(id + "/&&/" + project_name + "/&&/" + genre + "/&&/" + image);
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
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_CHARACTER + " WHERE project_id = ?";
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

    public static ArrayList<String> getBIO(Context context, String charID) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_CHARACTER + " WHERE _id = ?";
        ArrayList<String> char_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{charID});
            // changes done to check is cursor size is greator than 0
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    char_list.add(cursor.getString(cursor.getColumnIndex("name")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("age")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("gender")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("placebirth")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("profession")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("height")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("haircolor")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("eyecolor")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("bodybuild")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("desire")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("role")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("defmoment")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("need")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("phrase")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("trait1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("trait2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("trait3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("elevator_notes")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("image")));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }
        return char_list;
    }


    public static ArrayList<String> getChallenges(Context context, String charID) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_CHARACTER + " WHERE _id = ?";
        ArrayList<String> char_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{charID});
            // changes done to check is cursor size is greator than 0
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    //First challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("elevator_initial_reaction")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("elevator_wait_rescue")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("elevator_help_partner")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("elevator_escape_first")));
                    //Second Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q4")));
                    //Third Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q4")));
                    //Fourth Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q4")));
                    //Fifth Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q4")));
                    //Sixth Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q4")));
                    //Mentor Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q4")));
                    //Antagonist Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q4")));
                    //Sidekick Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q4")));
                    //Challenge 7 Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q4")));
                    //Challenge 8 Challenge
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q1")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q2")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q3")));
                    char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q4")));

                    char_list.add(cursor.getString(cursor.getColumnIndex("role")));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }
        return char_list;
    }


    public static StringBuffer generateChallenges(Context context, String charID) {
        ArrayList<String> char_description = new ArrayList<String>();
        char_description = getChallenges(context,charID);
        StringBuffer sb = new StringBuffer();
        // changes done to check list size
        if (char_description.size() > 0) {
            //First challenge
            String firstReaction = char_description.get(0);
            String waitRescue = char_description.get(1);
            String helpPartner = char_description.get(2);
            String escapeFirst = char_description.get(3);
            //Second challenge
            String challenge2_q1 = char_description.get(4);
            String challenge2_q2 = char_description.get(5);
            String challenge2_q3 = char_description.get(6);
            String challenge2_q4 = char_description.get(7);
            //Third challenge
            String challenge3_q1 = char_description.get(8);
            String challenge3_q2 = char_description.get(9);
            String challenge3_q3 = char_description.get(10);
            String challenge3_q4 = char_description.get(11);
            //Fourth challenge
            String challenge4_q1 = char_description.get(12);
            String challenge4_q2 = char_description.get(13);
            String challenge4_q3 = char_description.get(14);
            String challenge4_q4 = char_description.get(15);
            //Fifth challenge
            String challenge5_q1 = char_description.get(16);
            String challenge5_q2 = char_description.get(17);
            String challenge5_q3 = char_description.get(18);
            String challenge5_q4 = char_description.get(19);
            //Sixth challenge
            String challenge6_q1 = char_description.get(20);
            String challenge6_q2 = char_description.get(21);
            String challenge6_q3 = char_description.get(22);
            String challenge6_q4 = char_description.get(23);
            //Mentor challenge
            String c1_mentor_q1 = char_description.get(24);
            String c1_mentor_q2 = char_description.get(25);
            String c1_mentor_q3 = char_description.get(26);
            String c1_mentor_q4 = char_description.get(27);
            //Antagonist challenge
            String c1_antagonist_q1 = char_description.get(28);
            String c1_antagonist_q2 = char_description.get(29);
            String c1_antagonist_q3 = char_description.get(30);
            String c1_antagonist_q4 = char_description.get(31);
            //Sidekick challenge
            String c1_sidekick_q1 = char_description.get(32);
            String c1_sidekick_q2 = char_description.get(33);
            String c1_sidekick_q3 = char_description.get(34);
            String c1_sidekick_q4 = char_description.get(35);
            //Seventh challenge
            String challenge7_q1 = char_description.get(36);
            String challenge7_q2 = char_description.get(37);
            String challenge7_q3 = char_description.get(38);
            String challenge7_q4 = char_description.get(39);
            //Eight challenge
            String challenge8_q1 = char_description.get(40);
            String challenge8_q2 = char_description.get(41);
            String challenge8_q3 = char_description.get(42);
            String challenge8_q4 = char_description.get(43);

            String role = char_description.get(44);


            switch (role) {
                case "Mentor":
                    if (c1_mentor_q1 != null) {
                        sb.append("<b><font color='red'>" + context.getString(R.string.c1_mentor_bio_title) + "</font></b>");
                        sb.append("<br><b>" + context.getString(R.string.c1_mentor_bio_desc_1) + " " + Common.currentCharacter.getName() + "?" + "</b><br> " + c1_mentor_q1);
                        sb.append("<br><b>" + context.getString(R.string.c1_mentor_bio_desc_2) + "</b><br> " + c1_mentor_q2);
                        sb.append("<br><b>" + context.getString(R.string.c1_mentor_bio_desc_3) + "</b><br> " + c1_mentor_q3);
                        sb.append("<br><b>" + context.getString(R.string.c1_mentor_bio_desc_4) + "</b><br> " + c1_mentor_q4);
                    }
                    break;
                case "Antagonist":
                case "Antagonista":
                    if (c1_antagonist_q1 != null) {
                        sb.append("<b><font color='red'>" + context.getString(R.string.c1_antagonist_bio_title) + "</font></b>");
                        sb.append("<br><b>" + context.getString(R.string.c1_antagonist_bio_desc_1) + "</b><br> " + c1_antagonist_q1);
                        sb.append("<br><b>" + context.getString(R.string.c1_antagonist_bio_desc_2) + "</b><br> " + c1_antagonist_q2);
                        sb.append("<br><b>" + context.getString(R.string.c1_antagonist_bio_desc_3) + "</b><br> " + c1_antagonist_q3);
                        sb.append("<br><b>" + context.getString(R.string.c1_antagonist_bio_desc_4) + "</b><br> " + c1_antagonist_q4);
                    }
                    break;
                case "Sidekick":
                case "Escudero":
                    if (c1_sidekick_q1 != null) {
                        sb.append("<b><font color='red'>" + context.getString(R.string.c1_sidekick_bio_title) + "</font></b>");
                        sb.append("<br><b>" + context.getString(R.string.c1_sidekick_bio_desc_1) + "</b><br> " + c1_sidekick_q1);
                        sb.append("<br><b>" + context.getString(R.string.c1_sidekick_bio_desc_2) + "</b><br> " + c1_sidekick_q2);
                        sb.append("<br><b>" + context.getString(R.string.c1_sidekick_bio_desc_3) + "</b><br> " + c1_sidekick_q3);
                        sb.append("<br><b>" + context.getString(R.string.c1_sidekick_bio_desc_4) + "</b><br> " + c1_sidekick_q4);
                    }
                    break;
            }


            if (firstReaction != null) {
                sb.append("<b><font color='red'>" + context.getString(R.string.challenge_1_bio_title) + "</font></b>");
                sb.append("<br><b>" + context.getString(R.string.challenge_1_bio_desc_1) + "</b><br> " + firstReaction);
                sb.append("<br><b>" + context.getString(R.string.challenge_1_bio_desc_2) + "</b><br> " + waitRescue);
                sb.append("<br><b>" + context.getString(R.string.challenge_1_bio_desc_3) + "</b><br> " + helpPartner);
                sb.append("<br><b>" + context.getString(R.string.challenge_1_bio_desc_4) + "</b><br> " + escapeFirst);
                sb.append("<br><br>");
            }
            if (challenge2_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_2_bio_title) + "</font></b> " + context.getString(R.string.challenge_2_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_2_bio_desc_1) + "</b><br>" + challenge2_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_2_bio_desc_2) + "</b><br>" + challenge2_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_2_bio_desc_3) + "</b><br>" + challenge2_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_2_bio_desc_4) + "</b><br>" + challenge2_q4);
                sb.append("<br><br>");
            }
            if (challenge3_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_3_bio_title) + "</font></b> " + context.getString(R.string.challenge_3_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_3_bio_desc_1) + "</b><br>" + challenge3_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_3_bio_desc_2) + "</b><br>" + challenge3_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_3_bio_desc_3) + "</b><br>" + challenge3_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_3_bio_desc_4) + "</b><br>" + challenge3_q4);
                sb.append("<br><br>");
            }
            if (challenge4_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_4_bio_title) + "</font></b> " + context.getString(R.string.challenge_4_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_4_bio_desc_1) + "</b><br>" + challenge4_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_4_bio_desc_2) + "</b><br>" + challenge4_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_4_bio_desc_3) + "</b><br>" + challenge4_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_4_bio_desc_4) + "</b><br>" + challenge4_q4);
                sb.append("<br><br>");
            }

            if (challenge5_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_5_bio_title) + "</font></b> " + context.getString(R.string.challenge_5_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_5_bio_desc_1) + "</b><br>" + challenge5_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_5_bio_desc_2) + "</b><br>" + challenge5_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_5_bio_desc_3) + "</b><br>" + challenge5_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_5_bio_desc_4) + "</b><br>" + challenge5_q4);
                sb.append("<br><br>");
            }
            if (challenge6_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_6_bio_title) + "</font></b> " + context.getString(R.string.challenge_6_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_6_bio_desc_1) + "</b><br>" + challenge6_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_6_bio_desc_2) + "</b><br>" + challenge6_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_6_bio_desc_3) + "</b><br>" + challenge6_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_6_bio_desc_4) + "</b><br>" + challenge6_q4);
                sb.append("<br><br>");
            }

            if (challenge7_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_7_bio_title) + "</font></b> " + context.getString(R.string.challenge_7_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_7_bio_desc_1) + "</b><br>" + challenge7_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_7_bio_desc_2) + "</b><br>" + challenge7_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_7_bio_desc_3) + "</b><br>" + challenge7_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_7_bio_desc_4) + "</b><br>" + challenge7_q4);
                sb.append("<br><br>");
            }if (challenge8_q1 != null) {
                sb.append("<br><b><font color='red'>" + context.getString(R.string.challenge_8_bio_title) + "</font></b> " + context.getString(R.string.challenge_8_bio_subtitle) + " <br>");
                sb.append("<br><b>" + context.getString(R.string.challenge_8_bio_desc_1) + "</b><br>" + challenge8_q1);
                sb.append("<br><b>" + context.getString(R.string.challenge_8_bio_desc_2) + "</b><br>" + challenge8_q2);
                sb.append("<br><b>" + context.getString(R.string.challenge_8_bio_desc_3) + "</b><br>" + challenge8_q3);
                sb.append("<br><b>" + context.getString(R.string.challenge_8_bio_desc_4) + "</b><br>" + challenge8_q4);
                sb.append("<br><br>");
            }
        }
            return sb;
        }

    public static ArrayList<String> getPhoto(Context context, String charID) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.TABLE_CHARACTER + " WHERE _id = ?";
        ArrayList<String> char_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{charID});
            // changes done to check is cursor size is greator than 0
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    char_list.add(cursor.getString(cursor.getColumnIndex("image")));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }
        return char_list;
    }

    public static String generateBIO(Context context, String charID){
        ArrayList<String> char_description = new ArrayList<String>();

        char_description = Utils.getBIO(context, charID);
        StringBuffer bio_text = new StringBuffer();

        if (char_description.size() > 0) {
            String name = char_description.get(0);
            String age = char_description.get(1);
            final String gender = char_description.get(2);
            String placebirth = char_description.get(3);
            String job = char_description.get(4);
            String height = char_description.get(5);
            String haircolor = char_description.get(6);
            String eyecolor = char_description.get(7);
            String bodybuild = char_description.get(8);
            String desire = char_description.get(9);
            String role = char_description.get(10);
            String moment = char_description.get(11);
            String need = char_description.get(12);
            String phrase = char_description.get(13);
            final String trait1 = char_description.get(14);
            String trait2 = char_description.get(15);
            String trait3 = char_description.get(16);
            String notes = char_description.get(17);

            Common.currentCharacter.setGender(gender);
            Common.currentCharacter.setRole(role);

            bio_text.append(name + " " + context.getString(R.string.age_bio_1) + " " + age + " " + context.getString(R.string.age_bio_2) + "<br>");
            bio_text.append(context.getString(R.string.placebirth_bio) + " " + placebirth + "<br>");
            if(job.equals("Arbeitslos") || job.equals("Desempleado") || job.equals("Desempleada") || job.equals("Unemployed")|| job.equals("Pensiun")){
                bio_text.append(context.getString(R.string.nojob_bio) + " " + job + "<br>");
            } else {
                bio_text.append(context.getString(R.string.job_bio) + " " + job + "<br>");
            }
            bio_text.append(context.getString(R.string.height_bio) + " " + height + "<br>");
            bio_text.append(context.getString(R.string.hair_bio_1) + " " + haircolor + " " + context.getString(R.string.hair_bio_2));
            bio_text.append(" " + context.getString(R.string.eyes_bio) + " " + eyecolor + "<br>");
            bio_text.append(context.getString(R.string.bodybuild_bio) + " " + bodybuild + "<br>");


            if (gender.equals("Masculino") || gender.equals("Male") || gender.equals("Pria")) {
                bio_text.append(context.getString(R.string.male_desire_bio) + " " + desire + "<br>");
                bio_text.append(context.getString(R.string.male_need_bio) + " " + need + "?<br>");
                bio_text.append(context.getString(R.string.male_moment_bio) + " " + moment + "<br>");
                bio_text.append(context.getString(R.string.male_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(context.getString(R.string.male_phrase_bio) + "<br> " + phrase + "<br><br>");
            } else if (gender.equals("Femenino") || gender.equals("Female") || gender.equals("Wanita")) {
                bio_text.append(context.getString(R.string.female_desire_bio) + " " + desire + "<br>");
                bio_text.append(context.getString(R.string.female_need_bio) + " " + need + "?<br>");
                bio_text.append(context.getString(R.string.female_moment_bio) + " " + moment + "<br>");
                bio_text.append(context.getString(R.string.female_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(context.getString(R.string.female_phrase_bio) + "<br> " + phrase + "<br><br>");
            } else {
                bio_text.append(context.getString(R.string.binary_desire_bio) + " " + desire + "<br>");
                bio_text.append(context.getString(R.string.binary_need_bio) + " " + need + "?<br>");
                bio_text.append(context.getString(R.string.binary_moment_bio) + " " + moment + "<br>");
                bio_text.append(context.getString(R.string.binary_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(context.getString(R.string.binary_phrase_bio) + "<br> " + phrase + "<br><br>");
            }

            bio_text.append(context.getString(R.string.notes_bio) + "<br> " + notes + "<br><br><br><br><br><br>");

        }

        return bio_text.toString();
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
        database.delete(mySQLiteDBHelper.TABLE_PROJECT, "_id = ?", new String[]{projectID});
        database.close();
    }


    public static void changeFragment(Fragment nextFragment, FragmentTransaction transaction) {
        //Make the transaction
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
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
                .setNegativeButton(context.getString(R.string.rate_cancel), null);
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





    public static boolean checkFirstTime(Context context){
        int first_time = getSharePref(context,"first_time",0);

        if(first_time == 0){
            saveOnSharePreg(context,"first_time",1);
            return true;
        } else {
            return false;
        }
    }


}
