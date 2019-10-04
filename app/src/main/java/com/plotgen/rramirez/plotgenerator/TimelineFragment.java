package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_timeline;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.items.item_timeline;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {


    ArrayList timeline_list;
    List<item_timeline> mlist = new ArrayList<>();
    private String fragmentTag = TimelineFragment.class.getSimpleName();

    @BindView(R.id.timeline_empty_tv)
    TextView timeline_empty_tv;
    @BindView(R.id.fab_add_timeline)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fab_add_timeline;

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, myFragmentView);

        timeline_list = new ArrayList();
        timeline_list = SQLUtils.getTimelineByCharacterID(getContext(), Common.currentCharacter.getId());
        if (timeline_list.size() > 0){
            timeline_empty_tv.setVisibility(View.INVISIBLE);
        }
        populateTimeline(myFragmentView);

        fab_add_timeline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentTransaction();
            }
        });

        return myFragmentView;
    }


    public void onStart() {
        super.onStart();
    }

    public void populateTimeline(View view)
    {
        //set up the recycler view with the adapter
        RecyclerView recyclerView = view.findViewById(R.id.timeline_list);
        final Adapter_timeline adapter = new Adapter_timeline(this.getActivity(), mlist);
        mlist.clear();
        for (int i = 0; i < timeline_list.size(); i++) {
            String id =  timeline_list.get(i).toString().split("/&&/")[0];
            String title = timeline_list.get(i).toString().split("/&&/")[1];
            String description = timeline_list.get(i).toString().split("/&&/")[2];
            String date = timeline_list.get(i).toString().split("/&&/")[4];
            mlist.add(new item_timeline(id,title, description,date));
            adapter.notifyDataSetChanged();
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    private void fragmentTransaction(){
        Timeline_detail nextFragment = new Timeline_detail();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        Common.timelineCreationMode = true;
        //getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }

}
