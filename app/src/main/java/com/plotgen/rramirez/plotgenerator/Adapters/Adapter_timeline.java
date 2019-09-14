package com.plotgen.rramirez.plotgenerator.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Outline_detail;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Timeline_detail;
import com.plotgen.rramirez.plotgenerator.items.item_outline;
import com.plotgen.rramirez.plotgenerator.items.item_timeline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_timeline extends RecyclerView.Adapter<Adapter_timeline.myViewHolder> {

    Context mContext;
    List<item_timeline> mData;

    public Adapter_timeline(Context mContext, List<item_timeline> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.timeline_item, parent, false);
        return new myViewHolder(v,mData);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        String vv = Utils.unixToString(mData.get(position).getTimeline_date());
        String title =  vv + " " + mData.get(position).getTimeline_title();
        holder.timeline_title.setText(title);
        holder.timeline_description.setText(mData.get(position).getTimeline_description());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView timeline_title;
        TextView timeline_description;
        List<item_timeline> mData;

        public myViewHolder(View itemView, List<item_timeline> mData){
            super (itemView);
            timeline_title = itemView.findViewById(R.id.timeline_title);
            timeline_description = itemView.findViewById(R.id.timeline_description);
            this.mData = mData;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Common.currentTimelineID = mData.get(getAdapterPosition()).getTimeline_id();
            Common.timelineCreationMode = false;
            nextFragment(mContext);
        }
    }

    public void nextFragment(Context context){
        Timeline_detail nextFragment = new Timeline_detail();
        //Make the transaction
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.addToBackStack(null);
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();
    }
}
