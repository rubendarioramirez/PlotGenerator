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
 * Created by macintosh on 22/08/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    Context mContext;
    List<item> mData;

    public Adapter(Context mContext, List<item> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.card_background.setImageResource(mData.get(position).getBackground());
        holder.trigger_title.setText(mData.get(position).getTrigger_title());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView card_background;
        TextView trigger_title;


        public myViewHolder(View itemView){
            super (itemView);
            card_background = itemView.findViewById(R.id.card_background_herojourney);
            trigger_title = itemView.findViewById(R.id.trigger_title);
        }

    }
}
