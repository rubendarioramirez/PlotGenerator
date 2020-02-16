package com.plotgen.rramirez.plotgenerator.ViewHolder;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineStoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvTitle)
    public TextView tvTitle;
    @BindView(R.id.tvGenre)
    public TextView tvGenre;
    @BindView(R.id.tvStory)
    public TextView tvStory;
    @BindView(R.id.tvUser)
    public TextView tvUser;
    @BindView(R.id.ivTemplatePic)
    public ImageView ivTemplatePic;
    @BindView(R.id.ivUser)
    public ImageView ivUser;
    @BindView(R.id.itemStoryLayout)
    public RelativeLayout itemStoryLayout;

    public OfflineStoryViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

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


    }

    public void removeItem() {

        itemStoryLayout.removeAllViewsInLayout();
    }

    public void bindToPost(DocumentReference document, Story story) {
        tvUser.setText(story.getUser().getName());
        ivTemplatePic.setImageResource(R.drawable.typewriter);
        tvTitle.setText(document.getId());
        tvStory.setText(Html.fromHtml(story.getChalenge()));
        tvGenre.setText(story.getGenre());
        Glide.with(itemView.getContext())
                .load(story.getUser().getUriString())
                .apply(RequestOptions.circleCropTransform())
                .into(ivUser);
    }
}
