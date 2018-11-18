package com.plotgen.rramirez.plotgenerator.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Model.Comment;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryDetailFragment extends Fragment {

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

    @BindView(R.id.lvComments)
    ListView lvComments;

    @BindView(R.id.fieldCommentText)
    MaterialEditText etCommentText;

    FirebaseListAdapter<Comment> adapter;
    FirebaseListOptions<Comment> options;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mCommentReference, mPostReference;

    @OnClick(R.id.buttonPostComment)
    public void postComment(View v)
    {
        Comment comment = new Comment(Common.currentUser.getUid(),
                Common.currentUser.getName(),
                Common.currentUser.getPicUrl().toString(),
                etCommentText.getText().toString());

        // Push the comment, it will appear in the list
        mCommentReference.push().setValue(comment);

        // Clear the field
        etCommentText.setText(null);
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

        mDatabase = FirebaseDatabase.getInstance();
        mPostReference = mDatabase.getReference().child("Weekly_Challenge_test").child("posts").child(Common.currentStory.getId());
        mCommentReference = mDatabase.getReference().child("Weekly_Challenge_test").child("post-comments").child(Common.currentStory.getId());

        ivTemplatePic.setBackgroundColor(new Random().nextInt());

        tvTitle.setText(Common.currentStory.getTitle());
        tvGenre.setText(Common.currentStory.getGenre());
        tvStory.setText(Common.currentStory.getChalenge());

        ivLoves.setImageResource(R.drawable.ic_love_red);

        //tvLoves.setText(String.valueOf(Common.currentStory.getLike()));

        options = new FirebaseListOptions.Builder<Comment>()
                .setQuery(mCommentReference, Comment.class)
                .setLayout(R.layout.item_comment)
                .build();

        populateComments();

        return view;
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

        lvComments.setAdapter(adapter);
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
        }
        else
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

}
