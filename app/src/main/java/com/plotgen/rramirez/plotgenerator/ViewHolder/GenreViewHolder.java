package com.plotgen.rramirez.plotgenerator.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.plotgen.rramirez.plotgenerator.Model.Genre;
import com.plotgen.rramirez.plotgenerator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.genre)
    public TextView tvGenre;

    @BindView(R.id.itemGenreLayout)
    public RelativeLayout itemGenreLayout;


    public GenreViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(DatabaseReference genreRef) {

        tvGenre.setText(genreRef.getKey());

    }

}
