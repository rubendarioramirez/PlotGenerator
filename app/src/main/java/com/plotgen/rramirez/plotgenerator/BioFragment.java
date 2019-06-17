package com.plotgen.rramirez.plotgenerator;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Tutorial;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.THUMBNAIL_SIZE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioFragment extends Fragment {

    ArrayList<String> char_description;
    private FirebaseAnalytics mFirebaseAnalytics;
    String char_name, charID, project_name, charRole;

    @BindView(R.id.character_bio_name_title)
    TextView title;
    @BindView(R.id.character_bio_role_title)
    TextView role_subtitle;
    @BindView(R.id.character_bio_intro)
    TextView intro_tv;
    @BindView(R.id.bio_charimage)
    ImageView bio_charimage;

    private String filepath = "";


    public BioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myFragmentView = inflater.inflate(R.layout.fragment_bio, container, false);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        ButterKnife.bind(this, myFragmentView);

        char_description = new ArrayList<String>();

        if(Common.currentCharacter !=null) {
            try {
                char_name = Common.currentCharacter.getName();
                charID = Common.currentCharacter.getId();
                charRole = Common.currentCharacter.getRole();
            } catch (Exception e) {
                Log.v("matilda", e.toString());
            }
        }


        ((MainActivity) getActivity()).setActionBarTitle(char_name);
        char_description = getDescription(myFragmentView.getContext());
        // changes done to check list size
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
            String imageToShow = char_description.get(18);
            if (imageToShow!=null && !imageToShow.isEmpty()){
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(char_description.get(18)), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
                bio_charimage.setImageBitmap(ThumbImage);
                filepath = char_description.get(18);
            } else {
                String defaultImagePath = "android.resource://com.plotgen.rramirez.plotgenerator/drawable/ic_menu_camera";
                bio_charimage.setImageURI(Uri.parse(defaultImagePath));
                filepath = char_description.get(18);
            }


            Common.currentCharacter.setGender(gender);
            Common.currentCharacter.setRole(role);

            //Titles
            title.setText(char_name);
            role_subtitle.setText(charRole);

            StringBuffer bio_text = new StringBuffer();
            bio_text.append(name + " " + getString(R.string.age_bio_1) + " " + age + " " + getString(R.string.age_bio_2) + "<br>");
            bio_text.append(getString(R.string.placebirth_bio) + " " + placebirth + "<br>");
            if(job.equals("Arbeitslos") || job.equals("Desempleado") || job.equals("Desempleada") || job.equals("Unemployed")|| job.equals("Pensiun")){
                bio_text.append(getString(R.string.nojob_bio) + " " + job + "<br>");
            } else {
                bio_text.append(getString(R.string.job_bio) + " " + job + "<br>");
            }
            bio_text.append(getString(R.string.height_bio) + " " + height + "<br>");
            bio_text.append(getString(R.string.hair_bio_1) + " " + haircolor + " " + getString(R.string.hair_bio_2));
            bio_text.append(" " + getString(R.string.eyes_bio) + " " + eyecolor + "<br>");
            bio_text.append(getString(R.string.bodybuild_bio) + " " + bodybuild + "<br>");


            if (gender.equals("Masculino") || gender.equals("Male") || gender.equals("Pria")) {
                bio_text.append(getString(R.string.male_desire_bio) + " " + desire + "<br>");
                bio_text.append(getString(R.string.male_need_bio) + " " + need + "?<br>");
                bio_text.append(getString(R.string.male_moment_bio) + " " + moment + "<br>");
                bio_text.append(getString(R.string.male_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(getString(R.string.male_phrase_bio) + "<br> " + phrase + "<br><br>");
            } else if (gender.equals("Femenino") || gender.equals("Female") || gender.equals("Wanita")) {
                bio_text.append(getString(R.string.female_desire_bio) + " " + desire + "<br>");
                bio_text.append(getString(R.string.female_need_bio) + " " + need + "?<br>");
                bio_text.append(getString(R.string.female_moment_bio) + " " + moment + "<br>");
                bio_text.append(getString(R.string.female_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(getString(R.string.female_phrase_bio) + "<br> " + phrase + "<br><br>");
            } else {
                bio_text.append(getString(R.string.binary_desire_bio) + " " + desire + "<br>");
                bio_text.append(getString(R.string.binary_need_bio) + " " + need + "?<br>");
                bio_text.append(getString(R.string.binary_moment_bio) + " " + moment + "<br>");
                bio_text.append(getString(R.string.binary_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(getString(R.string.binary_phrase_bio) + "<br> " + phrase + "<br><br>");
            }

            bio_text.append(getString(R.string.notes_bio) + "<br> " + notes + "<br><br><br><br><br><br>");
            intro_tv.setText(Html.fromHtml(bio_text.toString()));


        }


        Tutorial.checkTutorial(myFragmentView,getActivity());

        return myFragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("");
    }

    public void SHARE(View view, String body, String char_name) {

        // Do something in response to button
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, char_name);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, "share"));

        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("Share", "completed");
        mFirebaseAnalytics.logEvent("share_completed", params);

    }


    public ArrayList<String> getDescription(Context context) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE _id = ?";
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



}
