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
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Model.Comment;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;
import com.plotgen.rramirez.plotgenerator.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserStoryDetailFragment extends Fragment {

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

    private DatabaseReference mCommentReference, mReference;
    private DatabaseReference mUserReference;

    public UserStoryDetailFragment() {
        // Required empty public constructor
    }

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
        /*sendNotification(Common.currentUser.getName() + " commented on your post",
                Common.currentUserStory.getUser(), Common.currentUserStory.getId());*/
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
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                            shareIntent.putExtra(Intent.EXTRA_STREAM, getString(R.string.invitation_message));
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, "Share Post"));

                        }
                    }
                });
    }

    private Uri createShareUri() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("com.plotgen.rramirez")
                .appendPath("post")
                .appendQueryParameter("id", Common.currentUserStory.getId());
        return builder.build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_detail, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this, view);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("stories").child(Common.currentUserStory.getId());
        mCommentReference = mDatabase.getReference().child("stories").child("post-comments").child(Common.currentUserStory.getId());

        mUserReference = mDatabase.getReference().child("users");

        ivTemplatePic.setImageResource(R.drawable.typewriter);

        tvTitle.setText(Common.currentUserStory.getProjectName());
        tvGenre.setText(Common.currentUserStory.getGenre());
        tvStory.setText(Common.currentUserStory.getStory());

        if (Common.currentUserStory.likes.containsKey(Common.currentUser.getUid())) {
            ivLoves.setImageResource(R.drawable.ic_love_red);
        } else {
            ivLoves.setImageResource(R.drawable.ic_love_outline);
        }

        ivLoves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLikeClicked(mReference);
            }
        });
        tvLoves.setText(String.valueOf(Common.currentUserStory.getLikeCount()));

        options = new FirebaseListOptions.Builder<Comment>()
                .setQuery(mCommentReference, Comment.class)
                .setLayout(R.layout.item_comment)
                .build();

        populateComments();
        checkCommentCount();

        return view;
    }

    private void onLikeClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                UserStory p = mutableData.getValue(UserStory.class);
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
                    //  sendNotification(Common.currentUser.getName() + " liked your post", p.getUser(), postRef.getKey());
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                Common.tempUserStory = p;
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d("UpdateLikeCount", "postTransaction:onComplete:" + databaseError);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Common.tempUserStory.likes.containsKey(Common.currentUser.getUid())) {
                            ivLoves.setImageResource(R.drawable.ic_love_red);
                        } else {
                            ivLoves.setImageResource(R.drawable.ic_love_outline);
                        }
                        tvLoves.setText(String.valueOf(Common.tempUserStory.getLikeCount()));
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
                    new Notify(token, message, id, "Stories").execute();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getActivity().getApplicationContext())
                .load(getHiResUrl(Common.currentUserStory.getUser().getUriString()))
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

            return photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
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

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Common.currentUserStory != null && Common.currentUserStory.getUser() != null)
            if (Common.currentUserStory.getUser().getUid().equals(Common.currentUser.getUid()))
                setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            String values = Common.currentUserStory.getId() + "_" + Common.currentUserStory.getProjectName();

            OfflineStoryFragment nextFragment = new OfflineStoryFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Utils.changeFragment(nextFragment, transaction, "project_info", values);
            transaction.addToBackStack(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }
}
