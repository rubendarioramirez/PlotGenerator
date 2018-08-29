package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharListFragment extends Fragment {

    TextView project_list_tv;

    public CharListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_list_tab));
        //Get the data from the previous fragment
        String project_name_text = this.getArguments().getString("project_name");
        final View myFragmentView =   inflater.inflate(R.layout.fragment_char_list, container, false);


        //Declare elements
        project_list_tv = myFragmentView.findViewById(R.id.project_list_tv);




        project_list_tv.setText(project_name_text);

        return myFragmentView;
    }

}
