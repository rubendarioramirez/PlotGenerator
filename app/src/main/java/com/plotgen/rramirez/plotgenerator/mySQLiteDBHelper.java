package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mySQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    //public static final String DATABASE_NAME = "test_database";
    public static final String DATABASE_NAME = "production_database";
    public static final String CHARACTER_TABLE_CHARACTER = "character";
    public static final String CHARACTER_TABLE_PROJECT = "projectname";
    public static final String CHARACTER_COLUMN_ID = "_id";
    public static final String CHARACTER_COLUMN_PROJECT = "project";
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
    public static final String CHARACTER_COLUMN_EIR = "elevator_initial_reaction";
    public static final String CHARACTER_COLUMN_EWR = "elevator_wait_rescue";
    public static final String CHARACTER_COLUMN_EHP = "elevator_help_partner";
    public static final String CHARACTER_COLUMN_EEF = "elevator_escape_first";
    public static final String CHARACTER_COLUMN_ENOTES = "elevator_notes";


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
                CHARACTER_COLUMN_JOB + " TEXT " + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + CHARACTER_TABLE_PROJECT + " (" +
                CHARACTER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHARACTER_COLUMN_PROJECT + " TEXT " + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_CHARACTER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_PROJECT);
        onCreate(sqLiteDatabase);
    }
}
