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
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;
import com.plotgen.rramirez.plotgenerator.Guides.GuideListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioChallengesFragment extends Fragment {

//    TextView char_role_challenge;
    ArrayList<String> char_description;
//    ImageButton guide_btn, character_bio_challenge_btn;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String fragmentTag = BioChallengesFragment.class.getSimpleName();
    String char_name, charID, project_name, charRole;

    @BindView(R.id.biochallenge_body)
    TextView biochallenge_body;
//    @BindView(R.id.biochallenge_title)
//    TextView title;
//    @BindView(R.id.biochallenge_subtitle)
//    TextView subtitle;
    @BindView(R.id.biochallenge_role)
    TextView biochallenge_role;

    public BioChallengesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myFragmentView = inflater.inflate(R.layout.fragment_bio_challenges, container, false);
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

        char_description = getDescription(myFragmentView.getContext());
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

            //Titles
//            title.setText(char_name);

            switch (role) {
                case "Mentor":
                    if (c1_mentor_q1 != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("<b><font color='red'>" + getString(R.string.c1_mentor_bio_title) + "</font></b>");
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_1) + " " + char_name + "?" + "</b></i><br> " + c1_mentor_q1);
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_2) + "</b></i><br> " + c1_mentor_q2);
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_3) + "</b></i><br> " + c1_mentor_q3);
                        sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_4) + "</b></i><br> " + c1_mentor_q4);
                        biochallenge_role.setVisibility(View.VISIBLE);
                        biochallenge_role.setText(Html.fromHtml(sb.toString()));
                    }
                    break;
                case "Antagonist":
                case "Antagonista":
                    if (c1_antagonist_q1 != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("<b><font color='red'>" + getString(R.string.c1_antagonist_bio_title) + "</font></b>");
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_1) + "</b></i><br> " + c1_antagonist_q1);
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_2) + "</b></i><br> " + c1_antagonist_q2);
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_3) + "</b></i><br> " + c1_antagonist_q3);
                        sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_4) + "</b></i><br> " + c1_antagonist_q4);
                        biochallenge_role.setVisibility(View.VISIBLE);
                        biochallenge_role.setText(Html.fromHtml(sb.toString()));
                    }
                    break;
                case "Sidekick":
                case "Escudero":
                    if (c1_sidekick_q1 != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("<b><font color='red'>" + getString(R.string.c1_sidekick_bio_title) + "</font></b>");
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_1) + "</b></i><br> " + c1_sidekick_q1);
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_2) + "</b></i><br> " + c1_sidekick_q2);
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_3) + "</b></i><br> " + c1_sidekick_q3);
                        sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_4) + "</b></i><br> " + c1_sidekick_q4);
                        biochallenge_role.setVisibility(View.VISIBLE);
                        biochallenge_role.setText(Html.fromHtml(sb.toString()));
                    }
                    break;
            }

            StringBuffer sb = new StringBuffer();
            if (firstReaction != null) {
                sb.append("<b><font color='red'>" + getString(R.string.challenge_1_bio_title) + "</font></b>");
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_1) + "</b></i><br> " + firstReaction);
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_2) + "</b></i><br> " + waitRescue);
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_3) + "</b></i><br> " + helpPartner);
                sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_4) + "</b></i><br> " + escapeFirst);
                sb.append("<br><br>");
            }
            if (challenge2_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_2_bio_title) + "</font></b> " + getString(R.string.challenge_2_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_1) + "</b></i><br>" + challenge2_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_2) + "</b></i><br>" + challenge2_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_3) + "</b></i><br>" + challenge2_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_4) + "</b></i><br>" + challenge2_q4);
                sb.append("<br><br>");
            }
            if (challenge3_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_3_bio_title) + "</font></b> " + getString(R.string.challenge_3_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_1) + "</b></i><br>" + challenge3_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_2) + "</b></i><br>" + challenge3_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_3) + "</b></i><br>" + challenge3_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_4) + "</b></i><br>" + challenge3_q4);
                sb.append("<br><br>");
            }
            if (challenge4_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_4_bio_title) + "</font></b> " + getString(R.string.challenge_4_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_1) + "</b></i><br>" + challenge4_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_2) + "</b></i><br>" + challenge4_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_3) + "</b></i><br>" + challenge4_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_4) + "</b></i><br>" + challenge4_q4);
                sb.append("<br><br>");
            }

            if (challenge5_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_5_bio_title) + "</font></b> " + getString(R.string.challenge_5_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_1) + "</b></i><br>" + challenge5_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_2) + "</b></i><br>" + challenge5_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_3) + "</b></i><br>" + challenge5_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_4) + "</b></i><br>" + challenge5_q4);
                sb.append("<br><br>");
            }
            if (challenge6_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_6_bio_title) + "</font></b> " + getString(R.string.challenge_6_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_1) + "</b></i><br>" + challenge6_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_2) + "</b></i><br>" + challenge6_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_3) + "</b></i><br>" + challenge6_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_6_bio_desc_4) + "</b></i><br>" + challenge6_q4);
                sb.append("<br><br>");
            }

            if (challenge7_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_7_bio_title) + "</font></b> " + getString(R.string.challenge_7_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_1) + "</b></i><br>" + challenge7_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_2) + "</b></i><br>" + challenge7_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_3) + "</b></i><br>" + challenge7_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_7_bio_desc_4) + "</b></i><br>" + challenge7_q4);
                sb.append("<br><br>");
            }if (challenge8_q1 != null) {
                sb.append("<br><b><font color='red'>" + getString(R.string.challenge_8_bio_title) + "</font></b> " + getString(R.string.challenge_8_bio_subtitle) + " <br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_1) + "</b></i><br>" + challenge8_q1 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_2) + "</b></i><br>" + challenge8_q2 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_3) + "</b></i><br>" + challenge8_q3 + "<br>");
                sb.append("<br><i><b>" + getString(R.string.challenge_8_bio_desc_4) + "</b></i><br>" + challenge8_q4);
                sb.append("<br><br>");
            }
            biochallenge_body.setVisibility(View.VISIBLE);

            if(Html.fromHtml(sb.toString()).toString().equals("")){
                biochallenge_body.setText(getString(R.string.biocontainer_challenge_empty));
            } else {
                biochallenge_body.setText(Html.fromHtml(sb.toString()));
            }
        }

        return myFragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("");
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

}
