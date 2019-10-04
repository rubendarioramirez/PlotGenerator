package com.plotgen.rramirez.plotgenerator.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvTitle)
    public TextView tvTitle;
    @BindView(R.id.tvGenre)
    public TextView tvGenre;
    @BindView(R.id.tvStory)
    public TextView tvStory;
    @BindView(R.id.tvUser)
    public TextView tvUser;
    @BindView(R.id.tvLoves)
    public TextView tvLoves;
    @BindView(R.id.tvComments)
    public TextView tvComments;
    @BindView(R.id.ivTemplatePic)
    public ImageView ivTemplatePic;
    @BindView(R.id.ivUser)
    public ImageView ivUser;
    @BindView(R.id.ivLoves)
    public ImageView ivLoves;
/*    @BindView(R.id.tvshare)
    public TextView tvShare;
    @BindView(R.id.ivshare)
    public ImageView ivShare;*/
    @BindView(R.id.itemStoryLayout)
    public RelativeLayout itemStoryLayout;

    public StoryViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    /*public void bindToPost(Story story, View.OnClickListener likeClickListener,
                            DatabaseReference commentRef) {

        Glide.with(itemView.getContext())
                .load(story.getUser().getUriString())
                .apply(RequestOptions.circleCropTransform())
                .into(ivUser);

        ivTemplatePic.setImageResource(R.drawable.typewriter);
        tvUser.setText(story.getUser().getName());
        tvTitle.setText(story.getTitle());
        tvGenre.setText(story.getGenre());
        tvStory.setText(story.getChalenge());
        tvLoves.setText(String.valueOf(story.getLikeCount()));
        ivLoves.setOnClickListener(likeClickListener);
*//*        tvShare.setOnClickListener(shareListener);
        ivShare.setOnClickListener(shareListener);*//*

        Log.v("matilda", "Called bindToPost");

        commentRef.addValueEventListener(new ValueEventListener() {
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
    }*/

    public void bindToPost(Story story, View.OnClickListener likeClickListener,
                           CollectionReference commentRef) {

        Glide.with(itemView.getContext())
                .load(story.getUser().getUriString())
                .apply(RequestOptions.circleCropTransform())
                .into(ivUser);

        ivTemplatePic.setImageResource(R.drawable.typewriter);
        tvUser.setText(story.getUser().getName());
        tvTitle.setText(story.getTitle());
        tvGenre.setText(story.getGenre());
        tvStory.setText(story.getChalenge());
        tvLoves.setText(String.valueOf(story.getLikeCount()));
        ivLoves.setOnClickListener(likeClickListener);
/*        tvShare.setOnClickListener(shareListener);
        ivShare.setOnClickListener(shareListener);*/

        Log.v("matilda", "Called bindToPost");

        commentRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int commentCount = 0;
                for (DocumentSnapshot commentData : queryDocumentSnapshots.getDocuments()) {
                    commentCount++;
                }
                tvComments.setText(String.valueOf(commentCount));
            }
        });

    }




    public void removeItem() {

        itemStoryLayout.removeAllViewsInLayout();
    }
}
