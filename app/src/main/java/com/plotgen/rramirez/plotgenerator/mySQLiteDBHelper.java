package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by macintosh on 27/08/18.
 */

public class mySQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    //TODO Change the name of DB for production database
    public static final String DATABASE_NAME = "new_database";
    public static final String CHARACTER_TABLE_NAME = "character";
    public static final String CHARACTER_COLUMN_ID = "_id";
    public static final String CHARACTER_COLUMN_PROJECT = "project";
    public static final String CHARACTER_COLUMN_NAME = "name";
    public static final String CHARACTER_COLUMN_NICKNAME = "nickname";
    public static final String CHARACTER_COLUMN_AGE = "age";
    public static final String CHARACTER_COLUMN_GENDER = "gender";
    public static final String CHARACTER_COLUMN_Pbirth = "place of birth";
    public static final String CHARACTER_COLUMN_JOB = "profession";

    public mySQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CHARACTER_TABLE_NAME + " (" +
                CHARACTER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHARACTER_COLUMN_PROJECT + " TEXT, " +
                CHARACTER_COLUMN_NAME + " TEXT, " +
                CHARACTER_COLUMN_NICKNAME + " TEXT, " +
                CHARACTER_COLUMN_AGE + " INT UNSIGNED, " +
                CHARACTER_COLUMN_GENDER + " TEXT, " +
                CHARACTER_COLUMN_Pbirth + " TEXT, " +
                CHARACTER_COLUMN_JOB + " TEXT " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
