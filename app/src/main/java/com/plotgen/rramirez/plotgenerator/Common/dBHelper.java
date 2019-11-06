package com.plotgen.rramirez.plotgenerator.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class dBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 22;

    public static final String DATABASE_NAME = "production_database";
    public static final String TABLE_CHARACTER = "character";
    public static final String TABLE_PROJECT = "projectname";
    public static final String TABLE_OUTLINE = "outline";
    public static final String TABLE_TIMELINE = "timeline";
    public static final String TABLE_CHALLENGE = "challenges";

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

    public static final String OUTLINE_COLUMN_ID = "_id";
    public static final String OUTLINE_COLUMN_NAME = "name";
    public static final String OUTLINE_COLUMN_DESCRIPTION = "description";
    public static final String OUTLINE_COLUMN_CHARACTERS = "charactersPresent";
    public static final String OUTLINE_COLUMN_PROJECT_ID = "project_id";
    public static final String OUTLINE_COLUMN_POSITION = "position";

    public static final String TIMELINE_COLUMN_ID = "_id";
    public static final String TIMELINE_COLUMN_TITLE = "title";
    public static final String TIMELINE_COLUMN_DESCRIPTION = "description";
    public static final String TIMELINE_COLUMN_CHARACTER_ID = "character_id";
    public static final String TIMELINE_COLUMN_POSITION = "position";
    public static final String TIMELINE_COLUMN_DATE = "date";

    //Challenges Table
    public static final String CHALLENGE_ID = "challengeID";
    public static final String CHALLENGE_CHAR_ID = "characterID";
    public static final String CHALLENGE_Q1 = "q1";
    public static final String CHALLENGE_Q2 = "q2";
    public static final String CHALLENGE_Q3 = "q3";
    public static final String CHALLENGE_Q4 = "q4";

    //Character BIO
    //region BIO Region
    public static final String CHARACTER_COLUMN_ID = "_id";
    public static final String CHARACTER_COLUMN_PROJECT = "project";
    public static final String CHARACTER_COLUMN_PROJECT_ID = "project_id";
    public static final String CHARACTER_COLUMN_IMAGE = "image";

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
    public static final String CHARACTER_COLUMN_BIRTHDATE = "birthdate";

    public static final String CHARACTER_COLUMN_ROLE = "role";
    public static final String CHARACTER_COLUMN_DEFMOMENT = "defmoment";
    public static final String CHARACTER_COLUMN_NEED = "need";
    public static final String CHARACTER_COLUMN_PLACEBIRTH = "placebirth";
    public static final String CHARACTER_COLUMN_PHRASE = "phrase";
    public static final String CHARACTER_COLUMN_TRAIT1 = "trait1";
    public static final String CHARACTER_COLUMN_TRAIT2 = "trait2";
    public static final String CHARACTER_COLUMN_TRAIT3 = "trait3";
    //Challenge I old naming.
    public static final String CHAR_EIR = "elevator_initial_reaction";
    public static final String CHAR_EWR = "elevator_wait_rescue";
    public static final String CHAR_EHP = "elevator_help_partner";
    public static final String CHAR_EEF = "elevator_escape_first";
    public static final String CHAR_ENOTES = "elevator_notes";
    //endregion
    public static final String CHAR_challengesCompleted = "challengesCompleted";




    public dBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CHARACTER + " (" +
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
                CHAR_EIR + " TEXT, " +
                CHAR_EWR + " TEXT, " +
                CHAR_EHP + " TEXT, " +
                CHAR_EEF + " TEXT, " +
                CHAR_ENOTES + " TEXT, " +
                CHARACTER_COLUMN_JOB + " TEXT," +
                CHARACTER_COLUMN_HEIGHT + " TEXT," +
                CHARACTER_COLUMN_HAIRCOLOR + " TEXT," +
                CHARACTER_COLUMN_EYECOLOR + " TEXT," +
                CHARACTER_COLUMN_BODYBUILD + " TEXT," +
                CHARACTER_COLUMN_BIRTHDATE + " TEXT," +
                CHARACTER_COLUMN_PROJECT_ID + " TEXT, " +
                CHARACTER_COLUMN_PHRASE + " TEXT, " +
                CHARACTER_COLUMN_IMAGE + " TEXT, " +
                CHAR_challengesCompleted + " INTEGER " +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PROJECT + " (" +
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
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_OUTLINE + " (" +
                OUTLINE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OUTLINE_COLUMN_NAME + " TEXT, " +
                OUTLINE_COLUMN_DESCRIPTION + " TEXT, " +
                OUTLINE_COLUMN_PROJECT_ID + " TEXT, " +
                OUTLINE_COLUMN_POSITION + " TEXT, " +
                OUTLINE_COLUMN_CHARACTERS + " TEXT " +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TIMELINE + " (" +
                TIMELINE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIMELINE_COLUMN_TITLE + " TEXT, " +
                TIMELINE_COLUMN_DESCRIPTION + " TEXT, " +
                TIMELINE_COLUMN_CHARACTER_ID + " TEXT, " +
                TIMELINE_COLUMN_POSITION + " TEXT, " +
                TIMELINE_COLUMN_DATE + " TEXT " +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CHALLENGE + " (" +
                CHALLENGE_ID + " TEXT, " +
                CHALLENGE_CHAR_ID + " TEXT, " +
                CHALLENGE_Q1 + " TEXT, " +
                CHALLENGE_Q2 + " TEXT, " +
                CHALLENGE_Q3 + " TEXT, " +
                CHALLENGE_Q4 + " TEXT " +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i1) {
            case 20:
                try {
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN birthdate TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: Char project id" + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("CREATE TABLE outline (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN name  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN description  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN project_id  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN position  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN charactersPresent  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("CREATE TABLE timeline (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN title  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN description  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN character_id  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN position  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN date  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                break;

            case 22:
                try {
                    sqLiteDatabase.execSQL("ALTER TABLE character ADD COLUMN birthdate TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: Char project id" + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("CREATE TABLE outline (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN name  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN description  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN project_id  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN position  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE outline ADD COLUMN charactersPresent  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("CREATE TABLE timeline (_id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN title  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN description  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN character_id  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN position  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE timeline ADD COLUMN date  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }

                try {
                    sqLiteDatabase.execSQL("CREATE TABLE challenges (challengeID TEXT)");
                    sqLiteDatabase.execSQL("ALTER TABLE challenges ADD COLUMN characterID  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE challenges ADD COLUMN q1  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE challenges ADD COLUMN q2  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE challenges ADD COLUMN q3  TEXT");
                    sqLiteDatabase.execSQL("ALTER TABLE challenges ADD COLUMN q4  TEXT");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }


                //Port Challenges
                try {
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_1', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, elevator_initial_reaction as q1, elevator_wait_rescue as q2, elevator_help_partner as q3, elevator_escape_first as q4 from character ) WHERE q1 IS NOT NULL ");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_2', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_2_q1 as q1, challenge_2_q2 as q2, challenge_2_q3 as q3, challenge_2_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_3', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_3_q1 as q1, challenge_3_q2 as q2, challenge_3_q3 as q3, challenge_3_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_4', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_4_q1 as q1, challenge_4_q2 as q2, challenge_4_q3 as q3, challenge_4_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_5', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_5_q1 as q1, challenge_5_q2 as q2, challenge_5_q3 as q3, challenge_5_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_6', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_6_q1 as q1, challenge_6_q2 as q2, challenge_6_q3 as q3, challenge_6_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_7', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_7_q1 as q1, challenge_7_q2 as q2, challenge_7_q3 as q3, challenge_7_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'challenge_8', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, challenge_8_q1 as q1, challenge_8_q2 as q2, challenge_8_q3 as q3, challenge_8_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'mentor_challenge', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, c1_mentor_q1 as q1, c1_mentor_q2 as q2, c1_mentor_q3 as q3, c1_mentor_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'sidekick_challenge', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, c1_sidekick_q1 as q1, c1_sidekick_q2 as q2, c1_sidekick_q3 as q3, c1_sidekick_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    sqLiteDatabase.execSQL("INSERT INTO challenges (challengeID, characterID, q1,q2,q3,q4) SELECT 'antagonist_challenge', cast(characterID AS TEXT), q1,q2,q3,q4 FROM (SELECT _id as characterID, c1_antagonist_q1 as q1, c1_antagonist_q2 as q2, c1_antagonist_q3 as q3, c1_antagonist_q4 as q4 from character ) WHERE q1 IS NOT NULL");
                    Log.v("matilda", "worked");
                } catch (SQLiteException ex) {
                    Log.w("Matilda", "Altering: " + ex.getMessage());
                }
                break;

            }


        }
}
