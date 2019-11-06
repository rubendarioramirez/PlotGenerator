package com.plotgen.rramirez.plotgenerator.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.plotgen.rramirez.plotgenerator.ChallengeTemplateFragment;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Model.Challenge;
import com.plotgen.rramirez.plotgenerator.Model.Project;
import com.plotgen.rramirez.plotgenerator.Project_detailsFragment;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Timeline_detail;
import com.plotgen.rramirez.plotgenerator.items.item_challenge_list;
import com.plotgen.rramirez.plotgenerator.items.item_timeline;

import java.util.List;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_challengeList extends RecyclerView.Adapter<Adapter_challengeList.myViewHolder> {

    Context mContext;
    List<item_challenge_list> mData;

    public Adapter_challengeList(Context mContext, List<item_challenge_list> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.challenge_list_item, parent, false);
        return new myViewHolder(v,mData);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.challenge_item_title.setText(mData.get(position).getChallenge_title());
        holder.challenge_item_recommendedAct.setText(mData.get(position).getChallenge_recommendedAct());
        holder.challenge_item_description.setText(mData.get(position).getChallenge_description());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView challenge_item_title;
        TextView challenge_item_recommendedAct;
        TextView challenge_item_description;
        List<item_challenge_list> mData;
        MaterialButton challenge_item_btn;

        public myViewHolder(View itemView, List<item_challenge_list> mData){
            super (itemView);
            challenge_item_title = itemView.findViewById(R.id.challenge_item_title);
            challenge_item_description = itemView.findViewById(R.id.challenge_item_description);
            challenge_item_recommendedAct = itemView.findViewById(R.id.challenge_item_recommendedAct);
            challenge_item_btn = itemView.findViewById(R.id.challenge_item_btn);

            this.mData = mData;

            challenge_item_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Common.currentChallenge = new Challenge(mData.get(getAdapterPosition()).getChallenge_id(),mData.get(getAdapterPosition()).getChallenge_title());
                    nextFragment(mContext);
                }
            });
        }
    }

    public void nextFragment(Context context){
        ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
        //Make the transaction
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.addToBackStack(null);
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();
    }
}
