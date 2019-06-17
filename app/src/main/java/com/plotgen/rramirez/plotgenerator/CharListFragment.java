package com.plotgen.rramirez.plotgenerator;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Tutorial;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Fragment.OfflineStoryFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.TOTAL_CHALLENGES;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharListFragment extends Fragment {

    TextView empty_character_tv;
//    ImageButton charlist_project_edit_btn;
    RecyclerView character_list_lv;
    ArrayList<String> char_list_array;
    ArrayAdapter<String> itemsAdapter;
    String project_info;
    String project_name_text;
    String project_id;
    String completion;
    private FirebaseAnalytics mFirebaseAnalytics;
    List<item_character_list> mlist = new ArrayList<>();


    @BindView(R.id.fab_add_char)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddChar;
    @BindView(R.id.fab_add_story)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddStory;
//    private AdView mAdView;
    private String fragmentTag = CharListFragment.class.getSimpleName();

    public CharListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Get the data from the previous fragment
        try {
            project_name_text = Common.currentProject.getName();
            project_id = Common.currentProject.getId();
        } catch (Exception e) {
            Log.v("matilda", e.toString());
        }
        final View myFragmentView = inflater.inflate(R.layout.fragment_char_list, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(project_name_text);
        setHasOptionsMenu(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        ButterKnife.bind(this, myFragmentView);

        //Declare elements
        RecyclerView recyclerView = myFragmentView.findViewById(R.id.character_list_lv);
        empty_character_tv = myFragmentView.findViewById(R.id.empty_character_tv);
        char_list_array = Utils.getCharListByID(myFragmentView.getContext(), project_id);


        final Adapter_characterList adapter = new Adapter_characterList(this.getActivity(),mlist);
        mlist.clear();

        if(!char_list_array.isEmpty()){
            empty_character_tv.setVisibility(View.INVISIBLE);
            for (int i = 0; i<char_list_array.size();i++) {
                String id = char_list_array.get(i).split("/&&/")[0];
                String name = char_list_array.get(i).split("/&&/")[1];
                String role = char_list_array.get(i).split("/&&/")[2];
                String gender = char_list_array.get(i).split("/&&/")[3];
                Integer challengesDone = Integer.valueOf(char_list_array.get(i).split("/&&/")[4]);
                Integer completionCheck = challengesDone*100/TOTAL_CHALLENGES;
                if (completionCheck>=100){
                    completion = "100";
                } else {
                 completion = String.valueOf(challengesDone*100/TOTAL_CHALLENGES);
                }
                String defaultImagePath = "android.resource://com.plotgen.rramirez.plotgenerator/drawable/ic_menu_camera";
                String image = "";
                for (int x =0; x<char_list_array.get(i).split("/&&/").length;x++)
                {
                    try {
                        image += "-" + char_list_array.get(i).split("/&&/")[x + 5];
                    } catch (Exception e){
                        Log.v("exceptions", e.toString());
                    }
                }
                if (!image.isEmpty()){
                    mlist.add(new item_character_list(id,image.substring(1), name, role, gender,completion));
                }
                else {
                    mlist.add(new item_character_list(id,defaultImagePath, name, role, gender,completion));
                }
                }
             }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


//        if (!Common.isPAU) {
//            //Display the ad
//            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_char_list);
//            AdsHelper.loadAd(mAdView);
//        }


        fabAddChar.setFabText(getString(R.string.add_char));
        fabAddChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.charCreationMode = true;
                CharacterFragment nextFragment = new CharacterFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.replace(R.id.flMain, nextFragment);
                getActivity().getSupportFragmentManager().popBackStack();
                transaction.commit();
                //                Utils.changeFragment(nextFragment, transaction);
            }
        });

        fabAddStory.setFabText(getString(R.string.write_story));
        if (Utils.isHaveStoryFromDB(myFragmentView.getContext(), project_id))
            fabAddStory.setFabText(getString(R.string.edit_story));

        fabAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineStoryFragment nextFragment = new OfflineStoryFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction);
                Bundle params = new Bundle();
                params.putString("created_story", "started");
                mFirebaseAnalytics.logEvent("created_story", params);
            }
        });

        Tutorial.checkTutorial(myFragmentView,getActivity());

        return myFragmentView;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_charlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_charlist_edit) {
            if(Common.currentProject != null) {
                //Send it to the next fragment
                Project_detailsFragment nextFragment = new Project_detailsFragment();
                //Make the transaction
                Bundle bundle = new Bundle();
                bundle.putString("project_name", project_name_text);
                //Send it to the next fragment
                nextFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.commit();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
