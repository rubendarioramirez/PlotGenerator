package com.plotgen.rramirez.plotgenerator;


import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plotgen.rramirez.plotgenerator.Guides.AntagonistFragment;
import com.plotgen.rramirez.plotgenerator.Guides.GuideLajosFragment;
import com.plotgen.rramirez.plotgenerator.Guides.GuideRoleFragment;
import com.plotgen.rramirez.plotgenerator.Guides.GuideWeilandFragment;

import java.util.List;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_characterList extends RecyclerView.Adapter<Adapter_characterList.myViewHolder> {

    Context mContext;
    List<item_character_list> mData;
    String project_name;

    public Adapter_characterList(Context mContext, List<item_character_list> mData, String project_name) {
        this.mContext = mContext;
        this.mData = mData;
        this.project_name = project_name;
    }



    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.character_list_item, parent, false);
        return new myViewHolder(v, mContext,mData);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.image.setImageResource(mData.get(position).getImage());
        holder.name.setText(mData.get(position).getName());
        holder.role.setText(mData.get(position).getRole());
        holder.completion.setText(mData.get(position).getCompletion());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView name,role,completion;
        Context mContext;
        List<item_character_list> mData;

        public myViewHolder(View itemView, Context mContext, List<item_character_list> mData){
            super (itemView);
            image = itemView.findViewById(R.id.character_image_list_view);
            name = itemView.findViewById(R.id.character_name_list_view);
            role = itemView.findViewById(R.id.character_role_list_view);
            completion = itemView.findViewById(R.id.character_completion_list_view);

            this.mContext = mContext;
            this.mData = mData;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            String clicked = mData.get(getAdapterPosition()).getName();
            nextFragment(mContext,clicked,project_name);
        }

    }

    public void nextFragment(Context context, String charName,String projectName){
                Bundle bundle = new Bundle();
                String parsed_char_name = charName.substring(0, charName.length() - 1);
                bundle.putString("char_name", parsed_char_name);
                bundle.putString("project_name", projectName);
                //Send it to the next fragment
                BioFragment nextFragment = new BioFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) context;
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
    }
}
