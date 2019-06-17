package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        return new Adapter_herojourney.myViewHolder_heroJourney(v, mContext, mData);
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

    //    public class myViewHolder_heroJourney extends RecyclerView.ViewHolder implements View.OnClickListener{
    public class myViewHolder_heroJourney extends RecyclerView.ViewHolder {
        ImageView card_background_herojourney;
        TextView hero_journey_card_act, hero_journey_card_title, hero_journey_card_desc;
        Context mContext;
        List<item_herojourney> mData;

        public myViewHolder_heroJourney(View itemView, Context mContext, List<item_herojourney> mData) {
            super(itemView);
            card_background_herojourney = itemView.findViewById(R.id.card_background_herojourney);
            hero_journey_card_act = itemView.findViewById(R.id.hero_journey_card_act);
            hero_journey_card_title = itemView.findViewById(R.id.hero_journey_card_title);
            hero_journey_card_desc = itemView.findViewById(R.id.hero_journey_card_desc);
            this.mContext = mContext;
            this.mData = mData;
        }
    }
}




