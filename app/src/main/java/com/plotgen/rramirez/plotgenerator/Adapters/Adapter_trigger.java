package com.plotgen.rramirez.plotgenerator.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.items.item_trigger;

import java.util.List;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_trigger extends RecyclerView.Adapter<Adapter_trigger.myViewHolder> {

    Context mContext;
    List<item_trigger> mData;

    public Adapter_trigger(Context mContext, List<item_trigger> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.trigger_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.card_background.setImageResource(mData.get(position).getBackground());
        holder.trigger_title.setText(mData.get(position).getTrigger_title());
        holder.trigger_author.setText(mData.get(position).getTrigger_author());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView card_background;
        TextView trigger_title;
        TextView trigger_author;
        public myViewHolder(View itemView){
            super (itemView);
            card_background = itemView.findViewById(R.id.card_background_herojourney);
            trigger_title = itemView.findViewById(R.id.trigger_title);
            trigger_author = itemView.findViewById(R.id.trigger_author);
        }
    }
}
