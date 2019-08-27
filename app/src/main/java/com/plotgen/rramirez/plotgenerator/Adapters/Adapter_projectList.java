package com.plotgen.rramirez.plotgenerator.Adapters;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plotgen.rramirez.plotgenerator.CharListFragment;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Model.Project;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.items.item_project_list;

import java.util.List;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.THUMBNAIL_SIZE;

/**
 * Created by macintosh on 22/08/18.
 */

public class Adapter_projectList extends RecyclerView.Adapter<Adapter_projectList.myViewHolder> {

    Context mContext;
    List<item_project_list> mData;
    String projectID;


    public Adapter_projectList(Context mContext, List<item_project_list> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.project_list_item_20, parent, false);
        return new myViewHolder(v, mContext,mData);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        holder.genre.setText(mData.get(position).getgenre());
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mData.get(position).getImage()), 640, 400);
        if(mData.get(position).getImage().equals("")){
            String defaultImagePath = "android.resource://com.plotgen.rramirez.plotgenerator/drawable/ic_menu_camera";
            holder.image.setImageURI(Uri.parse(defaultImagePath));
        } else {
            holder.image.setImageBitmap(ThumbImage);
        }
        //Animated text
        ValueAnimator projectsAnimator = new ValueAnimator();
        projectsAnimator.setObjectValues(0, Integer.parseInt(mData.get(position).getcharacters()));// here you set the range, from 0 to "count" value
        projectsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                holder.characters.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        projectsAnimator.setDuration(500); // here you set the duration of the anim
        projectsAnimator.start();
        projectID = mData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,genre,characters;
        ImageView image;
        Context mContext;
        List<item_project_list> mData;
        String projectID;

        public myViewHolder(View itemView, Context mContext, List<item_project_list> mData){
            super (itemView);
            name = itemView.findViewById(R.id.project_name_lv);
            genre = itemView.findViewById(R.id.project_genre_lv);
            characters = itemView.findViewById(R.id.project_completion_lv);
            image = itemView.findViewById(R.id.project_list_iv);

            this.mContext = mContext;
            this.mData = mData;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            String clicked = mData.get(getAdapterPosition()).getName();
            //String genre = mData.get(getAdapterPosition()).getgenre();
            //Integer characters = Integer.valueOf(mData.get(getAdapterPosition()).getcharacters());
            projectID = mData.get(getAdapterPosition()).getId();
            Project project = new Project(projectID,clicked);
            Common.currentProject = project;
            nextFragment(mContext);
        }

    }

    public void nextFragment(Context context){
                CharListFragment nextFragment = new CharListFragment();
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) context;
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
    }
}
