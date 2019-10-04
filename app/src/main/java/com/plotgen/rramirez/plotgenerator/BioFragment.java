package com.plotgen.rramirez.plotgenerator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Tutorial;
import com.plotgen.rramirez.plotgenerator.Common.Utils;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.THUMBNAIL_SIZE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioFragment extends Fragment {

    ArrayList<String> char_description;
    private FirebaseAnalytics mFirebaseAnalytics;
    String char_name, charID, project_name, charRole;

    @BindView(R.id.character_bio_name_title)
    TextView title;
    @BindView(R.id.character_bio_role_title)
    TextView role_subtitle;
    @BindView(R.id.character_bio_intro)
    TextView intro_tv;
    @BindView(R.id.bio_charimage)
    ImageView bio_charimage;
    @BindView(R.id.fab_bio_edit)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabEditCharacter;


    private String filepath = "";


    public BioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myFragmentView = inflater.inflate(R.layout.fragment_bio, container, false);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        ButterKnife.bind(this, myFragmentView);

        //char_description = new ArrayList<String>();

        if(Common.currentCharacter !=null) {
            try {
                char_name = Common.currentCharacter.getName();
                charID = Common.currentCharacter.getId();
                charRole = Common.currentCharacter.getRole();
            } catch (Exception e) {
                Log.v("matilda", e.toString());
            }
        }


        ((MainActivity) getActivity()).setActionBarTitle(char_name);

        //Titles
        title.setText(char_name);
        role_subtitle.setText(charRole);

        String bio = Utils.generateBIO(getContext(),charID);
        intro_tv.setText(Html.fromHtml(bio));
        displayPhoto();

        fabEditCharacter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Common.charCreationMode = false;
                //Send it to the next fragment
                fragmentTransaction();
            }
        });

        Tutorial.checkTutorial(myFragmentView,getActivity());

        return myFragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("");
    }

    public void displayPhoto() {
        String imageToShow = "";
        try{
            imageToShow = Utils.getPhoto(getContext(),charID).get(0);
        } catch (Exception e){
            Log.d("matilda", "displayPhoto doesnt work");
        }

        if (imageToShow!=null && !imageToShow.isEmpty()){
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageToShow), THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            bio_charimage.setImageBitmap(ThumbImage);
            filepath = imageToShow;
        } else {
            String defaultImagePath = "android.resource://com.plotgen.rramirez.plotgenerator/drawable/ic_menu_camera";
            bio_charimage.setImageURI(Uri.parse(defaultImagePath));
            filepath = imageToShow;
        }
    }

    private void fragmentTransaction(){
        CharacterFragment nextFragment = new CharacterFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }

}
