package com.plotgen.rramirez.plotgenerator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
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

import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Model.Character;

import java.util.List;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.THUMBNAIL_SIZE;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_characterList extends RecyclerView.Adapter<Adapter_characterList.myViewHolder> {

    Context mContext;
    List<item_character_list> mData;
    String project_name;
    String charID;


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

        if(mData.get(position).getImage().equals("android.resource://com.plotgen.rramirez.plotgenerator/drawable/ic_menu_camera"))
        {
            holder.image.setImageURI(Uri.parse(mData.get(position).getImage()));
        } else {
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mData.get(position).getImage()), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            holder.image.setImageBitmap(ThumbImage);
        }
        holder.name.setText(mData.get(position).getName());
        holder.role.setText(mData.get(position).getRole());
//        holder.completion.setText(mData.get(position).getCompletion() + "%");
        holder.completion.setText("");
        charID = mData.get(position).getId();
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
//            String parsed_char_name = clicked.substring(0, clicked.length() - 1);

            String role = mData.get(getAdapterPosition()).getRole();
            String gender = mData.get(getAdapterPosition()).getGender();
            Integer completion = Integer.valueOf(mData.get(getAdapterPosition()).getCompletion());
            Character character = new Character(charID,clicked,project_name,role,gender,completion);
            Common.currentCharacter = character;
            nextFragment(mContext);
        }

    }

//    public void nextFragment(Context context, String charName,String projectName){
    public void nextFragment(Context context){
                BioFragment nextFragment = new BioFragment();
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) context;
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
    }
}
