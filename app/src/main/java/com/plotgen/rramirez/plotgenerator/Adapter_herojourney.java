package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.plotgen.rramirez.plotgenerator.Common.Common;

import java.util.List;

/**
 * Created by macintosh on 23/08/18.
 */



public class Adapter_herojourney extends RecyclerView.Adapter<Adapter_herojourney.myViewHolder_heroJourney> implements RewardedVideoAdListener {

    Context mContext;
    List<item_herojourney> mData;
    public int challenge_unlock = 0;
    private RewardedVideoAd mRewardedVideoAd;

    public Adapter_herojourney(Context mContext, List<item_herojourney> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @Override
    public myViewHolder_heroJourney onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.hero_jouney_card_item, parent, false);

        if(!Common.isPAU) {
            //Rewarded ad
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            loadRewardedVideoAd(mContext);
        }
        else
            challenge_unlock = 1;

        return new Adapter_herojourney.myViewHolder_heroJourney(v, mContext,mData);
    }

    @Override
    public void onBindViewHolder(myViewHolder_heroJourney holder, int position) {
        holder.card_background_herojourney.setImageResource(mData.get(position).getBackground());
        holder.hero_journey_card_title.setText(mData.get(position).getHerojourney_title());
        holder.hero_journey_card_act.setText(mData.get(position).getHerojourney_act());
        holder.hero_journey_card_desc.setText(mData.get(position).getHerojourney_desc());



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder_heroJourney extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView card_background_herojourney;
        TextView hero_journey_card_act, hero_journey_card_title, hero_journey_card_desc;
        Context mContext;
        List<item_herojourney> mData;

        public myViewHolder_heroJourney(View itemView, Context mContext, List<item_herojourney> mData){
            super (itemView);
            card_background_herojourney = itemView.findViewById(R.id.card_background_herojourney);
            hero_journey_card_act = itemView.findViewById(R.id.hero_journey_card_act);
            hero_journey_card_title = itemView.findViewById(R.id.hero_journey_card_title);
            hero_journey_card_desc = itemView.findViewById(R.id.hero_journey_card_desc);
            this.mContext = mContext;
            this.mData = mData;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            String clicked = mData.get(getAdapterPosition()).getHerojourney_title();
            String charName = mData.get(getAdapterPosition()).getHerojourneyChar();
            String projectName = mData.get(getAdapterPosition()).getHerojourneyProject();
            if(clicked.equals(view.getContext().getResources().getString(R.string.challenge_1_title))){
                nextFragment(mContext,charName,projectName,clicked);
            } else if (clicked.equals(view.getContext().getResources().getString(R.string.challenge_2_title))){
                nextFragment(mContext,charName,projectName,clicked);
            } else if (clicked.equals(view.getContext().getResources().getString(R.string.challenge_3_title))){
                nextFragment(mContext,charName,projectName,clicked);
            } else if (clicked.equals(view.getContext().getResources().getString(R.string.challenge_4_title))){
                nextFragment(mContext,charName,projectName,clicked);
            }  else if (clicked.equals(view.getContext().getResources().getString(R.string.challenge_5_title))) {
                nextFragment(mContext,charName,projectName,clicked);
            } else if (clicked.equals(view.getContext().getResources().getString(R.string.challenge_6_title))) {
                nextFragment(mContext,charName,projectName,clicked);
            }
            else if (clicked.equals(view.getContext().getResources().getString(R.string.c1_mentor_title))) {
                if(challenge_unlock == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }
                }else {
                    nextFragment(mContext,charName,projectName,clicked);
                }
            }else if (clicked.equals(view.getContext().getResources().getString(R.string.c1_antagonist_title))) {
                if(challenge_unlock == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }
                }else {
                    nextFragment(mContext,charName,projectName,clicked);
                }
            }else if (clicked.equals(view.getContext().getResources().getString(R.string.c1_sidekick_title))) {
                if(challenge_unlock == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }
                }else {
                    nextFragment(mContext,charName,projectName,clicked);
                }
            }
            else if (clicked.equals(view.getContext().getResources().getString(R.string.char_guide_title))){
                    //Send it to the next fragment
                    GuideRoleFragment nextFragment = new GuideRoleFragment();
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }else if (clicked.equals(view.getContext().getResources().getString(R.string.lajos_character_title))){
                    //Send it to the next fragment
                    GuideLajosFragment nextFragment = new GuideLajosFragment();
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }else if (clicked.equals(view.getContext().getResources().getString(R.string.change_arc_title))){
                    //Send it to the next fragment
                    GuideWeilandFragment nextFragment = new GuideWeilandFragment();
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }else if (clicked.equals(view.getContext().getResources().getString(R.string.antagonist_guide_title))){
                //Send it to the next fragment
                AntagonistFragment nextFragment = new AntagonistFragment();
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            }



        }
    }

    public void nextFragment(Context context, String charName,String projectName,String clicked){
        Bundle bundle = new Bundle();
        bundle.putString("char_name",charName);
        bundle.putString("project_name",projectName);
        bundle.putString("challenge_number",clicked);
        //Send it to the next fragment
        ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
        nextFragment.setArguments(bundle);
        //Make the transaction
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        //transaction.addToBackStack(null);
        transaction.replace(R.id.flMain, nextFragment);
        transaction.commit();
        }

    private void loadRewardedVideoAd(Context mContext) {
        mRewardedVideoAd.loadAd(mContext.getString(R.string.reward_ad_challenge),
                new AdRequest.Builder()
                        .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                        .build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd(mContext);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Get the reward
        challenge_unlock = 1;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

}
