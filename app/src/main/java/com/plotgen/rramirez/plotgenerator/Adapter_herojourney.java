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
                bundle.putString("challenge_number",clicked.toString());
                //Send it to the next fragment
                ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            } else if (clicked.equals("Challenge II") || clicked.equals("Desafio II")){
                //Send it to the next fragment
                Bundle bundle = new Bundle();
                bundle.putString("char_name",charName);
                bundle.putString("project_name",projectName);
                bundle.putString("challenge_number",clicked.toString());
                //Send it to the next fragment
                ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            } else if (clicked.equals("Challenge III") || clicked.equals("Desafio III")){
                //Send it to the next fragment
                Bundle bundle = new Bundle();
                bundle.putString("char_name",charName);
                bundle.putString("project_name",projectName);
                bundle.putString("challenge_number",clicked.toString());
                //Send it to the next fragment
                ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            } else if (clicked.equals("Challenge IV") || clicked.equals("Desafio IV")){
                    //Send it to the next fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("char_name",charName);
                    bundle.putString("project_name",projectName);
                    bundle.putString("challenge_number",clicked.toString());
                    //Send it to the next fragment
                    ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                    nextFragment.setArguments(bundle);
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }  else if (clicked.equals("Challenge V") || clicked.equals("Desafio V")) {
                //Send it to the next fragment
                Bundle bundle = new Bundle();
                bundle.putString("char_name", charName);
                bundle.putString("project_name", projectName);
                bundle.putString("challenge_number", clicked.toString());
                //Send it to the next fragment
                ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.commit();
            }  else if (clicked.equals("Mentor Challenge") || clicked.equals("Desafio del Mentor")) {
                //Send it to the next fragment
                Bundle bundle = new Bundle();
                bundle.putString("char_name", charName);
                bundle.putString("project_name", projectName);
                bundle.putString("challenge_number", clicked.toString());
                //Send it to the next fragment
                ChallengeTemplateFragment nextFragment = new ChallengeTemplateFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.commit();
            }
            else if (clicked.equals("Typical Character Roles") || clicked.equals("Roles de personajes tipicos")){
                    //Send it to the next fragment
                    GuideRoleFragment nextFragment = new GuideRoleFragment();
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }else if (clicked.equals("3D Characters - Lajos Egri") || clicked.equals("Personajes 3D - Lajos Egri")){
                    //Send it to the next fragment
                    GuideLajosFragment nextFragment = new GuideLajosFragment();
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }else if (clicked.equals("Change Arc - K.M Weiland") || clicked.equals("Arco de cambio - K.M Weiland")){
                    //Send it to the next fragment
                    GuideWeilandFragment nextFragment = new GuideWeilandFragment();
                    //Make the transaction
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.flMain,nextFragment);
                    transaction.commit();
            }else if (clicked.equals("The anatomy of the perfect Antagonist") || clicked.equals("Anatomia del Antagonista perfecto")){
                //Send it to the next fragment
                AntagonistFragment nextFragment = new AntagonistFragment();
                //Make the transaction
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            }



        }
    }
}
