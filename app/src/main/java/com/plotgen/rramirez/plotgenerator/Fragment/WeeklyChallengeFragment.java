package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Utils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyChallengeFragment extends Fragment {

    @BindView(R.id.lvWeeklyChalenge)
    ListView lvWeeklyChallenge;

    FirebaseListAdapter<Story> adapter;
    FirebaseListOptions<Story> options;


    public WeeklyChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("Weekly Challenge Participant");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_challenge, container, false);

        ButterKnife.bind(this, view);

        options = new FirebaseListOptions.Builder<Story>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Weekly_Challenge_test"), Story.class)
                .setLayout(R.layout.item_story)
                .build();

        populateWeeklyChallenge();

        return view;
    }

    private void populateWeeklyChallenge() {

        adapter = new FirebaseListAdapter<Story>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Story model, int position) {

                TextView tvTitle, tvGenre, tvStory, tvUser;
                ImageView ivTemplatePic, ivUser;

                tvTitle = v.findViewById(R.id.tvTitle);
                tvGenre = v.findViewById(R.id.tvGenre);
                tvStory = v.findViewById(R.id.tvStory);
                tvUser = v.findViewById(R.id.tvUser);
                ivUser = v.findViewById(R.id.ivUser);

                Glide.with(v.getContext())
                        .load(model.getUser().getUriString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivUser);

                ivTemplatePic = v.findViewById(R.id.ivTemplatePic);

                ivTemplatePic.setBackgroundColor(new Random().nextInt());

                tvUser.setText(model.getUser().getName().toString());
                tvTitle.setText(model.getTitle().toString());
                tvGenre.setText(model.getGenre().toString());
                tvStory.setText(model.getChalenge().toString());

                final Story test = model;

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.currentStory = test;
                        StoryDetailFragment nextFragment = new StoryDetailFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Utils.changeFragment(nextFragment,transaction,"","");
                    }
                });
            }
        };

        lvWeeklyChallenge.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
