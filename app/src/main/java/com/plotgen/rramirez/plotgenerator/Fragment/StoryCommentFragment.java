package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plotgen.rramirez.plotgenerator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryCommentFragment extends Fragment {


    public StoryCommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_comment, container, false);
    }

}
