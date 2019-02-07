package com.plotgen.rramirez.plotgenerator.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.storage.StorageReference;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Notify;
import com.plotgen.rramirez.plotgenerator.Model.Comment;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryDetailFragment extends Fragment {

    private static final int REQUEST_INVITE = 101;
    @BindView(R.id.ivTemplatePic)
    ImageView ivTemplatePic;

    @BindView(R.id.ivUserPicDetail)
    ImageView ivUserPicDetail;

    @BindView(R.id.ivLoves)
    ImageView ivLoves;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvGenre)
    TextView tvGenre;

    @BindView(R.id.tvStory)
    TextView tvStory;

    @BindView(R.id.tvLoves)
    TextView tvLoves;

    @BindView(R.id.tvComments)
    TextView tvComments;

    @BindView(R.id.lvComments)
    ExpandableHeightListView lvComments;

    @BindView(R.id.fieldCommentText)
    MaterialEditText etCommentText;

    FirebaseListAdapter<Comment> adapter;
    FirebaseListOptions<Comment> options;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mCommentReference, mPostReference;
    private DatabaseReference mUserReference;

    @OnClick(R.id.buttonPostComment)
    public void postComment(View v) {
        final String s = etCommentText.getText().toString();

        // Comment is required
        if (TextUtils.isEmpty(s)) {
            etCommentText.setError("Required");
            return;
        }

        final Comment comment = new Comment(Common.currentUser.getUid(),
                Common.currentUser.getName(),
                Common.currentUser.getPicUrl().toString(),
                etCommentText.getText().toString());

        // Push the comment, it will appear in the list
        mCommentReference.push().setValue(comment);
        if (comment != null) {
            sendNotification(Common.currentUser.getName() + " commented on your post",
                    Common.currentStory.getUser(), Common.currentStory.getId());
        }
        // Clear the field
        etCommentText.setText(null);
    }


    @OnClick({R.id.tvshare, R.id.ivshare})
    public void share() {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(createShareUri())
                .setDomainUriPrefix("https://plotgen.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.v("short link", String.valueOf(Uri.decode(shortLink + "")));
                            Log.v("preview link", String.valueOf(Uri.decode(flowchartLink + "")));
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invitation_message) + " " + String.valueOf(shortLink));
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                            shareIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.invitation_message));
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, "Share Post"));

                        } else {
                            // Error
                            // ...
                        }
                    }
                });
    }

    private Uri createShareUri() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("com.plotgen.rramirez")
                .appendPath("post")
                .appendQueryParameter("id", Common.currentStory.getId());
        return builder.build();
    }

    public StoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_detail, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this, view);

        mDatabase = Common.currentDatabase;
        mPostReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts").child(Common.currentStory.getId());
        mCommentReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("post-comments").child(Common.currentStory.getId());
        mUserReference = mDatabase.getReference().child("users");

//        mPostReference = mDatabase.getReference().child("Weekly_Challenge").child("posts").child(Common.currentStory.getId());
//        mCommentReference = mDatabase.getReference().child("Weekly_Challenge").child("post-comments").child(Common.currentStory.getId());

        ivTemplatePic.setImageResource(R.drawable.typewriter);

        tvTitle.setText(Common.currentStory.getTitle());
        tvGenre.setText(Common.currentStory.getGenre());
        tvStory.setText(Common.currentStory.getChalenge());

        if (Common.currentStory.likes.containsKey(Common.currentUser.getUid())) {
            ivLoves.setImageResource(R.drawable.ic_love_red);
        } else {
            ivLoves.setImageResource(R.drawable.ic_love_outline);
        }

        ivLoves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLikeClicked(mPostReference);
            }
        });
        tvLoves.setText(String.valueOf(Common.currentStory.getLikeCount()));

        options = new FirebaseListOptions.Builder<Comment>()
                .setQuery(mCommentReference, Comment.class)
                .setLayout(R.layout.item_comment)
                .build();

        populateComments();
        checkCommentCount();

        return view;
    }

    private void onLikeClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Story p = mutableData.getValue(Story.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.likes.containsKey(Common.currentUser.getUid())) {
                    // Unstar the post and remove self from stars
                    p.likeCount = p.likeCount - 1;
                    p.likes.remove(Common.currentUser.getUid());
                } else {
                    // Star the post and add self to stars
                    p.likeCount = p.likeCount + 1;
                    p.likes.put(Common.currentUser.getUid(), true);
                    sendNotification(Common.currentUser.getName() + " liked your post", p.getUser(), p.getId());
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                Common.tempStory = p;
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d("UpdateLikeCount", "postTransaction:onComplete:" + databaseError);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Common.tempStory.likes.containsKey(Common.currentUser.getUid())) {
                            ivLoves.setImageResource(R.drawable.ic_love_red);
                        } else {
                            ivLoves.setImageResource(R.drawable.ic_love_outline);
                        }
                        tvLoves.setText(String.valueOf(Common.tempStory.getLikeCount()));
                    }
                });
            }
        });
    }

    public void sendNotification(final String message, User user, final String id) {
        //String firebase_token = mUserReference.child(user.getUid()).child("token");
        Query query = mUserReference.child(user.getUid()).orderByChild("token");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("token")) {
                    String token = dataSnapshot.child("token").getValue().toString();
                    String to = token; // the notification key
                    AtomicInteger msgId = new AtomicInteger();
                    new Notify(to, message, id,"Weekly Challenge").execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("notification action", databaseError.getDetails());
            }
        });

    }

    private void checkCommentCount() {
        mCommentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int commentCount = 0;
                for (DataSnapshot commentData : dataSnapshot.getChildren()) {
                    commentCount++;
                }

                tvComments.setText(String.valueOf(commentCount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateComments() {
        adapter = new FirebaseListAdapter<Comment>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Comment model, int position) {

                ImageView ivCommentPic;
                TextView tvCommentUser, tvCommentBody;

                ivCommentPic = v.findViewById(R.id.ivCommentPic);
                tvCommentUser = v.findViewById(R.id.tvCommentUser);
                tvCommentBody = v.findViewById(R.id.tvCommentBody);

                Glide.with(getActivity().getApplicationContext())
                        .load(model.getUserPic())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivCommentPic);

                tvCommentUser.setText(model.getUserName());
                tvCommentBody.setText(model.getUserComment());

            }
        };
        lvComments.setFocusable(false);
        lvComments.setAdapter(adapter);
        lvComments.setExpanded(true);
    }

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getActivity().getApplicationContext())
                .load(getHiResUrl(Common.currentStory.getUser().getUriString()))
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserPicDetail);
    }

    private String getHiResUrl(String s) {

        // Variable holding the original String portion of the url that will be replaced
        String originalPieceOfUrl = "s96-c/photo.jpg";

        // Variable holding the new String portion of the url that does the replacing, to improve image quality
        String newPieceOfUrlToAdd = "s400-c/photo.jpg";

        // Check if the Url path is null
        if (s != null) {

            // Convert the Url to a String and store into a variable
            String photoPath = s.toString();

            // Replace the original part of the Url with the new part
            String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);

            return newString;
        } else
            return s;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Common.currentStory != null && Common.currentStory.getUser() != null)
            if (Common.currentStory.getUser().getUid().equals(Common.currentUser.getUid()))
                setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            StoryEditFragment nextFragment = new StoryEditFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Utils.changeFragment(nextFragment, transaction);
            transaction.addToBackStack(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
