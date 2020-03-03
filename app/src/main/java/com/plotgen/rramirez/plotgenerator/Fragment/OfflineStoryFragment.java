package com.plotgen.rramirez.plotgenerator.Fragment;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.dBHelper;
import com.plotgen.rramirez.plotgenerator.Model.Genre;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;
import com.plotgen.rramirez.plotgenerator.R;
import com.google.firebase.firestore.EventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineStoryFragment extends Fragment {

    private static final int RC_SIGN_IN = 123;
    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.formatBold)
    ImageView ivBold;
    @BindView(R.id.formatItalic)
    ImageView ivItalic;
    @BindView(R.id.formatAlignLeft)
    ImageView ivAlignLeft;
    @BindView(R.id.formatAlignCenter)
    ImageView ivAlignCenter;
    @BindView(R.id.formatAlignRight)
    ImageView ivAlignRight;

    private boolean isUpdate = false;
    private String project_name, project_id;
    private String mStory;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CollectionReference mReference;

    private FirebaseAnalytics mFirebaseAnalytics;

    public OfflineStoryFragment() {
        // Required empty public constructor
    }

    public void saveStoryToDB() {
        SQLiteDatabase database = new dBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dBHelper.STORY_COLUMN_PROJECT, project_name);
        values.put(dBHelper.STORY_COLUMN_PROJECT_ID, project_id);
        values.put(dBHelper.STORY_COLUMN_STORIES, mStory);

        if (isUpdate)
            database.update(dBHelper.CHARACTER_TABLE_STORY, values, "project_id = ?", new String[]{project_id});
        else
            database.insert(dBHelper.CHARACTER_TABLE_STORY, null, values);
    }

    @OnClick(R.id.formatBold)
    public void setBold(View v) {
        mEditor.setBold();
    }

    @OnClick(R.id.formatItalic)
    public void setItalic(View v) {
        mEditor.setItalic();
    }

    @OnClick(R.id.formatAlignLeft)
    public void setAlignLeft(View v) {
        mEditor.setAlignLeft();
    }

    @OnClick(R.id.formatAlignCenter)
    public void setAlignCenter(View v) {
        mEditor.setAlignCenter();
    }

    @OnClick(R.id.formatAlignRight)
    public void setAlignRight(View v) {
        mEditor.setAlignRight();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offline_story, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(Objects.requireNonNull(getContext()));

        try {
            project_name = Common.currentProject.getName();
            project_id = Common.currentProject.getId();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = Common.currentDatabase;
        mReference = mDatabase.collection("stories");


        mUser = mAuth.getCurrentUser();

        //Make sure ProjectID is not null
        if(project_id !=null) {
            mStory = getStoryFromDB(view.getContext(), project_id);
        } else {
            mStory = "";
        }


        //publish button
        FloatingActionButton fabPublish = view.findViewById(R.id.btnPublish);
        fabPublish.setVisibility(View.INVISIBLE);


        DocumentReference mDocRef;
        if(mUser != null){
            String email = mUser.getEmail();
            mDocRef = FirebaseFirestore.getInstance().document("writers/" + email);
            mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        Boolean publisher = documentSnapshot.getBoolean("publisher");
                        if(publisher)
                            fabPublish.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        fabPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    mDatabase = FirebaseFirestore.getInstance();
                    mUser = Common.currentFirebaseUser;
                    CollectionReference collectionReference = mReference;
                    String key = mReference.document().getId();

                    Long tsLong = System.currentTimeMillis() / 1000;
                    Story story = new Story(key, project_name, project_id,
                            Common.currentProject.getGenre(), mStory, tsLong,
                            new User(Common.currentUser.getUid(),
                                    Common.currentUser.getName(),
                                    Common.currentUser.getEmail(),
                                    Common.currentUser.getPicUrl().toString()), 0, true);

                    Map<String, Object> storyValues = story.toMap();

                    String publishTitle = createTransactionID() + Common.currentUser.getEmail();
                    collectionReference.document(publishTitle).set(storyValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), " Story Added", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });

        if (!Common.isPAU) {
            AdView mAdView = (AdView) view.findViewById(R.id.adView_offile_story);
            AdsHelper.loadAd(mAdView);
        }

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(16);
        int selectedTheme = Utils.getSharePref(view.getContext(), "selectedTheme", 0);
        if (selectedTheme == 0) {
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background));
            mEditor.setBackgroundColor(getResources().getColor(R.color.background));
        } else if (selectedTheme == 1) {
            //DARK THEME
            mEditor.setEditorFontColor(Color.WHITE);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background_2));
            mEditor.setTextBackgroundColor(getResources().getColor(R.color.background_2));
        } else if (selectedTheme == 2) {
            //ROMANCE THEME
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background_3));
            mEditor.setTextBackgroundColor(getResources().getColor(R.color.background_3));
        } else if (selectedTheme == 3) {
            //AUTUMN THEME
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background_4));
            mEditor.setTextBackgroundColor(getResources().getColor(R.color.background_4));
        }

        mEditor.setPadding(10, 10, 10, 10);
        if (mStory.equals("")) {
            mEditor.setPlaceholder("Insert text here...");

        } else {
            isUpdate = true;
            mEditor.setHtml(mStory);
        }

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                //Log.v("matilda", "Characters are " + mEditor.getHtml().length());
                mStory = text;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                // submit story to firebase
                mUser = mAuth.getCurrentUser();
                //publishStory();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getContext(), "You cancelled", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getContext(), "Unknown error, please try again", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private String getStoryFromDB(Context context, String project_id) {
        dBHelper myhelper = new dBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + dBHelper.CHARACTER_TABLE_STORY + " WHERE project_id = ?";
        String s = "";
        ArrayList<String> story_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{project_id});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                story_list.add(cursor.getString(cursor.getColumnIndex("project")));
                story_list.add(cursor.getString(cursor.getColumnIndex("project_id")));
                story_list.add(cursor.getString(cursor.getColumnIndex("stories")));

                s = cursor.getString(cursor.getColumnIndex("stories"));
                cursor.moveToNext();
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception e){
            Log.v("matilda", "The exception at OfflineStory is "  + e.toString());
        }
        return s;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_story_save) {
            //Send it to the next fragment
            try {
                saveStoryToDB();
                Toast.makeText(getContext(),"Saved successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        else if (id == R.id.menu_story_share) {
                //Send it to the next fragment
                try {
                    if(Common.isPAU)
                    {
                        SHARE(mEditor.getHtml(),Common.currentProject.getName());
                    } else
                    {
                        Toast.makeText(getContext(),getResources().getString(R.string.premiumOnly), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return true;
            }

        return super.onOptionsItemSelected(item);
    }

        public void SHARE(String body, String char_name) {
            // Do something in response to button
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Auctor:" + char_name);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
            startActivity(Intent.createChooser(sharingIntent, "share"));

            //Log challenges updated
            Bundle params = new Bundle();
            params.putString("Share", "completed");
            mFirebaseAnalytics.logEvent("share_completed", params);
        }

        public String createTransactionID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
