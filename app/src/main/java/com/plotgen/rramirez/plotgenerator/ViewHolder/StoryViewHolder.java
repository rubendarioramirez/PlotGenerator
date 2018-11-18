package com.plotgen.rramirez.plotgenerator.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;

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


    public StoryViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(Story story, View.OnClickListener starClickListener) {

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
        tvComments.setText(String.valueOf(story.getCommentCount()));

        ivLoves.setOnClickListener(starClickListener);
    }
}