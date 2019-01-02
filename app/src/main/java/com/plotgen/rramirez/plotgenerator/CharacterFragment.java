package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class CharacterFragment extends Fragment {

    public static int rated;
    public EditText nameEditText, profession_edit_text, desire_edit_text, age_edit_text, height_et, hair_color_et, eye_color_et, bodybuild_et,
            placebirth_edit_text, defmoment_edit_text, need_edit_text, phrase_et, trait_edit_text,
            trait2_edit_text, trait3_edit_text, notes_edit_text;
    public TextView project_name_tv;
    public ImageButton delete_btn, random_gen_char_btn;
    public Spinner gender_spinner, role_spinner;
    public String project_name;
    public String project_id;
    ArrayList<String> char_description;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String fragmentTag = CharacterFragment.class.getSimpleName();

    public CharacterFragment() {
        // Required empty public constructor
    }

    //TODO BE able to block certain parts to just randomize some others. Yep, a mess

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myFragmentView = inflater.inflate(R.layout.fragment_character, container, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        try {
            final String project_info = this.getArguments().getString("project_info");
            project_name = project_info.substring(2);
            project_id = String.valueOf(project_info.charAt(0));
        } catch (Exception e) {

        }

        final String project_name_fromEdit = this.getArguments().getString("project_name");
        final String name_text = this.getArguments().getString("char_name");

        //Get if rated already
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(myFragmentView.getContext());
        rated = preferences.getInt("rated", 0);

        //Gender spinner functions
        gender_spinner = myFragmentView.findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(), R.array.gender_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);
        //Role spinner functions
        role_spinner = myFragmentView.findViewById(R.id.role_spinner);
        ArrayAdapter<CharSequence> role_adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(), R.array.char_guide_types_titles, android.R.layout.simple_spinner_item);
        role_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spinner.setAdapter(role_adapter);


        //Declare all the elements
        project_name_tv = myFragmentView.findViewById(R.id.char_template_title);
        nameEditText = myFragmentView.findViewById(R.id.nameEditText);
        age_edit_text = myFragmentView.findViewById(R.id.age_edit_text);
        profession_edit_text = myFragmentView.findViewById(R.id.profession_edit_text);
        height_et = myFragmentView.findViewById(R.id.height_et);
        hair_color_et = myFragmentView.findViewById(R.id.hair_color_et);
        eye_color_et = myFragmentView.findViewById(R.id.eye_color_et);
        bodybuild_et = myFragmentView.findViewById(R.id.bodybuild_et);
        placebirth_edit_text = myFragmentView.findViewById(R.id.placebirth_edit_text);
        defmoment_edit_text = myFragmentView.findViewById(R.id.defmoment_edit_text);
        desire_edit_text = myFragmentView.findViewById(R.id.desire_edit_text);
        need_edit_text = myFragmentView.findViewById(R.id.need_edit_text);
        phrase_et = myFragmentView.findViewById(R.id.phrase_et);
        trait_edit_text = myFragmentView.findViewById(R.id.trait_edit_text);
        trait2_edit_text = myFragmentView.findViewById(R.id.trait_2_edit_text);
        trait3_edit_text = myFragmentView.findViewById(R.id.trait_3_edit_text);
        notes_edit_text = myFragmentView.findViewById(R.id.notes_edit_text);
        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.char_template_submit);
        random_gen_char_btn = myFragmentView.findViewById(R.id.random_gen_char_btn);
        delete_btn = myFragmentView.findViewById(R.id.char_edit_delete_btn);

        //Set the title
        project_name_tv.setText(project_name);

        if (name_text == null) { //If it's in create new character mode
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveToDB();
                }
            });
            delete_btn.setVisibility(View.INVISIBLE);
            // Inflate the layout for this fragment
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.character_create));
        } else { //If it's in update mode
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.character_update));
            delete_btn.setVisibility(View.VISIBLE);
            //database getter
            char_description = getDescription(myFragmentView.getContext(), name_text);
            if (char_description.size() > 0) {
                nameEditText.setText(char_description.get(0));
                age_edit_text.setText(char_description.get(1));
                placebirth_edit_text.setText(char_description.get(3));
                profession_edit_text.setText(char_description.get(4));
                height_et.setText(char_description.get(5));
                hair_color_et.setText(char_description.get(6));
                eye_color_et.setText(char_description.get(7));
                bodybuild_et.setText(char_description.get(8));
                desire_edit_text.setText(char_description.get(9));
                defmoment_edit_text.setText(char_description.get(11));
                need_edit_text.setText(char_description.get(12));
                phrase_et.setText(char_description.get(13));
                trait_edit_text.setText(char_description.get(14));
                trait2_edit_text.setText(char_description.get(15));
                trait3_edit_text.setText(char_description.get(16));
                notes_edit_text.setText(char_description.get(17));

                //Set the proper spinner value
                String gender = char_description.get(2);
                if (gender.equals("Masculino") || gender.equals("Male")) {
                    gender_spinner.setSelection(1);
                } else if (gender.equals("Female") || gender.equals("Femenino")) {
                    gender_spinner.setSelection(2);
                } else if (gender.equals("Transgender") || gender.equals("Transgenero")) {
                    gender_spinner.setSelection(3);
                } else if (gender.equals("Binario") || gender.equals("Binary")) {
                    gender_spinner.setSelection(4);
                } else {
                    gender_spinner.setSelection(5);
                }

                //Set the proper spinner value
                String role = char_description.get(10);
                if (role.equals("Protagonist") || role.equals("Protagonista")) {
                    role_spinner.setSelection(0);
                } else if (role.equals("Protagonist's Helper") || role.equals("Ayudante de protagonista")) {
                    role_spinner.setSelection(1);
                } else if (role.equals("Sidekick") || role.equals("Escudero")) {
                    role_spinner.setSelection(2);
                } else if (role.equals("Guardian") || role.equals("Guardian")) {
                    role_spinner.setSelection(3);
                } else if (role.equals("Mentor") || role.equals("Mentor")) {
                    role_spinner.setSelection(4);
                } else if (role.equals("Impact") || role.equals("De Impacto")) {
                    role_spinner.setSelection(5);
                } else if (role.equals("Antagonist") || role.equals("Antagonista")) {
                    role_spinner.setSelection(6);
                } else if (role.equals("Antagonist's Helper") || role.equals("Ayudante de Antagonista")) {
                    role_spinner.setSelection(7);
                } else if (role.equals("Skeptic") || role.equals("Esceptico")) {
                    role_spinner.setSelection(8);
                } else if (role.equals("Obstacle") || role.equals("Obstaculo")) {
                    role_spinner.setSelection(9);
                } else {
                    role_spinner.setSelection(10);
                }
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDB(name_text, project_name_fromEdit);
                }
            });
            if (rated == 0) {
                Utils.showRateDialogForRate(myFragmentView.getContext());
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
                height_et.setText(info.get(5));
                hair_color_et.setText(info.get(6));
                eye_color_et.setText(info.get(7));
                bodybuild_et.setText(info.get(8));
                desire_edit_text.setText(info.get(9));
                defmoment_edit_text.setText(info.get(10));
                trait_edit_text.setText(info.get(11));
                trait2_edit_text.setText(info.get(12));
                trait3_edit_text.setText(info.get(13));
                need_edit_text.setText(info.get(14));
                phrase_et.setText(info.get(15));
            }
        });


        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(myFragmentView.getContext());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setTitle(getString(R.string.delete_character_btn));
        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFromDB(name_text);
            }
        });
        // create alert dialog
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        //Delete
        delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.show();

            }
        });


        return myFragmentView;
    }


    private ArrayList<String> generateBIO(Context context) {

        ArrayList<String> male_names = new ArrayList<>(100);
        ArrayList<String> female_names = new ArrayList<>(100);
        ArrayList<String> last_name = new ArrayList<>(100);
        ArrayList<String> placeOfBirth = new ArrayList<>(100);
        ArrayList<String> profession = new ArrayList<>(100);
        ArrayList<String> height = new ArrayList<>();
        ArrayList<String> haircolor = new ArrayList<>();
        ArrayList<String> eyecolor = new ArrayList<>();
        ArrayList<String> bodybuild = new ArrayList<>();
        ArrayList<String> desire = new ArrayList<>();
        ArrayList<String> moment = new ArrayList<>();
        ArrayList<String> traits = new ArrayList<>();
        ArrayList<String> phrases_array = new ArrayList<>();
        ArrayList<String> needs = new ArrayList<>();
        ArrayList<String> bio = new ArrayList<>();
        String selectedName;


        male_names.addAll(Arrays.asList("James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew", "Kenneth", "George", "Joshua", "Kevin", "Brian", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob", "Gary", "Nicholas", "Eric", "Stephen", "Jonathan", "Larry", "Justin", "Scott", "Brandon", "Frank", "Benjamin", "Gregory", "Raymond", "Samuel", "Patrick", "Alexander", "Jack", "Santiago", "Sebastián", "Matías", "Mateo", "Nicolás", "Alejandro", "Diego", "Samuel", "Benjamín", "Daniel", "Joaquín", "Lucas", "Tomas", "Gabriel", "Martín", "David", "Emiliano", "Jerónimo", "Emmanuel", "Agustín", "Juan Pablo", "Juan José", "Andrés", "Thiago", "Leonardo", "Felipe", "Ángel", "Maximiliano", "Christopher", "Juan Diego", "Adrián", "Pablo", "Miguel Ángel", "Rodrigo", "Alexander", "Ignacio", "Emilio", "Dylan", "Bruno", "Carlos", "Vicente", "Valentino", "Santino", "Julián", "Juan Sebastián", "Aarón", "Lautaro", "Axel", "Fernando", "Ian", "Christian", "Javier", "Manuel", "Luciano", "Francisco", "Juan David", "Iker", "Facundo", "Rafael", "Alex", "Franco", "Antonio", "Luis", "Isaac", "Máximo", "Pedro", "Ricardo", "Sergio", "Eduardo", "Bautista"));
        female_names.addAll(Arrays.asList("Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Margaret", "Karen", "Nancy", "Lisa", "Betty", "Dorothy", "Sandra", "Ashley", "Kimberly", "Donna", "Emily", "Carol", "Michelle", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Helen", "Sharon", "Cynthia", "Kathleen", "Amy", "Shirley", "Angela", "Anna", "Ruth", "Brenda", "Pamela", "Nicole", "Katherine", "Samantha", "Christine", "Catherine", "Virginia", "Debra", "Rachel", "Janet", "Emma", "Carolyn", "Maria", "Heather", "Diane", "Julie", "Joyce", "Evelyn", "Joan", "Victoria", "Kelly", "Christina", "Lauren", "Frances", "Martha", "Judith", "Cheryl", "Megan", "Andrea", "Olivia", "Ann", "Jean", "Alice", "Jacqueline", "Hannah", "Doris", "Kathryn", "Gloria", "Teresa", "Sara", "Janice", "Marie", "Julia", "Grace", "Judy", "Theresa", "Beverly", "Denise", "Marilyn", "Amber", "Danielle", "Rose", "Brittany", "Diana", "Abigail", "Natalie", "Jane", "Lori", "Alexis", "Tiffany", "Kayla", "Sofia", "Isabella", "Camila", "Valentina", "Valeria", "Mariana", "Luciana", "Daniela", "Gabriela", "Victoria", "Martina", "Lucia", "Ximena/Jimena", "Sara", "Samantha", "Maria José", "Emma", "Catalina", "Julieta", "Mía", "Antonella", "Renata", "Emilia", "Natalia", "Zoe", "Nicole", "Paula", "Amanda", "María Fernanda", "Emily", "Antonia", "Alejandra", "Juana", "Andrea", "Manuela", "Ana Sofia", "Guadalupe", "Agustina", "Elena", "María", "Bianca", "Ariana", "Ivanna", "Abril", "Florencia", "Carolina", "Maite", "Rafaela"));
        last_name.addAll(Arrays.asList("SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER", "WILSON", "MOORE", "TAYLOR", "ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN", "THOMPSON", "GARCIA", "MARTINEZ", "ROBINSON", "CLARK", "RODRIGUEZ", "LEWIS", "LEE", "WALKER", "HALL", "ALLEN", "YOUNG", "HERNANDEZ", "KING", "WRIGHT", "LOPEZ", "HILL", "SCOTT", "GREEN", "ADAMS", "BAKER", "GONZALEZ", "NELSON", "CARTER", "MITCHELL", "PEREZ", "ROBERTS", "TURNER", "PHILLIPS", "CAMPBELL", "PARKER", "EVANS", "EDWARDS", "COLLINS", "STEWART", "SANCHEZ", "MORRIS", "ROGERS", "REED", "COOK", "MORGAN", "BELL", "MURPHY", "BAILEY", "RIVERA", "COOPER", "RICHARDSON", "Garcia", "Fernandez", "Lopez", "Martinez", "Gonzalez", "Rodriguez", "Sanchez", "Perez", "Martin", "Gomez", "Ruiz", "Diaz", "Hernandez", "Alvarez", "Jimenez", "Moreno", "Munoz", "Alonso", "Romero", "Navarro", "Gutierrez", "Torres", "Dominguez", "Gil", "Vazquez", "Blanco", "Serrano", "Ramos", "Castro", "Suarez", "Sanz", "Rubio", "Ortega", "Molina", "Delgado", "Ortiz", "Morales", "Ramirez", "Marin", "Iglesias", "Santos", "Castillo", "Garrido"));
        placeOfBirth.addAll(Arrays.asList(context.getResources().getStringArray(R.array.placebirth_array)));
        profession.addAll(Arrays.asList(context.getResources().getStringArray(R.array.profession_array)));
        height.addAll(Arrays.asList(context.getResources().getStringArray(R.array.heights)));
        haircolor.addAll(Arrays.asList(context.getResources().getStringArray(R.array.haircolors)));
        eyecolor.addAll(Arrays.asList(context.getResources().getStringArray(R.array.eyecolors)));
        bodybuild.addAll(Arrays.asList(context.getResources().getStringArray(R.array.bodytypes)));
        desire.addAll(Arrays.asList(context.getResources().getStringArray(R.array.desire_array)));
        moment.addAll(Arrays.asList(context.getResources().getStringArray(R.array.defmoment_array)));
        phrases_array.addAll(Arrays.asList(context.getResources().getStringArray(R.array.phrases)));
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
        if (gender == 1) {
            selectedName = male_names.get(index);
        } else {
            selectedName = female_names.get(index2);
        }
        String selectedLastName = last_name.get(index3).toLowerCase();
        String completeName = selectedName + " " + WordUtils.capitalize(selectedLastName);

        //Place of birth
        int index4 = (r.nextInt(placeOfBirth.size()));
        String placeBirth = placeOfBirth.get(index4);

        //Age randomizer
        int age = (r.nextInt(60) + 12);

        //Profession
        int index5 = (r.nextInt(profession.size()));
        String job = profession.get(index5);

        //Height
        int index_height = (r.nextInt(height.size()));
        String height_temp = height.get(index_height);
        //Hair Color
        int index_hair = (r.nextInt(haircolor.size()));
        String hair = haircolor.get(index_hair);
        //Eye Color
        int index_eye = (r.nextInt(eyecolor.size()));
        String eye = eyecolor.get(index_eye);
        //Bodybuilds
        int index_body = (r.nextInt(bodybuild.size()));
        String body = bodybuild.get(index_body);

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

        //phrases
        int index20 = (r.nextInt(phrases_array.size()));
        String phrase_text = phrases_array.get(index20);

        //Moment
        int index11 = (r.nextInt(needs.size()));
        String need = needs.get(index11);

        //Fill the list
        bio.add(completeName);
        bio.add(placeBirth);
        bio.add(String.valueOf(age));
        bio.add(String.valueOf(gender));
        bio.add(job);
        bio.add(height_temp);
        bio.add(hair);
        bio.add(eye);
        bio.add(body);
        bio.add(wish);
        bio.add(defmoment);
        bio.add(trait1);
        bio.add(trait2);
        bio.add(trait3);
        bio.add(need);
        bio.add(phrase_text);
        return bio;
    }


    private void saveToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT_ID, project_id);
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_HEIGHT, height_et.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_HAIRCOLOR, hair_color_et.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EYECOLOR, eye_color_et.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_BODYBUILD, bodybuild_et.getText().toString());
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
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ENOTES, notes_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PHRASE, phrase_et.getText().toString());
        database.insert(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, null, values);

        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("Character", "completed");
        mFirebaseAnalytics.logEvent("character_created", params);
        fragmentTransition();
    }

    private void deleteFromDB(String name_char) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        database.delete(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, "name = ?", new String[]{name_char});
        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("Character", "deleted");
        mFirebaseAnalytics.logEvent("character_deleted", params);

        fragmentTransition();
    }


    private void updateDB(String name_text, String project_name) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_HEIGHT, height_et.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_HAIRCOLOR, hair_color_et.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EYECOLOR, eye_color_et.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_BODYBUILD, bodybuild_et.getText().toString());
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
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ENOTES, notes_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PHRASE, phrase_et.getText().toString());
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, values, "name = ?", new String[]{name_text.toString()});
        //Come back to previous fragment
        fragmentTransition();

    }

    public ArrayList<String> getDescription(Context context, String char_name) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE name = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{char_name});
        cursor.moveToFirst();
        ArrayList<String> char_list = new ArrayList<String>();
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
            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
    }


    public void fragmentTransition() {
        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment, transaction, "", "");
        getFragmentManager().popBackStack();

    }

}
