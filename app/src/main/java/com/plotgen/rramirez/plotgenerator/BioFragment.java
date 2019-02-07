package com.plotgen.rramirez.plotgenerator;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;
import com.plotgen.rramirez.plotgenerator.Guides.GuideListFragment;
import com.plotgen.rramirez.plotgenerator.Model.Character;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioFragment extends Fragment {

    TextView intro_tv, char_role_challenge, character_bio_challenge;
    ArrayList<String> char_description;
//    ImageButton guide_btn, character_bio_challenge_btn;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String fragmentTag = BioFragment.class.getSimpleName();
    String char_name;
    String project_name;

    @BindView(R.id.fab_add_challenge)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddChallenge;
    @BindView(R.id.fab_guide)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabCheckGuide;
    @BindView(R.id.character_bio_name_title)
    TextView title;
    @BindView(R.id.character_bio_role_title)
    TextView role_subtitle;

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

        //Bind the elements
        intro_tv = myFragmentView.findViewById(R.id.character_bio_intro);
        character_bio_challenge = myFragmentView.findViewById(R.id.character_bio_challenge);
        char_role_challenge = myFragmentView.findViewById(R.id.char_role_challenge);
        char_description = new ArrayList<String>();


        try{
            char_name = Common.currentCharacter.getName();
            Log.v("matilda", "Name is: " + Common.currentCharacter.getName());
            Log.v("matilda", "Challenges done are: " + Common.currentCharacter.getChallengesDone());
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }


        ((MainActivity) getActivity()).setActionBarTitle(char_name);
        setHasOptionsMenu(true);

        char_description = getDescription(myFragmentView.getContext(), char_name);
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
            final String role = char_description.get(10);
            String moment = char_description.get(11);
            String need = char_description.get(12);
            String phrase = char_description.get(13);
            final String trait1 = char_description.get(14);
            String trait2 = char_description.get(15);
            String trait3 = char_description.get(16);
            String notes = char_description.get(17); //Always update notes to the last index. Because of reasons.
            //First challenge
            String firstReaction = char_description.get(18);
            String waitRescue = char_description.get(19);
            String helpPartner = char_description.get(20);
            String escapeFirst = char_description.get(21);
            //Second challenge
            String challenge2_q1 = char_description.get(22);
            String challenge2_q2 = char_description.get(23);
            String challenge2_q3 = char_description.get(24);
            String challenge2_q4 = char_description.get(25);
            //Third challenge
            String challenge3_q1 = char_description.get(26);
            String challenge3_q2 = char_description.get(27);
            String challenge3_q3 = char_description.get(28);
            String challenge3_q4 = char_description.get(29);
            //Fourth challenge
            String challenge4_q1 = char_description.get(30);
            String challenge4_q2 = char_description.get(31);
            String challenge4_q3 = char_description.get(32);
            String challenge4_q4 = char_description.get(33);
            //Fifth challenge
            String challenge5_q1 = char_description.get(34);
            String challenge5_q2 = char_description.get(35);
            String challenge5_q3 = char_description.get(36);
            String challenge5_q4 = char_description.get(37);
            //Sixth challenge
            String challenge6_q1 = char_description.get(38);
            String challenge6_q2 = char_description.get(39);
            String challenge6_q3 = char_description.get(40);
            String challenge6_q4 = char_description.get(41);
            //Mentor challenge
            String c1_mentor_q1 = char_description.get(42);
            String c1_mentor_q2 = char_description.get(43);
            String c1_mentor_q3 = char_description.get(44);
            String c1_mentor_q4 = char_description.get(45);
            //Antagonist challenge
            String c1_antagonist_q1 = char_description.get(46);
            String c1_antagonist_q2 = char_description.get(47);
            String c1_antagonist_q3 = char_description.get(48);
            String c1_antagonist_q4 = char_description.get(49);
            //Sidekick challenge
            String c1_sidekick_q1 = char_description.get(50);
            String c1_sidekick_q2 = char_description.get(51);
            String c1_sidekick_q3 = char_description.get(52);
            String c1_sidekick_q4 = char_description.get(53);
            //Seventh challenge
            String challenge7_q1 = char_description.get(54);
            String challenge7_q2 = char_description.get(55);
            String challenge7_q3 = char_description.get(56);
            String challenge7_q4 = char_description.get(57);
            //Eight challenge
            String challenge8_q1 = char_description.get(58);
            String challenge8_q2 = char_description.get(59);
            String challenge8_q3 = char_description.get(60);
            String challenge8_q4 = char_description.get(61);


            Common.currentCharacter.setGender(gender);
            Common.currentCharacter.setRole(role);

            //Titles
            title.setText(char_name);
            role_subtitle.setText(role);

            StringBuffer bio_text = new StringBuffer();
            bio_text.append(name + " " + getString(R.string.age_bio_1) + "<br> " + age + " " + getString(R.string.age_bio_2) + "<br>");
            bio_text.append(getString(R.string.placebirth_bio) + "<br> " + placebirth + "<br>");
            if(job.equals("Arbeitslos") || job.equals("Desempleado") || job.equals("Desempleada") || job.equals("Unemployed")){
                bio_text.append(getString(R.string.nojob_bio) + " " + job + "<br>");
            } else {
                bio_text.append(getString(R.string.job_bio) + " " + job + "<br>");
            }
            bio_text.append(getString(R.string.height_bio) + " " + height + "<br>");
            bio_text.append(getString(R.string.hair_bio_1) + " " + haircolor + " " + getString(R.string.hair_bio_2));
            bio_text.append(" " + getString(R.string.eyes_bio) + " " + eyecolor + "<br>");
            bio_text.append(getString(R.string.bodybuild_bio) + " " + bodybuild + "<br>");


            if (gender.equals("Masculino") || gender.equals("Male")) {
                bio_text.append(getString(R.string.male_desire_bio) + " " + desire + "<br>");
                bio_text.append(getString(R.string.male_need_bio) + " " + need + "?<br>");
                bio_text.append(getString(R.string.male_moment_bio) + " " + moment + "<br>");
                bio_text.append(getString(R.string.male_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
                bio_text.append(getString(R.string.male_phrase_bio) + "<br> " + phrase + "<br><br>");
            } else if (gender.equals("Femenino") || gender.equals("Female")) {
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

            bio_text.append(getString(R.string.notes_bio) + "<br> " + notes);
            intro_tv.setText(Html.fromHtml(bio_text.toString()));


            switch (role) {
                case "Mentor":
                    if (c1_mentor_q1 != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("<b>" + getString(R.string.c1_mentor_bio_title) + "</b>");
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_1) + " " + char_name + "?" + "</b></i><br> " + c1_mentor_q1);
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_2) + "</b></i><br> " + c1_mentor_q2);
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_3) + "</b></i><br> " + c1_mentor_q3);
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_4) + "</b></i><br> " + c1_mentor_q4);
                        char_role_challenge.setVisibility(View.VISIBLE);
                        char_role_challenge.setText(Html.fromHtml(sb.toString()));
                    }
                    break;
                case "Antagonist":
                case "Antagonista":
                    if (c1_antagonist_q1 != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("<b>" + getString(R.string.c1_antagonist_bio_title) + "</b>");
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_1) + "</b></i><br> " + c1_antagonist_q1);
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_2) + "</b></i><br> " + c1_antagonist_q2);
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_3) + "</b></i><br> " + c1_antagonist_q3);
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_4) + "</b></i><br> " + c1_antagonist_q4);
                        char_role_challenge.setVisibility(View.VISIBLE);
                        char_role_challenge.setText(Html.fromHtml(sb.toString()));
                    }
                    break;
                case "Sidekick":
                case "Escudero":
                    if (c1_sidekick_q1 != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("<b>" + getString(R.string.c1_sidekick_bio_title) + "</b>");
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_1) + "</b></i><br> " + c1_sidekick_q1);
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_2) + "</b></i><br> " + c1_sidekick_q2);
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_3) + "</b></i><br> " + c1_sidekick_q3);
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_4) + "</b></i><br> " + c1_sidekick_q4);
                        char_role_challenge.setVisibility(View.VISIBLE);
                        char_role_challenge.setText(Html.fromHtml(sb.toString()));
                    }
                    break;
            }

            StringBuffer sb = new StringBuffer();
            if (firstReaction != null) {
                sb.append("<b>" + getString(R.string.challenge_1_bio_title) + "</b>");
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_1) + "</b></i><br> " + firstReaction);
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_2) + "</b></i><br> " + waitRescue);
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_3) + "</b></i><br> " + helpPartner);
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_4) + "</b></i><br> " + escapeFirst);
                sb.append("<br><br>");
            }
            if (challenge2_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_2_bio_title) + "</b> " + getString(R.string.challenge_2_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_1) + "</b></i><br>" + challenge2_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_2) + "</b></i><br>" + challenge2_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_3) + "</b></i><br>" + challenge2_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_4) + "</b></i><br>" + challenge2_q4);
                sb.append("<br><br>");
            }
            if (challenge3_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_3_bio_title) + "</b> " + getString(R.string.challenge_3_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_1) + "</b></i><br>" + challenge3_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_2) + "</b></i><br>" + challenge3_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_3) + "</b></i><br>" + challenge3_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_4) + "</b></i><br>" + challenge3_q4);
                sb.append("<br><br>");
            }
            if (challenge4_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_4_bio_title) + "</b> " + getString(R.string.challenge_4_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_1) + "</b></i><br>" + challenge4_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_2) + "</b></i><br>" + challenge4_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_3) + "</b></i><br>" + challenge4_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_4) + "</b></i><br>" + challenge4_q4);
                sb.append("<br><br>");
            }

            if (challenge5_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_5_bio_title) + "</b> " + getString(R.string.challenge_5_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_1) + "</b></i><br>" + challenge5_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_2) + "</b></i><br>" + challenge5_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_3) + "</b></i><br>" + challenge5_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_4) + "</b></i><br>" + challenge5_q4);
                sb.append("<br><br>");
            }
            if (challenge6_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_6_bio_title) + "</b> " + getString(R.string.challenge_6_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_1) + "</b></i><br>" + challenge6_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_2) + "</b></i><br>" + challenge6_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_3) + "</b></i><br>" + challenge6_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_4) + "</b></i><br>" + challenge6_q4);
                sb.append("<br><br>");
            }

            if (challenge7_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_7_bio_title) + "</b> " + getString(R.string.challenge_7_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_1) + "</b></i><br>" + challenge7_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_2) + "</b></i><br>" + challenge7_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_3) + "</b></i><br>" + challenge7_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_4) + "</b></i><br>" + challenge7_q4);
                sb.append("<br><br>");
            }if (challenge8_q1 != null) {
                sb.append("<br><b>" + getString(R.string.challenge_8_bio_title) + "</b> " + getString(R.string.challenge_8_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_1) + "</b></i><br>" + challenge8_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_2) + "</b></i><br>" + challenge8_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_3) + "</b></i><br>" + challenge8_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_4) + "</b></i><br>" + challenge8_q4);
                sb.append("<br><br>");
            }
            character_bio_challenge.setVisibility(View.VISIBLE);
            character_bio_challenge.setText(Html.fromHtml(sb.toString()));

            fabCheckGuide.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    GuideListFragment nextFragment = new GuideListFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                    if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                        transaction.addToBackStack(null);
                    }
                    Utils.changeFragment(nextFragment, transaction);
                }
            });

            fabAddChallenge.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        ChallengeListFragment nextFragment = new ChallengeListFragment();
                        //Make the transaction
                        FragmentTransaction transaction = getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down);
                        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                        if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                            transaction.addToBackStack(null);
                        }
                        transaction.replace(R.id.flMain, nextFragment);
                        transaction.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }


        if(Common.onBoarding == 6){
            Common.onBoarding = 7;
            Utils.displayDialog(myFragmentView.getContext(), getString(R.string.onBoardingTitle_7), getString(R.string.onBoarding_7), "Got it!");
        }


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


    public ArrayList<String> getDescription(Context context, String char_name) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE name = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{char_name});
        Log.e("cursor count",cursor.getCount()+"");
        ArrayList<String> char_list = new ArrayList<String>();
        // changes done to check is cursor size is greator than 0
        if(cursor!=null && cursor.getCount()>0) {
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
                cursor.moveToNext();
            }
        }
        cursor.close();
        return char_list;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bio, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_bio_edit) {
            //Send it to the next fragment
            try {
                Common.charCreationMode = false;
                //Send it to the next fragment
                CharacterFragment nextFragment = new CharacterFragment();
                //Make the transaction
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        else if (id == R.id.menu_bio_share){
            try {
                String allbody = intro_tv.getText().toString() + " \n" + character_bio_challenge.getText().toString() + " \n";
                String char_role = project_name + ": " + char_name;
                SHARE(getView(), allbody, char_role);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
