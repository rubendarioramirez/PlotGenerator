package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mySQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    //TODO Change the name of DB for production database
    public static final String DATABASE_NAME = "new_database";
    public static final String CHARACTER_TABLE_CHARACTER = "character";
    public static final String CHARACTER_TABLE_PROJECT = "projectname";
    public static final String CHARACTER_COLUMN_ID = "_id";
    public static final String CHARACTER_COLUMN_PROJECT = "project";
    public static final String CHARACTER_COLUMN_NAME = "name";
    public static final String CHARACTER_COLUMN_NICKNAME = "nickname";
    public static final String CHARACTER_COLUMN_AGE = "age";
    public static final String CHARACTER_COLUMN_GENDER = "gender";
    public static final String CHARACTER_COLUMN_Pbirth = "place of birth";
    public static final String CHARACTER_COLUMN_DESIRE = "desire";
    public static final String CHARACTER_COLUMN_JOB = "profession";
    public static final String CHARACTER_COLUMN_ROLE = "role";
    public static final String CHARACTER_COLUMN_DEFMOMENT = "defmoment";
    public static final String CHARACTER_COLUMN_NEED = "need";
    public static final String CHARACTER_COLUMN_PLACEBIRTH = "placebirth";

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
                CHARACTER_COLUMN_Pbirth + " TEXT, " +
                CHARACTER_COLUMN_DESIRE + " TEXT, " +
                CHARACTER_COLUMN_ROLE + " TEXT, " +
                CHARACTER_COLUMN_DEFMOMENT + " TEXT, " +
                CHARACTER_COLUMN_NEED + " TEXT, " +
                CHARACTER_COLUMN_PLACEBIRTH + " TEXT, " +
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
