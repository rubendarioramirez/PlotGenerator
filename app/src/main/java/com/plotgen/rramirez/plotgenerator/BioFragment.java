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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioFragment extends Fragment {

    TextView title, role_subtitle, intro_tv,char_role_challenge,character_bio_challenge, character_bio_challenge_2,character_bio_challenge_3,character_bio_challenge_4,character_bio_challenge_5;
    ArrayList<String> char_description;
    ImageButton character_bio_edit_btn, character_bio_share_btn, guide_btn,character_bio_challenge_btn;
    private FirebaseAnalytics mFirebaseAnalytics;


    public BioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final String char_name = this.getArguments().getString("char_name");
        final String project_name = this.getArguments().getString("project_name");

        final View myFragmentView = inflater.inflate(R.layout.fragment_bio, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_list_tab));
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());

        //Bind the elements
        title = myFragmentView.findViewById(R.id.character_bio_name_title);
        role_subtitle=myFragmentView.findViewById(R.id.character_bio_role_title);
        intro_tv = myFragmentView.findViewById(R.id.character_bio_intro);
        character_bio_challenge = myFragmentView.findViewById(R.id.character_bio_challenge);
        character_bio_challenge_2 = myFragmentView.findViewById(R.id.character_bio_challenge_2);
        character_bio_challenge_3 = myFragmentView.findViewById(R.id.character_bio_challenge_3);
        character_bio_challenge_4 = myFragmentView.findViewById(R.id.character_bio_challenge_4);
        character_bio_challenge_5 = myFragmentView.findViewById(R.id.character_bio_challenge_5);
        character_bio_edit_btn = myFragmentView.findViewById(R.id.character_bio_edit_btn);
        character_bio_share_btn = myFragmentView.findViewById(R.id.character_bio_share_btn);
        character_bio_challenge_btn = myFragmentView.findViewById(R.id.bio_fragment_challenge_btn);
        char_role_challenge = myFragmentView.findViewById(R.id.char_role_challenge);
        guide_btn = myFragmentView.findViewById(R.id.guide_btn);


        //Set the narrative
        char_description = getDescription(myFragmentView.getContext(), char_name.toString());
        String name = char_description.get(0);
        String age = char_description.get(1);
        String gender = char_description.get(2);
        String placebirth = char_description.get(3);
        String job = char_description.get(4);
        String desire = char_description.get(5);
        final String role = char_description.get(6);
        String moment = char_description.get(7);
        String need = char_description.get(8);
        String phrase = char_description.get(9);
        final String trait1 = char_description.get(10);
        String trait2 = char_description.get(11);
        String trait3 = char_description.get(12);
        String notes = char_description.get(13); //Always update notes to the last index. Because of reasons.
        //First challenge
        String firstReaction = char_description.get(14);
        String waitRescue = char_description.get(15);
        String helpPartner = char_description.get(16);
        String escapeFirst = char_description.get(17);
        //Second challenge
        String challenge2_q1 = char_description.get(18);
        String challenge2_q2 = char_description.get(19);
        String challenge2_q3 = char_description.get(20);
        String challenge2_q4 = char_description.get(21);
        //Third challenge
        String challenge3_q1 = char_description.get(22);
        String challenge3_q2 = char_description.get(23);
        String challenge3_q3 = char_description.get(24);
        String challenge3_q4 = char_description.get(25);
        //Fourth challenge
        String challenge4_q1 = char_description.get(26);
        String challenge4_q2 = char_description.get(27);
        String challenge4_q3 = char_description.get(28);
        String challenge4_q4 = char_description.get(29);
        //Fifth challenge
        String challenge5_q1 = char_description.get(30);
        String challenge5_q2 = char_description.get(31);
        String challenge5_q3 = char_description.get(32);
        String challenge5_q4 = char_description.get(33);
        //Mentor challenge
        String c1_mentor_q1 = char_description.get(34);
        String c1_mentor_q2 = char_description.get(35);
        String c1_mentor_q3 = char_description.get(36);
        String c1_mentor_q4 = char_description.get(37);
        //Antagonist challenge
        String c1_antagonist_q1 = char_description.get(38);
        String c1_antagonist_q2 = char_description.get(39);
        String c1_antagonist_q3 = char_description.get(40);
        String c1_antagonist_q4 = char_description.get(41);
        //Sidekick challenge
        String c1_sidekick_q1 = char_description.get(42);
        String c1_sidekick_q2 = char_description.get(43);
        String c1_sidekick_q3 = char_description.get(44);
        String c1_sidekick_q4 = char_description.get(45);


        //Titles
        title.setText(char_name.toString() );
        role_subtitle.setText(role.toString());

        //Remove challenges textView if they're empty
        character_bio_challenge_2.setVisibility(View.INVISIBLE);
        character_bio_challenge_3.setVisibility(View.INVISIBLE);
        character_bio_challenge_4.setVisibility(View.INVISIBLE);

        StringBuffer bio_text=new StringBuffer();
        bio_text.append(name + " " + getString(R.string.age_bio_1) + "<br> " + age + " " + getString(R.string.age_bio_2) + "<br>");
        bio_text.append(getString(R.string.placebirth_bio) +"<br> " + placebirth+"<br>");
        bio_text.append(getString(R.string.job_bio) + " " + job + "<br>");

        if(gender.equals("Masculino") || gender.equals("Male")) {
            bio_text.append(getString(R.string.male_desire_bio) + " " + desire + "<br>");
            bio_text.append(getString(R.string.male_need_bio) + " " + need + "?<br>");
            bio_text.append(getString(R.string.male_moment_bio) + " " + moment + "<br>");
            bio_text.append(getString(R.string.male_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
            bio_text.append(getString(R.string.male_phrase_bio) +"<br> " +  phrase +"<br><br>");
        } else {
            bio_text.append(getString(R.string.female_desire_bio) + " " + desire + "<br>");
            bio_text.append(getString(R.string.female_need_bio) + " " + need + "?<br>");
            bio_text.append(getString(R.string.female_moment_bio) + " " + moment + "<br>");
            bio_text.append(getString(R.string.female_trait_bio) + " " + trait1 + ", " + trait2 + ", " + trait3 + "<br><br>");
            bio_text.append(getString(R.string.female_phrase_bio) +"<br> " +  phrase +"<br><br>");
        }

        bio_text.append(getString(R.string.notes_bio) +"<br> " +  notes );
        intro_tv.setText(Html.fromHtml(bio_text.toString()));


        switch (role){
            case "Mentor":
                if(c1_mentor_q1 != null) {
                    StringBuffer sb=new StringBuffer();
                    sb.append("<b>" + getString(R.string.c1_mentor_bio_title) + "</b>");
                    sb.append("<br><i><b>" + getString(R.string.c1_mentor_bio_desc_1) + " " + char_name + "?" + "</b></i><br> " + c1_mentor_q1);
                    sb.append("<br><i><b>"+ getString(R.string.c1_mentor_bio_desc_2) +"</b></i><br> " + c1_mentor_q2);
                    sb.append("<br><i><b>"+ getString(R.string.c1_mentor_bio_desc_3) +"</b></i><br> " + c1_mentor_q3);
                    sb.append("<br><i><b>"+ getString(R.string.c1_mentor_bio_desc_4) +"</b></i><br> " + c1_mentor_q4);
                    char_role_challenge.setVisibility(View.VISIBLE);
                    char_role_challenge.setText(Html.fromHtml(sb.toString()));
                }
                break;
            case "Antagonist":
            case "Antagonista":
                if(c1_antagonist_q1 != null) {
                    StringBuffer sb=new StringBuffer();
                    sb.append("<b>" + getString(R.string.c1_antagonist_bio_title) + "</b>");
                    sb.append("<br><i><b>" + getString(R.string.c1_antagonist_bio_desc_1) + "</b></i><br> " + c1_antagonist_q1);
                    sb.append("<br><i><b>"+ getString(R.string.c1_antagonist_bio_desc_2) +"</b></i><br> " + c1_antagonist_q2);
                    sb.append("<br><i><b>"+ getString(R.string.c1_antagonist_bio_desc_3) +"</b></i><br> " + c1_antagonist_q3);
                    sb.append("<br><i><b>"+ getString(R.string.c1_antagonist_bio_desc_4) +"</b></i><br> " + c1_antagonist_q4);
                    char_role_challenge.setVisibility(View.VISIBLE);
                    char_role_challenge.setText(Html.fromHtml(sb.toString()));
                }
                break;
            case "Sidekick":
            case "Escudero":
                if(c1_sidekick_q1 != null) {
                    StringBuffer sb=new StringBuffer();
                    sb.append("<b>" + getString(R.string.c1_sidekick_bio_title) + "</b>");
                    sb.append("<br><i><b>" + getString(R.string.c1_sidekick_bio_desc_1) + "</b></i><br> " + c1_sidekick_q1);
                    sb.append("<br><i><b>"+ getString(R.string.c1_sidekick_bio_desc_2) +"</b></i><br> " + c1_sidekick_q2);
                    sb.append("<br><i><b>"+ getString(R.string.c1_sidekick_bio_desc_3) +"</b></i><br> " + c1_sidekick_q3);
                    sb.append("<br><i><b>"+ getString(R.string.c1_sidekick_bio_desc_4) +"</b></i><br> " + c1_sidekick_q4);
                    char_role_challenge.setVisibility(View.VISIBLE);
                    char_role_challenge.setText(Html.fromHtml(sb.toString()));
                }
                break;
        }

        if(firstReaction != null) {
            StringBuffer sb=new StringBuffer();
            sb.append("<b>" + getString(R.string.challenge_1_bio_title) + "</b>");
            sb.append("<br><i><b>" + getString(R.string.challenge_1_bio_desc_1) + "</b></i><br> " + firstReaction);
            sb.append("<br><i><b>"+ getString(R.string.challenge_1_bio_desc_2) +"</b></i><br> " + waitRescue);
            sb.append("<br><i><b>"+ getString(R.string.challenge_1_bio_desc_3) +"</b></i><br> " + helpPartner);
            sb.append("<br><i><b>"+ getString(R.string.challenge_1_bio_desc_4) +"</b></i><br> " + escapeFirst);
            character_bio_challenge.setText(Html.fromHtml(sb.toString()));
        }
        if(challenge2_q1 !=null){
            StringBuffer sb=new StringBuffer();
            sb.append("<br><b>"+getString(R.string.challenge_2_bio_title) +"</b> "+getString(R.string.challenge_2_bio_subtitle) +" <br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_1) +"</b></i><br>" + challenge2_q1 + "<br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_2_bio_desc_2) + "</b></i><br>" + challenge2_q2+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_2_bio_desc_3) +"</b></i><br>" + challenge2_q3+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_2_bio_desc_4) +"</b></i><br>" + challenge2_q4);
            character_bio_challenge_2.setVisibility(View.VISIBLE);
            character_bio_challenge_2.setText(Html.fromHtml(sb.toString()));
        }
        if(challenge3_q1 !=null){
            StringBuffer sb=new StringBuffer();
            sb.append("<br><b>"+getString(R.string.challenge_3_bio_title) +"</b> "+getString(R.string.challenge_3_bio_subtitle) +" <br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_1) +"</b></i><br>" + challenge3_q1 + "<br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_3_bio_desc_2) + "</b></i><br>" + challenge3_q2+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_3_bio_desc_3) +"</b></i><br>" + challenge3_q3+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_3_bio_desc_4) +"</b></i><br>" + challenge3_q4);
            character_bio_challenge_3.setVisibility(View.VISIBLE);
            character_bio_challenge_3.setText(Html.fromHtml(sb.toString()));
        }
        if(challenge4_q1 !=null){
            StringBuffer sb=new StringBuffer();
            sb.append("<br><b>"+getString(R.string.challenge_4_bio_title) +"</b> "+getString(R.string.challenge_4_bio_subtitle) +" <br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_1) +"</b></i><br>" + challenge4_q1 + "<br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_4_bio_desc_2) + "</b></i><br>" + challenge4_q2+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_4_bio_desc_3) +"</b></i><br>" + challenge4_q3+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_4_bio_desc_4) +"</b></i><br>" + challenge4_q4);
            sb.append("<br><br><br>");
            character_bio_challenge_4.setVisibility(View.VISIBLE);
            character_bio_challenge_4.setText(Html.fromHtml(sb.toString()));
        }
        if(challenge5_q1 !=null){
            StringBuffer sb=new StringBuffer();
            sb.append("<br><b>"+getString(R.string.challenge_5_bio_title) +"</b> "+getString(R.string.challenge_5_bio_subtitle) +" <br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_1) +"</b></i><br>" + challenge5_q1 + "<br>");
            sb.append("<br><i><b>" + getString(R.string.challenge_5_bio_desc_2) + "</b></i><br>" + challenge5_q2+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_5_bio_desc_3) +"</b></i><br>" + challenge5_q3+ "<br>");
            sb.append("<br><i><b>"+ getString(R.string.challenge_5_bio_desc_4) +"</b></i><br>" + challenge5_q4);
            sb.append("<br><br><br>");
            character_bio_challenge_5.setVisibility(View.VISIBLE);
            character_bio_challenge_5.setText(Html.fromHtml(sb.toString()));
        }


        character_bio_edit_btn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("char_name",char_name);
                    bundle.putString("project_name",project_name);

                    //Send it to the next fragment
                    CharacterFragment nextFragment = new CharacterFragment();
                    nextFragment.setArguments(bundle);
                    //Make the transaction
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations( R.anim.slide_up, 0, 0, R.anim.slide_down);
                    transaction.add(R.id.flMain,nextFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        guide_btn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                GuideListFragment nextFragment = new GuideListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction,"","");
            }
        });

        character_bio_challenge_btn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("char_name",char_name);
                    bundle.putString("project_name",project_name);
                    bundle.putString("role",role);
                    //Send it to the next fragment
                    ChallengeListFragment nextFragment = new ChallengeListFragment();
                    nextFragment.setArguments(bundle);
                    //Make the transaction
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations( R.anim.slide_up, 0, 0, R.anim.slide_down);
                    transaction.add(R.id.flMain,nextFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        character_bio_share_btn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    String allbody = intro_tv.getText().toString() + " \n" +  character_bio_challenge.getText().toString() + " \n" + character_bio_challenge_2.getText().toString() + " \n" + character_bio_challenge_3.getText().toString() + " \n" + character_bio_challenge_4.getText().toString()+ " \n" + character_bio_challenge_5.getText().toString();
                    String char_role = project_name + ": " + char_name + " - " + role;
                    SHARE(myFragmentView,allbody, char_role);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return myFragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_list_tab));
    }

    public void SHARE(View view, String body, String char_name) {

        // Do something in response to button
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, char_name);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent,  "share"));

        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("Share", "completed");
        mFirebaseAnalytics.logEvent("share_completed",params);


    }


    public ArrayList<String> getDescription(Context context, String char_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE name = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{char_name});
        cursor.moveToFirst();
        ArrayList<String> char_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            char_list.add(cursor.getString(cursor.getColumnIndex("name")));
            char_list.add(cursor.getString(cursor.getColumnIndex("age")));
            char_list.add(cursor.getString(cursor.getColumnIndex("gender")));
            char_list.add(cursor.getString(cursor.getColumnIndex("placebirth")));
            char_list.add(cursor.getString(cursor.getColumnIndex("profession")));
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

            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
    }


}
