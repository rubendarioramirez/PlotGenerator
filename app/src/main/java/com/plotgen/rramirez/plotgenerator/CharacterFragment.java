package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends Fragment {

    public EditText nameEditText, profession_edit_text, desire_edit_text, age_edit_text, placebirth_edit_text, role_edit_text, defmoment_edit_text,need_edit_text;
    public TextView project_name_tv;
    public Button submit, random_gen_char_btn;
    public Spinner gender_spinner;
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



        //Declare all the elements
        project_name_tv = myFragmentView.findViewById(R.id.project_name_tv);
        nameEditText = myFragmentView.findViewById(R.id.nameEditText);
        age_edit_text = myFragmentView.findViewById(R.id.age_edit_text);
        profession_edit_text = myFragmentView.findViewById(R.id.profession_edit_text);
        placebirth_edit_text = myFragmentView.findViewById(R.id.placebirth_edit_text);
        role_edit_text= myFragmentView.findViewById(R.id.role_edit_text);
        defmoment_edit_text= myFragmentView.findViewById(R.id.defmoment_edit_text);
        desire_edit_text = myFragmentView.findViewById(R.id.desire_edit_text);
        need_edit_text= myFragmentView.findViewById(R.id.need_edit_text);
        //Save button action
        submit =  myFragmentView.findViewById(R.id.submit);
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
            role_edit_text.setText(char_description.get(6));
            defmoment_edit_text.setText(char_description.get(7));
            need_edit_text.setText(char_description.get(8));

            //Set the proper spinner value
            String gender = char_description.get(2);
            if(gender.equals("Masculino") || gender.equals("Male")){
                gender_spinner.setSelection(1);
            } else {
                gender_spinner.setSelection(2);
            }

            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    updateDB(name_text.toString(), project_name_text.toString());
                }
            });
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
                defmoment_edit_text.setText("Randomly generated");
                need_edit_text.setText("Randomly generated");

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
        ArrayList<String> bio = new ArrayList<String>();
        String selectedName;


        male_names.addAll(Arrays.asList("James", "John", "Robert", "Michael", "William", "David", "Richard","Joseph", "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew", "Kenneth", "George", "Joshua", "Kevin", "Brian", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob", "Gary", "Nicholas", "Eric", "Stephen", "Jonathan", "Larry","Justin", "Scott", "Brandon","Frank", "Benjamin","Gregory" ,"Raymond" ,"Samuel", "Patrick", "Alexander" ,"Jack"));
        female_names.addAll(Arrays.asList("Mary", "Patricia","Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Margaret", "Karen", "Nancy", "Lisa", "Betty", "Dorothy", "Sandra", "Ashley", "Kimberly", "Donna", "Emily", "Carol", "Michelle", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Helen", "Sharon", "Cynthia", "Kathleen", "Amy", "Shirley", "Angela", "Anna", "Ruth", "Brenda", "Pamela", "Nicole", "Katherine", "Samantha", "Christine", "Catherine", "Virginia", "Debra", "Rachel", "Janet", "Emma", "Carolyn", "Maria", "Heather", "Diane", "Julie", "Joyce", "Evelyn", "Joan", "Victoria", "Kelly", "Christina", "Lauren", "Frances", "Martha", "Judith", "Cheryl", "Megan", "Andrea", "Olivia", "Ann", "Jean", "Alice", "Jacqueline", "Hannah", "Doris", "Kathryn", "Gloria", "Teresa", "Sara", "Janice", "Marie", "Julia", "Grace", "Judy", "Theresa", "Beverly", "Denise", "Marilyn", "Amber", "Danielle", "Rose", "Brittany", "Diana", "Abigail", "Natalie", "Jane", "Lori", "Alexis", "Tiffany", "Kayla"));
        last_name.addAll(Arrays.asList("SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER", "WILSON", "MOORE", "TAYLOR", "ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN", "THOMPSON", "GARCIA", "MARTINEZ", "ROBINSON", "CLARK", "RODRIGUEZ", "LEWIS", "LEE", "WALKER", "HALL", "ALLEN", "YOUNG", "HERNANDEZ", "KING", "WRIGHT", "LOPEZ", "HILL","SCOTT", "GREEN", "ADAMS", "BAKER", "GONZALEZ", "NELSON", "CARTER", "MITCHELL", "PEREZ", "ROBERTS", "TURNER", "PHILLIPS", "CAMPBELL", "PARKER", "EVANS", "EDWARDS", "COLLINS", "STEWART", "SANCHEZ", "MORRIS", "ROGERS", "REED", "COOK", "MORGAN", "BELL", "MURPHY", "BAILEY", "RIVERA", "COOPER", "RICHARDSON"));
        placeOfBirth.addAll(Arrays.asList(context.getResources().getStringArray(R.array.placebirth_array)));
        profession.addAll(Arrays.asList(context.getResources().getStringArray(R.array.profession_array)));
        desire.addAll(Arrays.asList(context.getResources().getStringArray(R.array.desire_array)));

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

        //Fill the list
        bio.add(completeName);
        bio.add(placeBirth);
        bio.add(String.valueOf(age));
        bio.add(String.valueOf(gender));
        bio.add(job);
        bio.add(wish);
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
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name_tv.getText().toString());


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
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name);
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


}
