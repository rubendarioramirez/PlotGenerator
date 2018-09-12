package com.plotgen.rramirez.plotgenerator;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class CharacterFragment extends Fragment {

    public EditText nameEditText, profession_edit_text, desire_edit_text, age_edit_text, placebirth_edit_text, defmoment_edit_text,need_edit_text, trait_edit_text, trait2_edit_text,trait3_edit_text;
    public TextView project_name_tv;
    public Button submit, random_gen_char_btn;
    public Spinner gender_spinner, role_spinner;
    public static boolean rated;
    ArrayList<String> char_description;


    public CharacterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_tab));
        final View myFragmentView = inflater.inflate(R.layout.fragment_character, container, false);

        final String project_name_text = this.getArguments().getString("project_name");
        final String name_text = this.getArguments().getString("char_name");


        //Gender spinner functions
        gender_spinner = myFragmentView.findViewById(R.id.gender_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(),
                R.array.gender_spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender_spinner.setAdapter(adapter);

        //Role spinner functions
        role_spinner = myFragmentView.findViewById(R.id.role_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> role_adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(),
                R.array.char_guide_types_titles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        role_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        role_spinner.setAdapter(role_adapter);



        //Declare all the elements
        project_name_tv = myFragmentView.findViewById(R.id.char_template_title);
        nameEditText = myFragmentView.findViewById(R.id.nameEditText);
        age_edit_text = myFragmentView.findViewById(R.id.age_edit_text);
        profession_edit_text = myFragmentView.findViewById(R.id.profession_edit_text);
        placebirth_edit_text = myFragmentView.findViewById(R.id.placebirth_edit_text);
        defmoment_edit_text= myFragmentView.findViewById(R.id.defmoment_edit_text);
        desire_edit_text = myFragmentView.findViewById(R.id.desire_edit_text);
        need_edit_text= myFragmentView.findViewById(R.id.need_edit_text);
        trait_edit_text= myFragmentView.findViewById(R.id.trait_edit_text);
        trait2_edit_text= myFragmentView.findViewById(R.id.trait_2_edit_text);
        trait3_edit_text= myFragmentView.findViewById(R.id.trait_3_edit_text);
        //Save button action
        submit =  myFragmentView.findViewById(R.id.char_template_submit);
        random_gen_char_btn = myFragmentView.findViewById(R.id.random_gen_char_btn);


        //Set the title
        project_name_tv.setText(project_name_text);


        if (name_text == null){ //If it's in create new character mode
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveToDB();
                }
            });
        } else { //If it's in update mode
            //database getter
            char_description = getDescription(myFragmentView.getContext(), name_text.toString());
            nameEditText.setText(char_description.get(0));
            age_edit_text.setText(char_description.get(1));
            placebirth_edit_text.setText(char_description.get(3));
            profession_edit_text.setText(char_description.get(4));
            desire_edit_text.setText(char_description.get(5));
            defmoment_edit_text.setText(char_description.get(7));
            need_edit_text.setText(char_description.get(8));
            trait_edit_text.setText(char_description.get(9));
            trait2_edit_text.setText(char_description.get(10));
            trait3_edit_text.setText(char_description.get(11));


            //Set the proper spinner value
            String gender = char_description.get(2);
            if(gender.equals("Masculino") || gender.equals("Male")){
                gender_spinner.setSelection(1);
            } else {
                gender_spinner.setSelection(2);
            }

            //Set the proper spinner value
            String role = char_description.get(6);
            if(role.equals("Protagonist") || role.equals("Protagonista")){
                role_spinner.setSelection(0);
            } else if(role.equals("Protagonist's Helper") || role.equals("Ayudante de protagonista")) {
                role_spinner.setSelection(1);
            } else if(role.equals("Sidekick") || role.equals("Escudero")) {
                role_spinner.setSelection(2);
            }else if(role.equals("Guardian") || role.equals("Guardian")) {
                role_spinner.setSelection(3);
            } else if(role.equals("Mentor") || role.equals("Mentor")) {
                role_spinner.setSelection(4);
            }else if(role.equals("Impact") || role.equals("De Impacto")) {
                role_spinner.setSelection(5);
            }else if(role.equals("Antagonist") || role.equals("Antagonista")) {
                role_spinner.setSelection(6);
            }else if(role.equals("Antagonist's Helper") || role.equals("Ayudante de Antagonista")) {
                role_spinner.setSelection(7);
            }else if(role.equals("Skeptic") || role.equals("Esceptico")) {
                role_spinner.setSelection(8);
            }else if(role.equals("Obstacle") || role.equals("Obstaculo")) {
                role_spinner.setSelection(9);
            } else{
                role_spinner.setSelection(10);
            }


            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    updateDB(name_text.toString(), project_name_text.toString());
                }
            });

            if(!rated) {
                showRateDialogForRate(myFragmentView.getContext());
            }
        }

        //Random generation
        random_gen_char_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                ArrayList<String> info = generateBIO(myFragmentView.getContext());

                nameEditText.setText(info.get(0));
                placebirth_edit_text.setText(info.get(1));
                age_edit_text.setText(info.get(2));
                gender_spinner.setSelection(Integer.valueOf(info.get(3)));
                profession_edit_text.setText(info.get(4));
                desire_edit_text.setText(info.get(5));
                defmoment_edit_text.setText(info.get(6));
                trait_edit_text.setText(info.get(7));
                trait2_edit_text.setText(info.get(8));
                trait3_edit_text.setText(info.get(9));
                need_edit_text.setText(info.get(10));


            }
        });




        return myFragmentView;
    }


    private ArrayList<String> generateBIO(Context context){

        ArrayList<String> male_names = new ArrayList<String>(100);
        ArrayList<String> female_names = new ArrayList<String>(100);
        ArrayList<String> last_name = new ArrayList<String>(100);
        ArrayList<String> placeOfBirth = new ArrayList<String>(100);
        ArrayList<String> profession = new ArrayList<String>(100);
        ArrayList<String> desire = new ArrayList<String>();
        ArrayList<String> moment = new ArrayList<String>();
        ArrayList<String> traits = new ArrayList<String>();
        ArrayList<String> needs = new ArrayList<String>();
        ArrayList<String> bio = new ArrayList<String>();
        String selectedName;


        male_names.addAll(Arrays.asList("James", "John", "Robert", "Michael", "William", "David", "Richard","Joseph", "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew", "Kenneth", "George", "Joshua", "Kevin", "Brian", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob", "Gary", "Nicholas", "Eric", "Stephen", "Jonathan", "Larry","Justin", "Scott", "Brandon","Frank", "Benjamin","Gregory" ,"Raymond" ,"Samuel", "Patrick", "Alexander" ,"Jack"));
        female_names.addAll(Arrays.asList("Mary", "Patricia","Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Margaret", "Karen", "Nancy", "Lisa", "Betty", "Dorothy", "Sandra", "Ashley", "Kimberly", "Donna", "Emily", "Carol", "Michelle", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Helen", "Sharon", "Cynthia", "Kathleen", "Amy", "Shirley", "Angela", "Anna", "Ruth", "Brenda", "Pamela", "Nicole", "Katherine", "Samantha", "Christine", "Catherine", "Virginia", "Debra", "Rachel", "Janet", "Emma", "Carolyn", "Maria", "Heather", "Diane", "Julie", "Joyce", "Evelyn", "Joan", "Victoria", "Kelly", "Christina", "Lauren", "Frances", "Martha", "Judith", "Cheryl", "Megan", "Andrea", "Olivia", "Ann", "Jean", "Alice", "Jacqueline", "Hannah", "Doris", "Kathryn", "Gloria", "Teresa", "Sara", "Janice", "Marie", "Julia", "Grace", "Judy", "Theresa", "Beverly", "Denise", "Marilyn", "Amber", "Danielle", "Rose", "Brittany", "Diana", "Abigail", "Natalie", "Jane", "Lori", "Alexis", "Tiffany", "Kayla"));
        last_name.addAll(Arrays.asList("SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER", "WILSON", "MOORE", "TAYLOR", "ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN", "THOMPSON", "GARCIA", "MARTINEZ", "ROBINSON", "CLARK", "RODRIGUEZ", "LEWIS", "LEE", "WALKER", "HALL", "ALLEN", "YOUNG", "HERNANDEZ", "KING", "WRIGHT", "LOPEZ", "HILL","SCOTT", "GREEN", "ADAMS", "BAKER", "GONZALEZ", "NELSON", "CARTER", "MITCHELL", "PEREZ", "ROBERTS", "TURNER", "PHILLIPS", "CAMPBELL", "PARKER", "EVANS", "EDWARDS", "COLLINS", "STEWART", "SANCHEZ", "MORRIS", "ROGERS", "REED", "COOK", "MORGAN", "BELL", "MURPHY", "BAILEY", "RIVERA", "COOPER", "RICHARDSON"));
        placeOfBirth.addAll(Arrays.asList(context.getResources().getStringArray(R.array.placebirth_array)));
        profession.addAll(Arrays.asList(context.getResources().getStringArray(R.array.profession_array)));
        desire.addAll(Arrays.asList(context.getResources().getStringArray(R.array.desire_array)));
        moment.addAll(Arrays.asList(context.getResources().getStringArray(R.array.defmoment_array)));
        traits.addAll(Arrays.asList(context.getResources().getStringArray(R.array.trait_array)));
        needs.addAll(Arrays.asList(context.getResources().getStringArray(R.array.need_array)));

        //Generate the new random Instance
        Random r = new Random();

        //Gender randomizer
        int gender = (r.nextInt(2) + 1);

        //NAME RANDOMIZER
        int index = (r.nextInt(male_names.size()));
        int index2 = (r.nextInt(female_names.size()));
        int index3 = (r.nextInt(last_name.size()));
        //Depending the gender which name to pick
        if(gender == 1) {
            selectedName = male_names.get(index);
        } else {
            selectedName = female_names.get(index2);
        }
        String selectedLastName =last_name.get(index3).toLowerCase();
        String completeName = selectedName + " " + WordUtils.capitalize(selectedLastName);

        //Place of birth
        int index4 = (r.nextInt(placeOfBirth.size()));
        String placeBirth = placeOfBirth.get(index4);

        //Age randomizer
        int age = (r.nextInt(80) + 12);

        //Profession
        int index5 = (r.nextInt(profession.size()));
        String job = profession.get(index5);

        //Desire
        int index6 = (r.nextInt(desire.size()));
        String wish = desire.get(index6);

        //Moment
        int index7 = (r.nextInt(moment.size()));
        String defmoment = moment.get(index7);

        //Traits
        int index8 = (r.nextInt(traits.size()));
        int index9 = (r.nextInt(traits.size()));
        int index10 = (r.nextInt(traits.size()));
        String trait1 = traits.get(index8);
        String trait2 = traits.get(index9);
        String trait3 = traits.get(index10);

        //Moment
        int index11 = (r.nextInt(needs.size()));
        String need =needs.get(index11);

        //Fill the list
        bio.add(completeName);
        bio.add(placeBirth);
        bio.add(String.valueOf(age));
        bio.add(String.valueOf(gender));
        bio.add(job);
        bio.add(wish);
        bio.add(defmoment);
        bio.add(trait1);
        bio.add(trait2);
        bio.add(trait3);
        bio.add(need);
        return bio;

    }


    private void saveToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DESIRE, desire_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_AGE, age_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_GENDER, gender_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name_tv.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_TRAIT1, trait_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_TRAIT2, trait2_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_TRAIT3, trait3_edit_text.getText().toString());


        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, null, values);

        //Come back to previous fragment
        fragmentTransition();

    }


    private void updateDB(String name_text, String project_name){
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DESIRE, desire_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_AGE, age_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_GENDER, gender_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name);
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_TRAIT1, trait_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_TRAIT2, trait2_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_TRAIT3, trait3_edit_text.getText().toString());
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, values,   "name = ?", new String[]{name_text.toString()});

        //Come back to previous fragment
        fragmentTransition();

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
            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
    }


    public void fragmentTransition(){
        Bundle bundle = new Bundle();
        bundle.putString("project_name",project_name_tv.getText().toString());
        //Send it to the next fragment
        CharListFragment nextFragment = new CharListFragment();
        nextFragment.setArguments(bundle);
        //Make the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();

    }

    public static void showRateDialogForRate(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.rate_app))
                .setMessage(context.getString(R.string.rate_app_des))
                .setPositiveButton(context.getString(R.string.rate_submit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            ////////////////////////////////
                            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            // To count with Play market backstack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            rated = true;
                            try {
                                context.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            }


                        }
                    }
                })
                .setNegativeButton( context.getString(R.string.rate_cancel), null);
        builder.show();
    }


}
