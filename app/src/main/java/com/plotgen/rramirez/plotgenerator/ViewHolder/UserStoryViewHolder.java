package com.plotgen.rramirez.plotgenerator.ViewHolder;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;
import com.plotgen.rramirez.plotgenerator.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserStoryViewHolder extends RecyclerView.ViewHolder {

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

    public UserStoryViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(UserStory story, View.OnClickListener likeClickListener,
                           DocumentReference commentRef) {
        if (story.getUser() != null)
            Glide.with(itemView.getContext())
                    .load(story.getUser().getUriString())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivUser);

        ivTemplatePic.setImageResource(R.drawable.typewriter);
        if (story.getUser() != null)
            tvUser.setText(story.getUser().getName());
        tvTitle.setText(story.getProjectName());
        tvGenre.setText(story.getGenre());
        /*tvStory.setText(story.getStory());*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvStory.setText(Html.fromHtml(story.getStory(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvStory.setText(Html.fromHtml(story.getStory()));
        }
        tvLoves.setText(String.valueOf(story.getLikeCount()));
        ivLoves.setOnClickListener(likeClickListener);
      /*        tvShare.setOnClickListener(shareListener);
        ivShare.setOnClickListener(shareListener);*/

        Log.v("matilda", "Called bindToPost");

        commentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    int commentCount = 0;
                    if (documentSnapshot.getData()!=null){
                        commentCount = documentSnapshot.getData().size();
                    }
                    tvComments.setText(String.valueOf(commentCount));
                }

            }

          /*  @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int commentCount = 0;
                for (DataSnapshot commentData : dataSnapshot.getChildren()) {
                    commentCount++;
                }

                tvComments.setText(String.valueOf(commentCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }*/
        });
    }

    public void removeItem() {

        itemStoryLayout.removeAllViewsInLayout();
    }
}
