package com.plotgen.rramirez.plotgenerator;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioFragment extends Fragment {

    TextView title, intro_tv,character_bio_challenge;
    ArrayList<String> char_description;
    ImageButton character_bio_edit_btn;
    Button character_bio_challenge_btn;
    String gender_article, gender_article_posesive;

    public BioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final String char_name = this.getArguments().getString("char_name");
        final String project_name = this.getArguments().getString("project_name");

        View myFragmentView = inflater.inflate(R.layout.fragment_bio, container, false);

        //Bind the elements
        title = myFragmentView.findViewById(R.id.character_bio_name_title);
        intro_tv = myFragmentView.findViewById(R.id.character_bio_intro);
        character_bio_challenge = myFragmentView.findViewById(R.id.character_bio_challenge);
        character_bio_edit_btn = myFragmentView.findViewById(R.id.character_bio_edit_btn);
        character_bio_challenge_btn = myFragmentView.findViewById(R.id.bio_fragment_challenge_btn);

        //Set title
        title.setText(char_name.toString());
        //Set the narrative

        char_description = getDescription(myFragmentView.getContext(), char_name.toString());
        String name = char_description.get(0);
        String age = char_description.get(1);
        String gender = char_description.get(2);
        String placebirth = char_description.get(3);
        String job = char_description.get(4);
        String desire = char_description.get(5);
        String moment = char_description.get(7);
        String need = char_description.get(8);
        String trait1 = char_description.get(9);
        String trait2 = char_description.get(10);
        String trait3 = char_description.get(11);
        String firstReaction = char_description.get(12);
        String waitRescue = char_description.get(13);
        String helpPartner = char_description.get(14);
        String escapeFirst = char_description.get(15);


        //Get device lang
        if (Locale.getDefault().getLanguage()=="es"){
            intro_tv.setText(name + " tiene " + age + " años.\nNacio en " + placebirth +".\nTrabaja de " + job +
                    "\n\nEn su mente desea " + desire + "\nPero es eso lo que necesita? o en realidad lo que necesita es " + need +
                    "?\n\n De su infancia sabemos que " + moment +
                    "\n\nSus amigos dicen que es " + trait1 + ", " + trait2 + " y " + trait3  );

            if(firstReaction != null) {
                character_bio_challenge.setText("Sobre el desafio del elevador podemos decir que:\n\n"
                        + "Cual seria su primera reaccion de quedarse atrapado? " + firstReaction +
                        "\n\nEsperaria a ser rescatado? " + waitRescue +
                        "\n\nSi la persona que esta con el entrara en panico. Lo ayudaria? " + helpPartner
                        + "\n\nFinalmente, si solo uno pudiese escapar, quien iria primero? " + escapeFirst);
            }
        }
        else {
            //Get gender article
            if(gender.equals("Masculino") || gender.equals("Male")) {
                gender_article = "he";
                gender_article_posesive = "his";

            } else{
                gender_article = "she";
                gender_article_posesive = "her";
            }

            intro_tv.setText(name + " is " + age + " years old. \nBorn in " + placebirth +".\n  Works as " + job + ".\n\n " + "In " + gender_article_posesive + " mind " + gender_article + " wants to " + desire +
                    "\nBut is that what " + gender_article + " really wants? or " + gender_article + " actually needs to " + need + "...\n\n  About " +gender_article_posesive+" childhood we know that "+ gender_article + " " + moment
                    + "\n\nAlso " +  gender_article_posesive  + " friends says that " + gender_article + " is " + trait1 + ", " + trait2 + " and " + trait3);

            if(firstReaction != null) {
                character_bio_challenge.setText("About the elevator challenge we could say that:\n"
                        + "What will be " + gender_article_posesive + " first reacion if gets stuck in a elevator? " + firstReaction +
                        "\n\nWill " + gender_article + " wait to be rescued? " + waitRescue +
                        "\n\nIf the person next to " + gender_article_posesive + " has a panic attack, what will happen? " + helpPartner
                        + "\n\nLastly in the eventuality that just one can escape and the other should wait, what will " + gender_article + " do? " + escapeFirst);
            }
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
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        character_bio_challenge_btn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("char_name",char_name);
                    bundle.putString("project_name",project_name);
                    //Send it to the next fragment
                    ChallengeListFragment nextFragment = new ChallengeListFragment();
                    nextFragment.setArguments(bundle);
                    //Make the transaction
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return myFragmentView;
    }



    public ArrayList<String> getDescription(Context context, String char_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE name = " + "'" + char_name + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
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
            char_list.add(cursor.getString(cursor.getColumnIndex("trait1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("trait2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("trait3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_initial_reaction")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_wait_rescue")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_help_partner")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_escape_first")));
            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
    }


}
