package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlotGenFragment extends Fragment {


    public PlotGenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).setActionBarTitle("Plot Generator");

        return inflater.inflate(R.layout.fragment_plotgen, container, false);
    }

}
