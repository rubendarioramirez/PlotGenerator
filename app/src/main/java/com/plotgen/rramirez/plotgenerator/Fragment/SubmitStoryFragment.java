package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitStoryFragment extends Fragment {

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etGenre)
    EditText etGenre;
    @BindView(R.id.etStory)
    EditText etStory;

    private DatabaseReference mDatabase;

    @OnClick(R.id.btnSubmit)
    public void submitStory(View view)
    {

        Story story = new Story(etTitle.getText().toString(),
                etGenre.getText().toString(),
                etStory.getText().toString(),
                new User(Common.currentUser.getUid(),
                        Common.currentUser.getName(),
                        Common.currentUser.getEmail(),
                        Common.currentUser.getPicUrl().toString()));

        FirebaseDatabase.getInstance().getReference().child("Weekly_Challenge_test").push().setValue(story);

        etTitle.setText("");
        etGenre.setText("");
        etStory.setText("");

        WeeklyChallengeFragment nextFragment = new WeeklyChallengeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment,transaction,"","");
    }


    public SubmitStoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("Submit Your Challenge!");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit_story, container, false);

        ButterKnife.bind(this, view);

        tvEmail.setText(Common.currentUser.getName());

        return view;
    }

}
