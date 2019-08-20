package com.plotgen.rramirez.plotgenerator.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.items.item_outline;
import com.plotgen.rramirez.plotgenerator.items.item_trigger;

import java.util.List;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_outline extends RecyclerView.Adapter<Adapter_outline.myViewHolder> {

    Context mContext;
    List<item_outline> mData;

    public Adapter_outline(Context mContext, List<item_outline> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.outline_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.outline_title.setText(mData.get(position).getOutline_title());
        holder.outline_description.setText(mData.get(position).getOutline_description());
        holder.outline_characters.setText(mData.get(position).getOutline_characters());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView outline_title;
        TextView outline_description;
        TextView outline_characters;
        public myViewHolder(View itemView){
            super (itemView);
            outline_title = itemView.findViewById(R.id.outline_title);
            outline_description = itemView.findViewById(R.id.outline_description);
            outline_characters = itemView.findViewById(R.id.outline_characters);
        }
    }
}
