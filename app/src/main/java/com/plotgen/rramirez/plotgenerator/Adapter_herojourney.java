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

import java.util.List;

/**
 * Created by macintosh on 23/08/18.
 */



public class Adapter_herojourney extends RecyclerView.Adapter<Adapter_herojourney.myViewHolder_heroJourney> {

    Context mContext;
    List<item_herojourney> mData;

    public Adapter_herojourney(Context mContext, List<item_herojourney> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @Override
    public myViewHolder_heroJourney onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.hero_jouney_card_item, parent, false);

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
            if(clicked.equals("Challenge I") || clicked.equals("Desafio I")){
                //Send it to the next fragment
                Bundle bundle = new Bundle();
                bundle.putString("char_name",charName);
                bundle.putString("project_name",projectName);
                //Send it to the next fragment
                ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            }


        }
    }
}
