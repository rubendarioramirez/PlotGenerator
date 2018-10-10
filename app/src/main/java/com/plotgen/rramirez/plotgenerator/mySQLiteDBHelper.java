package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    //public static final String DATABASE_NAME = "test_database";
    public static final String DATABASE_NAME = "production_database";
    public static final String CHARACTER_TABLE_CHARACTER = "character";
    public static final String CHARACTER_TABLE_PROJECT = "projectname";
    public static final String CHARACTER_COLUMN_ID = "_id";
    public static final String CHARACTER_COLUMN_PROJECT = "project";
    //Character BIO
    public static final String CHARACTER_COLUMN_NAME = "name";
    public static final String CHARACTER_COLUMN_NICKNAME = "nickname";
    public static final String CHARACTER_COLUMN_AGE = "age";
    public static final String CHARACTER_COLUMN_GENDER = "gender";
    public static final String CHARACTER_COLUMN_DESIRE = "desire";
    public static final String CHARACTER_COLUMN_JOB = "profession";
    public static final String CHARACTER_COLUMN_ROLE = "role";
    public static final String CHARACTER_COLUMN_DEFMOMENT = "defmoment";
    public static final String CHARACTER_COLUMN_NEED = "need";
    public static final String CHARACTER_COLUMN_PLACEBIRTH = "placebirth";
    public static final String CHARACTER_COLUMN_TRAIT1 = "trait1";
    public static final String CHARACTER_COLUMN_TRAIT2  = "trait2";
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
                CHARACTER_COLUMN_AGE + " INT UNSIGNED, " +
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
                CHARACTER_COLUMN_C5Q4 + " TEXT " + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + CHARACTER_TABLE_PROJECT + " (" +
                CHARACTER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHARACTER_COLUMN_PROJECT + " TEXT " + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_CHARACTER);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_PROJECT);
        switch(i1) {
            case 5:
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_3_q1 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_3_q2 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_3_q3 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_3_q4 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_4_q1 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_4_q2 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_4_q3 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_4_q4 TEXT");

            case 6:
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_5_q1 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_5_q2 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_5_q3 TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN challenge_5_q4 TEXT");
        }


        //onCreate(sqLiteDatabase);
    }
}
