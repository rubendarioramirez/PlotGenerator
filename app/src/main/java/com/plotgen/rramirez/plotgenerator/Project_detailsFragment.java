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

import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Project_detailsFragment extends Fragment {

    private static final int PERMISSION_REQUEST_GALLERY = 101;
    private static final int REQUEST_CODE_GALLERY = 102;
    EditText project_name_et, project_plot_et;
    Spinner project_genre_spinner;
    public Boolean updateMode;
    String project_name_text;
    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton fab_save;
    ArrayList<String> project_description;
    private ImageView project_icon_iv;
    private View myFragmentView;
    private String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private Uri uri;
    private String filepath = "";

    public Project_detailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_project_details, container, false);
        project_name_text = this.getArguments().getString("project_name");

        project_name_et = myFragmentView.findViewById(R.id.project_name_et);
        project_plot_et = myFragmentView.findViewById(R.id.project_plot_et);
        project_icon_iv = myFragmentView.findViewById(R.id.project_icon);
        final FloatingActionButton fab_save = myFragmentView.findViewById(R.id.project_add_submit);
        final FloatingActionButton fab_delete = myFragmentView.findViewById(R.id.project_detail_delete);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());

        //Role spinner functions
        project_genre_spinner = myFragmentView.findViewById(R.id.project_genre_spinner);
        ArrayAdapter<CharSequence> genre_adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(), R.array.genres_array, android.R.layout.simple_spinner_item);
        genre_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project_genre_spinner.setAdapter(genre_adapter);

        project_icon_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileImageDialog();
            }
        });

        if (project_name_text != "") {
            //Update mode
            ArrayList<String> project_list_array = Utils.getProject(this.getContext(), project_name_text);
            if (project_list_array != null && !project_list_array.isEmpty()) {
                project_name_et.setText(project_list_array.get(0));
                project_name_et.setEnabled(false);
                project_plot_et.setText(project_list_array.get(2));
                if(!project_list_array.get(3).equalsIgnoreCase(null) && !project_list_array.get(3).equalsIgnoreCase(""))
                project_icon_iv.setImageURI(Uri.parse(project_list_array.get(3)));
                project_description = getProject(myFragmentView.getContext(), project_name_text);
                String project_genre = project_description.get(1);
                //Set the proper spinner value
                if (project_genre.equals("Tragedia") || project_genre.equals("Tragedy")) {
                    project_genre_spinner.setSelection(0);
                } else if (project_genre.equals("Ciencía Ficción") || project_genre.equals("Science fiction")) {
                    project_genre_spinner.setSelection(1);
                } else if (project_genre.equals("Fantasía") || project_genre.equals("Fantasy")) {
                    project_genre_spinner.setSelection(2);
                } else if (project_genre.equals("Mitología") || project_genre.equals("Mythology")) {
                    project_genre_spinner.setSelection(3);
                } else if (project_genre.equals("Aventura") || project_genre.equals("Adventure")) {
                    project_genre_spinner.setSelection(4);
                } else if (project_genre.equals("Misterio") || project_genre.equals("Mistery")) {
                    project_genre_spinner.setSelection(5);
                } else if (project_genre.equals("Drama")) {
                    project_genre_spinner.setSelection(6);
                } else if (project_genre.equals("Romance")) {
                    project_genre_spinner.setSelection(7);
                } else if (project_genre.equals("Acción") || project_genre.equals("Action / Adventure")) {
                    project_genre_spinner.setSelection(8);
                } else if (project_genre.equals("Satira") || project_genre.equals("Satire")) {
                    project_genre_spinner.setSelection(9);
                } else if (project_genre.equals("Horror")) {
                    project_genre_spinner.setSelection(10);
                } else if (project_genre.equals("Tragedia comica") || project_genre.equals("Tragic comedy")) {
                    project_genre_spinner.setSelection(11);
                } else if (project_genre.equals("Ficción para jovenes") || project_genre.equals("Young adult fiction") || project_genre.equals("Ficción para joveves")) {
                    project_genre_spinner.setSelection(12);
                } else if (project_genre.equals("Dystopia")) {
                    project_genre_spinner.setSelection(13);
                } else if (project_genre.equals("Thriller") || project_genre.equals("Action thriller")) {
                    project_genre_spinner.setSelection(14);
                }
                //project_icon_iv.setImageURI(Uri.parse(project_description.get(2)));
                updateMode = true;
            } else {
                //make it back to project
                ProjectFragment nextFragment = new ProjectFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment, transaction, "", "");
                getFragmentManager().popBackStack();

            }
        } else {
            updateMode = false;
        }


        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(project_name_et)) {
                    Toast.makeText(getActivity(), getString(R.string.projects_empty),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (updateMode) {
                        updateDB();
                        fab_delete.setVisibility(View.INVISIBLE);
                    } else {
                        saveToDB(project_name_et, project_plot_et, project_genre_spinner);
                        fab_delete.setVisibility(View.VISIBLE);
                    }
                    ProjectFragment nextFragment = new ProjectFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    Utils.changeFragment(nextFragment, transaction, "", "");
                    getFragmentManager().popBackStack();

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
                                Utils.deleteFromDB(view.getContext(), project_name_text);
                                ProjectFragment nextFragment = new ProjectFragment();
                                //Make the transaction
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                Utils.changeFragment(nextFragment, transaction, "", "");
                                getFragmentManager().popBackStack();

                                //transaction.addToBackStack(null);
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
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, values, "project = ?", new String[]{project_name_text.toString()});
        database.close();
        //Come back to previous fragment
        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Utils.changeFragment(nextFragment, transaction, "", "");
        getFragmentManager().popBackStack();

        //transaction.addToBackStack(null);
    }

    private boolean isEmpty(TextView etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }


    public ArrayList<String> getProject(Context context, String project_name) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_PROJECT + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_name});
        cursor.moveToFirst();
        ArrayList<String> char_list = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            char_list.add(cursor.getString(cursor.getColumnIndex("project")));
            char_list.add(cursor.getString(cursor.getColumnIndex("genre")));
            char_list.add(cursor.getString(cursor.getColumnIndex("plot")));
            char_list.add(cursor.getString(cursor.getColumnIndex("image")));
            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
    }

    private void showProfileImageDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(myFragmentView.getContext(), R.style.bottom_dialog_theme);
        dialog.setContentView(R.layout.dialog_profile_image_selection);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        RelativeLayout tvSelectFromGallery = dialog.findViewById(R.id.select_from_gallery_container);
        RelativeLayout tvRemovePhoto= dialog.findViewById(R.id.remove_photo_container);

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
                project_icon_iv.setImageResource(R.drawable.writer_icon);
                dialog.dismiss();
            }
        });


        tvSelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo : check for permission

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
                    //TODO all granted
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
                            if(filepath!=null)
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

}
