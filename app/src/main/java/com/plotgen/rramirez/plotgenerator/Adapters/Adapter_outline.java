package com.plotgen.rramirez.plotgenerator.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.plotgen.rramirez.plotgenerator.CharListFragment;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Outline_detail;
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
        return new myViewHolder(v,mData);
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

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView outline_title;
        TextView outline_description;
        TextView outline_characters;
        List<item_outline> mData;

        public myViewHolder(View itemView, List<item_outline> mData){
            super (itemView);
            outline_title = itemView.findViewById(R.id.outline_title);
            outline_description = itemView.findViewById(R.id.outline_description);
            outline_characters = itemView.findViewById(R.id.outline_characters);

            this.mData = mData;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.v("Matilda", "Clicked from Adapter at ID: " + mData.get(getAdapterPosition()).getOutline_id());
            Common.currentOutlineID = mData.get(getAdapterPosition()).getOutline_id();
            Common.outlineCreationMode = false;
            nextFragment(mContext);

        }
    }

    public void nextFragment(Context context){
        Outline_detail nextFragment = new Outline_detail();
        //Make the transaction
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.addToBackStack(null);
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();
    }
}
