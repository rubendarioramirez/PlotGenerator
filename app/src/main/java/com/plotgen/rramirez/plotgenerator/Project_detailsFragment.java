package com.plotgen.rramirez.plotgenerator;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;


import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.plotgen.rramirez.plotgenerator.Common.Constants.debugMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class Project_detailsFragment extends Fragment {

    private static final int PERMISSION_REQUEST_GALLERY = 101;
    private static final int REQUEST_CODE_GALLERY = 102;
    public Boolean updateMode;
    EditText project_name_et, project_plot_et;
    Spinner project_genre_spinner;
    String project_name_text, projectID;
    FloatingActionButton fab_save;
    FloatingActionButton fab_delete;
    ArrayList<String> project_description;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView project_icon_iv;
    private View myFragmentView;
    private String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private Uri uri;
    private String filepath = "";
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ArrayList<String> project_list_array = new ArrayList<>();

    public Project_detailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_project_details, container, false);

        project_name_et = myFragmentView.findViewById(R.id.project_name_et);
        project_plot_et = myFragmentView.findViewById(R.id.project_plot_et);
        project_icon_iv = myFragmentView.findViewById(R.id.project_icon);
        fab_save = myFragmentView.findViewById(R.id.project_add_submit);
        fab_delete = myFragmentView.findViewById(R.id.project_detail_delete);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());


        //Role spinner functions
        project_genre_spinner = myFragmentView.findViewById(R.id.project_genre_spinner);
        project_genre_spinner = myFragmentView.findViewById(R.id.project_genre_spinner);
        ArrayAdapter<CharSequence> genre_adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(), R.array.genres_array, android.R.layout.simple_spinner_item);
        genre_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project_genre_spinner.setAdapter(genre_adapter);

        //Make sure the right buttons are appearing.
        setupUI();

        //Make sure you can click on the pick
        project_icon_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileImageDialog();
            }
        });

        if (!Common.projectCreationMode) {
            //Update mode
            if(Common.currentProject !=null) {
                project_name_text = Common.currentProject.getName();
                projectID = Common.currentProject.getId();
            } else {
                project_name_text = "Rambo";
            }


            project_list_array = getProject(this.getContext());
            if (project_list_array != null && !project_list_array.isEmpty()) {
                project_name_et.setText(project_list_array.get(0));
                String project_genre =  project_list_array.get(1);
                project_plot_et.setText(project_list_array.get(2));
                String[] possibleGenres = getResources().getStringArray(R.array.genres_array);
//                if (project_list_array.size() >= 4 && !project_list_array.get(3).equalsIgnoreCase(null) && !project_list_array.get(3).equalsIgnoreCase("")) {
                  if(project_list_array.size() >= 4 )
                    {
                    project_icon_iv.setImageURI(Uri.parse(project_list_array.get(3)));
                    }

                    //Update the spinner
                    if (project_genre.equals(possibleGenres[0])) {
                        project_genre_spinner.setSelection(0);
                    } else if (project_genre.equals(possibleGenres[1])) {
                        project_genre_spinner.setSelection(1);
                    } else if (project_genre.equals(possibleGenres[2])) {
                        project_genre_spinner.setSelection(2);
                    } else if (project_genre.equals(possibleGenres[3])) {
                        project_genre_spinner.setSelection(3);
                    } else if (project_genre.equals(possibleGenres[4])) {
                        project_genre_spinner.setSelection(4);
                    } else if (project_genre.equals(possibleGenres[5])) {
                        project_genre_spinner.setSelection(5);
                    } else if (project_genre.equals(possibleGenres[6])) {
                        project_genre_spinner.setSelection(6);
                    } else if (project_genre.equals(possibleGenres[7])) {
                        project_genre_spinner.setSelection(7);
                    } else if (project_genre.equals(possibleGenres[8])) {
                        project_genre_spinner.setSelection(8);
                    } else if (project_genre.equals(possibleGenres[9])) {
                        project_genre_spinner.setSelection(9);
                    } else if (project_genre.equals(possibleGenres[10])) {
                        project_genre_spinner.setSelection(10);
                    } else if (project_genre.equals(possibleGenres[11])) {
                        project_genre_spinner.setSelection(11);
                    } else if (project_genre.equals(possibleGenres[12])) {
                        project_genre_spinner.setSelection(12);
                    } else if (project_genre.equals(possibleGenres[13])) {
                        project_genre_spinner.setSelection(13);
                    } else if (project_genre.equals(possibleGenres[14])) {
                        project_genre_spinner.setSelection(14);
                    }
                    updateMode = true;
            }
        }

        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(project_name_et)) {
                    Toast.makeText(getActivity(), getString(R.string.projects_empty),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (!Common.projectCreationMode) {
                        updateDB();
                    } else {
                        saveToDB(project_name_et, project_plot_et, project_genre_spinner);
                        Common.projectCreationMode = false;
                    }
                    fragmentTransaction();
                }
            }
        });


        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle(getString(R.string.delete_project_btn))
                        .setMessage(getString(R.string.delete_project_btn_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                //deleteFromStories();
                                Utils.deleteFromDB(view.getContext(), projectID);
                                fragmentTransaction();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        if(Common.onBoarding == 1) {
            Utils.displayDialog(myFragmentView.getContext(), getString(R.string.onBoardingTitle_2), getString(R.string.onBoarding_2), "Got it!");
            Common.onBoarding = 2;
        }
        return myFragmentView;
    }


    private void saveToDB(TextView project_name_et, TextView project_plot_et, Spinner project_genre_spinner) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PROJECT, project_name_et.getText().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_GENRE, project_genre_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PLOT, project_plot_et.getText().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_IMAGE, filepath);
        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, values);

        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("Genre", project_genre_spinner.getSelectedItem().toString());
        mFirebaseAnalytics.logEvent("genre", params);
        database.close();

    }

    private void updateDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PROJECT, project_name_et.getText().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_GENRE, project_genre_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PLOT, project_plot_et.getText().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_IMAGE, filepath);
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, values, "_id = ?", new String[]{projectID});
        database.close();
    }

    private void deleteFromStories() {
        final String project_id = project_list_array.get(3);
        final DatabaseReference mStoriesDatabase = mDatabase.getReference().child("stories");
        final DatabaseReference mGenreDatabase = mDatabase.getReference().child("stories").child("genre");
        if (mUser != null) {
            mStoriesDatabase.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    if (mutableData.hasChild(mUser.getUid())) {
                        if (mutableData.child(mUser.getUid()).hasChild(project_id)) {
                            mStoriesDatabase.child(mUser.getUid()).child(project_id).removeValue();
                        }
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    Log.d("Updated token", "postTransaction:onComplete:" + databaseError);

                }
            });

            mGenreDatabase.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    if (mutableData.hasChild(project_list_array.get(1))) {
                        if (mutableData.child(project_list_array.get(1)).hasChild(mUser.getUid()))
                            if (mutableData.child(project_list_array.get(1)).child(mUser.getUid()).hasChild(project_id)) {
                                mGenreDatabase.child(project_list_array.get(1)).child(mUser.getUid()).child(project_id).removeValue();
                            }
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    Log.d("Updated token", "postTransaction:onComplete:" + databaseError);
                }
            });

        }
    }

    private boolean isEmpty(TextView etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }


    public ArrayList<String> getProject(Context context) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_PROJECT + " WHERE _id = ?";
        ArrayList<String> char_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{projectID});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                char_list.add(cursor.getString(cursor.getColumnIndex("project")));
                char_list.add(cursor.getString(cursor.getColumnIndex("genre")));
                char_list.add(cursor.getString(cursor.getColumnIndex("plot")));
                if (cursor.getColumnIndex("image") != -1 && cursor.getString(cursor.getColumnIndex("image")) != null)
                    char_list.add(cursor.getString(cursor.getColumnIndex("image")));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }
        return char_list;
    }

    private void showProfileImageDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(myFragmentView.getContext(), R.style.bottom_dialog_theme);
        dialog.setContentView(R.layout.dialog_profile_image_selection);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        RelativeLayout tvSelectFromGallery = dialog.findViewById(R.id.select_from_gallery_container);
        RelativeLayout tvRemovePhoto = dialog.findViewById(R.id.remove_photo_container);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvRemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filepath="";
                project_icon_iv.setImageResource(R.drawable.ic_menu_camera);
                dialog.dismiss();
            }
        });


        tvSelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(myFragmentView.getContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_GALLERY);
                } else {
                    openGallery();
                }

                dialog.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_GALLERY:
                boolean isGalleryGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);

                if (isGalleryGranted) {
                    openGallery();
                    return;
                }

                break;
            default:
        }
    }

    //open gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // request code gallery
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();

                        try {
                            filepath = Utils.getFilePath(myFragmentView.getContext(), uri);
                            if (filepath != null)
                                project_icon_iv.setImageURI(Uri.parse(filepath));

                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
        }
    }

    private void fragmentTransaction(){
        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }

    private void setupUI(){
        if(Common.projectCreationMode){
            fab_delete.hide();
        }
        else{
            fab_delete.show();
        }
    }

}
