package com.plotgen.rramirez.plotgenerator.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.plotgen.rramirez.plotgenerator.Model.Comment;
import com.plotgen.rramirez.plotgenerator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvCommentUser)
    public TextView tvCommentUser;

    @BindView(R.id.ivCommentPic)
    public ImageView ivCommentPic;

    @BindView(R.id.tvCommentBody)
    public TextView tvCommentBody;



    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindToPost(Comment comment){
        Glide.with(itemView.getContext())
                .load(comment.getUserPic())
                .apply(RequestOptions.circleCropTransform())
                .into(ivCommentPic);

        tvCommentUser.setText(comment.getUserName());
        tvCommentBody.setText(comment.getUserComment());
    }
}
