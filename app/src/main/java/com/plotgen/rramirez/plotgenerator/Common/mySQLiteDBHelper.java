package com.plotgen.rramirez.plotgenerator.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySQLiteDBHelper extends SQLiteOpenHelper {

    //public static final String DATABASE_NAME = "test_database";
    //  private static final int DATABASE_VERSION = 12;
    //  private static final int DATABASE_VERSION = 13;
    private static final int DATABASE_VERSION = 14;

    public static final String DATABASE_NAME = "production_database";
    public static final String CHARACTER_TABLE_CHARACTER = "character";
    public static final String CHARACTER_TABLE_PROJECT = "projectname";
    public static final String CHARACTER_COLUMN_ID = "_id";
    public static final String CHARACTER_COLUMN_PROJECT = "project";
    public static final String CHARACTER_COLUMN_PROJECT_ID = "project_id";
    public static final String CHARACTER_COLUMN_IMAGE = "image";

    public static final String PROJECT_COLUMN_ID = "_id";
    public static final String PROJECT_COLUMN_PROJECT = "project";
    public static final String PROJECT_COLUMN_GENRE = "genre";
    public static final String PROJECT_COLUMN_PLOT = "plot";
    public static final String PROJECT_COLUMN_IMAGE = "image";

    public static final String CHARACTER_TABLE_STORY = "story";
    public static final String STORY_COLUMN_ID = "_id";
    public static final String STORY_COLUMN_PROJECT = "project";
    public static final String STORY_COLUMN_PROJECT_ID = "project_id";
    public static final String STORY_COLUMN_STORIES = "stories";
    public static final String STORY_COLUMN_IMAGE = "image";


    //Character BIO
    public static final String CHARACTER_COLUMN_NAME = "name";
    public static final String CHARACTER_COLUMN_NICKNAME = "nickname";
    public static final String CHARACTER_COLUMN_AGE = "age";
    public static final String CHARACTER_COLUMN_GENDER = "gender";
    public static final String CHARACTER_COLUMN_DESIRE = "desire";
    public static final String CHARACTER_COLUMN_JOB = "profession";

    public static final String CHARACTER_COLUMN_HEIGHT = "height";
    public static final String CHARACTER_COLUMN_HAIRCOLOR = "haircolor";
    public static final String CHARACTER_COLUMN_EYECOLOR = "eyecolor";
    public static final String CHARACTER_COLUMN_BODYBUILD = "bodybuild";

    public static final String CHARACTER_COLUMN_ROLE = "role";
    public static final String CHARACTER_COLUMN_DEFMOMENT = "defmoment";
    public static final String CHARACTER_COLUMN_NEED = "need";
    public static final String CHARACTER_COLUMN_PLACEBIRTH = "placebirth";
    public static final String CHARACTER_COLUMN_PHRASE = "phrase";
    public static final String CHARACTER_COLUMN_TRAIT1 = "trait1";
    public static final String CHARACTER_COLUMN_TRAIT2 = "trait2";
    public static final String CHARACTER_COLUMN_TRAIT3 = "trait3";
    //Challenge I old naming.
    public static final String CHARACTER_COLUMN_EIR = "elevator_initial_reaction";
    public static final String CHARACTER_COLUMN_EWR = "elevator_wait_rescue";
    public static final String CHARACTER_COLUMN_EHP = "elevator_help_partner";
    public static final String CHARACTER_COLUMN_EEF = "elevator_escape_first";
    public static final String CHARACTER_COLUMN_ENOTES = "elevator_notes";
    //Challenge II.
    public static final String CHARACTER_COLUMN_C2Q1 = "challenge_2_q1";
    public static final String CHARACTER_COLUMN_C2Q2 = "challenge_2_q2";
    public static final String CHARACTER_COLUMN_C2Q3 = "challenge_2_q3";
    public static final String CHARACTER_COLUMN_C2Q4 = "challenge_2_q4";
    //Challenge III.
    public static final String CHARACTER_COLUMN_C3Q1 = "challenge_3_q1";
    public static final String CHARACTER_COLUMN_C3Q2 = "challenge_3_q2";
    public static final String CHARACTER_COLUMN_C3Q3 = "challenge_3_q3";
    public static final String CHARACTER_COLUMN_C3Q4 = "challenge_3_q4";
    //Challenge IV.
    public static final String CHARACTER_COLUMN_C4Q1 = "challenge_4_q1";
    public static final String CHARACTER_COLUMN_C4Q2 = "challenge_4_q2";
    public static final String CHARACTER_COLUMN_C4Q3 = "challenge_4_q3";
    public static final String CHARACTER_COLUMN_C4Q4 = "challenge_4_q4";
    //Challenge V.
    public static final String CHARACTER_COLUMN_C5Q1 = "challenge_5_q1";
    public static final String CHARACTER_COLUMN_C5Q2 = "challenge_5_q2";
    public static final String CHARACTER_COLUMN_C5Q3 = "challenge_5_q3";
    public static final String CHARACTER_COLUMN_C5Q4 = "challenge_5_q4";
    //Challenge VI.
    public static final String CHARACTER_COLUMN_C6Q1 = "challenge_6_q1";
    public static final String CHARACTER_COLUMN_C6Q2 = "challenge_6_q2";
    public static final String CHARACTER_COLUMN_C6Q3 = "challenge_6_q3";
    public static final String CHARACTER_COLUMN_C6Q4 = "challenge_6_q4";
    //Challenge VII.
    public static final String CHARACTER_COLUMN_C7Q1 = "challenge_7_q1";
    public static final String CHARACTER_COLUMN_C7Q2 = "challenge_7_q2";
    public static final String CHARACTER_COLUMN_C7Q3 = "challenge_7_q3";
    public static final String CHARACTER_COLUMN_C7Q4 = "challenge_7_q4";
    //Challenge VIII.
    public static final String CHARACTER_COLUMN_C8Q1 = "challenge_8_q1";
    public static final String CHARACTER_COLUMN_C8Q2 = "challenge_8_q2";
    public static final String CHARACTER_COLUMN_C8Q3 = "challenge_8_q3";
    public static final String CHARACTER_COLUMN_C8Q4 = "challenge_8_q4";
    //Mentor Challenge.
    public static final String CHARACTER_COLUMN_C1_MENTOR_Q1 = "c1_mentor_q1";
    public static final String CHARACTER_COLUMN_C1_MENTOR_Q2 = "c1_mentor_q2";
    public static final String CHARACTER_COLUMN_C1_MENTOR_Q3 = "c1_mentor_q3";
    public static final String CHARACTER_COLUMN_C1_MENTOR_Q4 = "c1_mentor_q4";
    //Antagonist Challenge.
    public static final String CHARACTER_COLUMN_C1_ANTAGONIST_Q1 = "c1_antagonist_q1";
    public static final String CHARACTER_COLUMN_C1_ANTAGONIST_Q2 = "c1_antagonist_q2";
    public static final String CHARACTER_COLUMN_C1_ANTAGONIST_Q3 = "c1_antagonist_q3";
    public static final String CHARACTER_COLUMN_C1_ANTAGONIST_Q4 = "c1_antagonist_q4";
    //Escudero Challenge.
    public static final String CHARACTER_COLUMN_C1_sidekick_Q1 = "c1_sidekick_q1";
    public static final String CHARACTER_COLUMN_C1_sidekick_Q2 = "c1_sidekick_q2";
    public static final String CHARACTER_COLUMN_C1_sidekick_Q3 = "c1_sidekick_q3";
    public static final String CHARACTER_COLUMN_C1_sidekick_Q4 = "c1_sidekick_q4";


    public mySQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CHARACTER_TABLE_CHARACTER + " (" +
                CHARACTER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHARACTER_COLUMN_PROJECT + " TEXT, " +
                CHARACTER_COLUMN_NAME + " TEXT, " +
                CHARACTER_COLUMN_NICKNAME + " TEXT, " +
                CHARACTER_COLUMN_AGE + " INT, " +
                CHARACTER_COLUMN_GENDER + " TEXT, " +
                CHARACTER_COLUMN_DESIRE + " TEXT, " +
                CHARACTER_COLUMN_ROLE + " TEXT, " +
                CHARACTER_COLUMN_DEFMOMENT + " TEXT, " +
                CHARACTER_COLUMN_NEED + " TEXT, " +
                CHARACTER_COLUMN_PLACEBIRTH + " TEXT, " +
                CHARACTER_COLUMN_TRAIT1 + " TEXT, " +
                CHARACTER_COLUMN_TRAIT2 + " TEXT, " +
                CHARACTER_COLUMN_TRAIT3 + " TEXT, " +
                CHARACTER_COLUMN_EIR + " TEXT, " +
                CHARACTER_COLUMN_EWR + " TEXT, " +
                CHARACTER_COLUMN_EHP + " TEXT, " +
                CHARACTER_COLUMN_EEF + " TEXT, " +
                CHARACTER_COLUMN_ENOTES + " TEXT, " +
                CHARACTER_COLUMN_JOB + " TEXT," +
                CHARACTER_COLUMN_HEIGHT + " TEXT," +
                CHARACTER_COLUMN_HAIRCOLOR + " TEXT," +
                CHARACTER_COLUMN_EYECOLOR + " TEXT," +
                CHARACTER_COLUMN_BODYBUILD + " TEXT," +
                CHARACTER_COLUMN_C2Q1 + " TEXT, " +
                CHARACTER_COLUMN_C2Q2 + " TEXT, " +
                CHARACTER_COLUMN_C2Q3 + " TEXT, " +
                CHARACTER_COLUMN_C2Q4 + " TEXT, " +
                CHARACTER_COLUMN_C3Q1 + " TEXT, " +
                CHARACTER_COLUMN_C3Q2 + " TEXT, " +
                CHARACTER_COLUMN_C3Q3 + " TEXT, " +
                CHARACTER_COLUMN_C3Q4 + " TEXT, " +
                CHARACTER_COLUMN_C4Q1 + " TEXT, " +
                CHARACTER_COLUMN_C4Q2 + " TEXT, " +
                CHARACTER_COLUMN_C4Q3 + " TEXT, " +
                CHARACTER_COLUMN_C4Q4 + " TEXT, " +
                CHARACTER_COLUMN_C5Q1 + " TEXT, " +
                CHARACTER_COLUMN_C5Q2 + " TEXT, " +
                CHARACTER_COLUMN_C5Q3 + " TEXT, " +
                CHARACTER_COLUMN_C5Q4 + " TEXT, " +
                CHARACTER_COLUMN_C6Q1 + " TEXT, " +
                CHARACTER_COLUMN_C6Q2 + " TEXT, " +
                CHARACTER_COLUMN_C6Q3 + " TEXT, " +
                CHARACTER_COLUMN_C6Q4 + " TEXT, " +
                CHARACTER_COLUMN_C7Q1 + " TEXT, " +
                CHARACTER_COLUMN_C7Q2 + " TEXT, " +
                CHARACTER_COLUMN_C7Q3 + " TEXT, " +
                CHARACTER_COLUMN_C7Q4 + " TEXT, " +
                CHARACTER_COLUMN_C8Q1 + " TEXT, " +
                CHARACTER_COLUMN_C8Q2 + " TEXT, " +
                CHARACTER_COLUMN_C8Q3 + " TEXT, " +
                CHARACTER_COLUMN_C8Q4 + " TEXT, " +
                CHARACTER_COLUMN_C1_MENTOR_Q1 + " TEXT, " +
                CHARACTER_COLUMN_C1_MENTOR_Q2 + " TEXT, " +
                CHARACTER_COLUMN_C1_MENTOR_Q3 + " TEXT, " +
                CHARACTER_COLUMN_C1_MENTOR_Q4 + " TEXT, " +
                CHARACTER_COLUMN_PROJECT_ID + " TEXT, " +
                CHARACTER_COLUMN_PHRASE + " TEXT, " +
                CHARACTER_COLUMN_C1_ANTAGONIST_Q1 + " TEXT, " +
                CHARACTER_COLUMN_C1_ANTAGONIST_Q2 + " TEXT, " +
                CHARACTER_COLUMN_C1_ANTAGONIST_Q3 + " TEXT, " +
                CHARACTER_COLUMN_C1_ANTAGONIST_Q4 + " TEXT, " +
                CHARACTER_COLUMN_C1_sidekick_Q1 + " TEXT, " +
                CHARACTER_COLUMN_C1_sidekick_Q2 + " TEXT, " +
                CHARACTER_COLUMN_C1_sidekick_Q3 + " TEXT, " +
                CHARACTER_COLUMN_C1_sidekick_Q4 + " TEXT, " +

                CHARACTER_COLUMN_IMAGE + " TEXT " +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + CHARACTER_TABLE_PROJECT + " (" +
                PROJECT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PROJECT_COLUMN_PROJECT + " TEXT, " +
                PROJECT_COLUMN_GENRE + " TEXT, " +
                PROJECT_COLUMN_PLOT + " TEXT, " +
                PROJECT_COLUMN_IMAGE + " TEXT" +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + CHARACTER_TABLE_STORY + " (" +
                STORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STORY_COLUMN_PROJECT + " TEXT, " +
                STORY_COLUMN_PROJECT_ID + " TEXT, " +
                STORY_COLUMN_STORIES + " TEXT " +
//                STORY_COLUMN_IMAGE + " TEXT" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i1) {
            case 10:
                sqLiteDatabase.execSQL("CREATE TABLE story (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN project  TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN project_id  TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN stories  TEXT");
                break;
            case 11:
                sqLiteDatabase.execSQL("ALTER TABLE projectname ADD COLUMN " + PROJECT_COLUMN_IMAGE + " TEXT");
                break;
            case 12:
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q1 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q2 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q3 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q4 TEXT");

                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q1 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q2 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q3 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q4 TEXT");
                break;
            case 13:
                try {
                    sqLiteDatabase.execSQL("CREATE TABLE story (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN project  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN project_id  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN stories  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("ALTER TABLE projectname ADD COLUMN " + PROJECT_COLUMN_IMAGE + " TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q1 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q2 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q3 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q4 TEXT");

                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q1 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q2 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q3 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q4 TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }
                break;
            case 14:
                try {
                    sqLiteDatabase.execSQL("CREATE TABLE story (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN project  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN project_id  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE story ADD COLUMN stories  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("ALTER TABLE projectname ADD COLUMN " + PROJECT_COLUMN_IMAGE + " TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q1 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q2 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q3 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_7_q4 TEXT");

                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q1 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q2 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q3 TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_8_q4 TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }
                try {
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN image TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }
                break;
        }

    }
}
