package com.plotgen.rramirez.plotgenerator.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.plotgen.rramirez.plotgenerator.CharListFragment;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Genre;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;
import com.plotgen.rramirez.plotgenerator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineStoryFragment extends Fragment implements RewardedVideoAdListener {

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
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RewardedVideoAd mRewardedVideoAd;
    private String language;
    private boolean canPublish = false;

    public OfflineStoryFragment() {
        // Required empty public constructor
    }

    public void saveStoryToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT, project_name);
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT_ID, project_id);
        values.put(mySQLiteDBHelper.STORY_COLUMN_STORIES, mStory);

        if (isUpdate)
            database.update(mySQLiteDBHelper.CHARACTER_TABLE_STORY, values, "project_id = ?", new String[]{project_id});
        else
            database.insert(mySQLiteDBHelper.CHARACTER_TABLE_STORY, null, values);
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("My Story");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offline_story, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ButterKnife.bind(this, view);


        try {
            project_name = Common.currentProject.getName();
            project_id = Common.currentProject.getId();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        //Make sure ProjectID is not null
        if(project_id !=null) {
            mStory = getStoryFromDB(view.getContext(), project_id);
        } else {
            mStory = "";
        }


        if (!Common.isPAU) {
            //Rewarded ad
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this.getContext());
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            loadRewardedVideoAd();
        } else {
            canPublish = true;
        }

        language = Locale.getDefault().getDisplayLanguage();

        //Save button
        FloatingActionButton fabSaveStory = view.findViewById(R.id.btnSaveStory);
        //publish button
        FloatingActionButton fabPublish = view.findViewById(R.id.btnPublish);
        fabPublish.setVisibility(View.VISIBLE);

        fabSaveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStoryToDB();
                changeFragment();

            }
        });

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
                    if (canPublish) {
                        publishStory();
                    } else {
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        }
                    }
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

                mStory = text;
            }
        });

        return view;
    }

    private void publishStory() {
        final ArrayList<String> project = Utils.getProject(this.getContext(), project_name);
        final DatabaseReference mStoriesDatabase = mDatabase.getReference().child("stories");
        Query myTopPostsQuery = mStoriesDatabase.child(mUser.getUid() + "_" + project_id);


        mStoriesDatabase.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String key = dataSnapshot.getKey();
                    Long tsLong = System.currentTimeMillis() / 1000;
                    UserStory story = new UserStory(key, project_name, project_id,
                            project.get(1), project.get(2), mStory, language, tsLong,
                            new User(Common.currentUser.getUid(),
                                    Common.currentUser.getName(),
                                    Common.currentUser.getEmail(),
                                    Common.currentUser.getPicUrl().toString()));

                    Genre genre = new Genre(Common.currentUser.getUid(), key, project.get(1), project_id);
                    Map<String, Object> genreValues = genre.toMap();
                    Map<String, Object> storyValues = story.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, storyValues);
                    childUpdates.put("/genre/" + project.get(1) + "/" + key, genreValues);

                      mStoriesDatabase.updateChildren(childUpdates);
                      Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();

                } else {
                    //  String key = mStoriesDatabase.push().getKey();
                    String key = mUser.getUid().concat("_" + project_id);
                    Long tsLong = System.currentTimeMillis() / 1000;

                    UserStory story = new UserStory(key, project_name, project_id,
                            project.get(1), project.get(2), mStory,language, tsLong,
                            new User(Common.currentUser.getUid(),
                                    Common.currentUser.getName(),
                                    Common.currentUser.getEmail(),
                                    Common.currentUser.getPicUrl().toString()));

                    Genre genre = new Genre(Common.currentUser.getUid(), key, project.get(1), project_id);
                    Map<String, Object> genreValues = genre.toMap();
                    Map<String, Object> storyValues = story.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, storyValues);
                    childUpdates.put("/genre/" + project.get(1) + "/" + key, genreValues);

                    mStoriesDatabase.updateChildren(childUpdates);
                    Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getDetails());

            }
        });

     /*   String key = mStoriesDatabase.push().getKey();
        Long tsLong = System.currentTimeMillis() / 1000;


        UserStory story = new UserStory(key, project_name, project_id,
                project.get(1), project.get(2), mStory, tsLong,
                new User(Common.currentUser.getUid(),
                        Common.currentUser.getName(),
                        Common.currentUser.getEmail(),
                        Common.currentUser.getPicUrl().toString()));

        Genre genre = new Genre(Common.currentUser.getUid(), key, project.get(1), project_id);
        Map<String, Object> genreValues = genre.toMap();
        Map<String, Object> storyValues = story.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, storyValues);
        childUpdates.put("/genre/" + project.get(1) + "/" + key, genreValues);

        mStoriesDatabase.updateChildren(childUpdates);*/

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
                publishStory();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getContext(), "You cancelled", Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getContext(), "Unknown error, please try again", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

   /* private void saveStoryToDB(View v) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT, project_name);
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT_ID, project_id);
        values.put(mySQLiteDBHelper.STORY_COLUMN_STORIES, mStory);
//      Auto saving
//    private final static int INTERVAL = 1000 * 60; //1 minutes
//    Handler mHandler = new Handler();
//
//    Runnable mHandlerTask = new Runnable()
//    {
//        @Override
//        public void run() {
//            saveStoryToDB();
//            Log.v("matilda", "running");
//            mHandler.postDelayed(mHandlerTask, INTERVAL);
//        }
//    };
//
//    void startRepeatingTask()
//    {
//        mHandlerTask.run();
//    }
//
//    void stopRepeatingTask()
//    {
//        mHandler.removeCallbacks(mHandlerTask);
//        Log.v("matilda", "stopped");
//    }
//
//    @Override
//    public void onDestroy () {
//
//        Log.v("matilda", "destroyed");
//        mHandler.removeCallbacks(mHandlerTask);
//        super.onDestroy ();
//
//
//    }


        if (isUpdate)
            database.update(mySQLiteDBHelper.CHARACTER_TABLE_STORY, values, "project = ?", new String[]{project_name});
        else
            database.insert(mySQLiteDBHelper.CHARACTER_TABLE_STORY, null, values);

        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment, transaction, "", "");
        getFragmentManager().popBackStack();

    }*/

    private String getStoryFromDB(Context context, String project_id) {
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_STORY + " WHERE project_id = ?";
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

    private void changeFragment() {
        CharListFragment nextFragment = new CharListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment, transaction);
        transaction.addToBackStack(null);
        // getFragmentManager().popBackStack();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.reward_ad_plot_gen),
                new AdRequest.Builder()
                        .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                        .build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Get the reward
        canPublish = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

}
