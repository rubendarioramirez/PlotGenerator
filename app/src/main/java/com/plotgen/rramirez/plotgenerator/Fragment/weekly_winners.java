package com.plotgen.rramirez.plotgenerator.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.plotgen.rramirez.plotgenerator.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.annotation.Nullable;


public class weekly_winners extends Fragment {

    public weekly_winners() {
        // Required empty public constructor
    }


    TextView title_tv, body_tv, author_tv;
    String dataBaseToUse;


//    private DocumentReference mDocRef_es = FirebaseFirestore.getInstance().document(dataBaseToUse);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_winners, container, false);

        title_tv = view.findViewById(R.id.wwtitle_tv);
        body_tv = view.findViewById(R.id.wwbody_tv);
        author_tv = view.findViewById(R.id.wwauthor_tv);
        DocumentReference mDocRef;

        if (Locale.getDefault().getDisplayLanguage().equals("español")) {
            mDocRef = FirebaseFirestore.getInstance().document("weekly_winner_es/current");
        } else {
            mDocRef = FirebaseFirestore.getInstance().document("weekly_winners/current");
        }

            mDocRef.addSnapshotListener(this.getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot.exists()){

                        String title = documentSnapshot.getString("title");
                        String body = documentSnapshot.getString("body");
                        String author = documentSnapshot.getString("author");

                        title_tv.setText(title);
                        //Parse body to get the line breaks
                        String bodyparsed = body.replace("\\n", "\n");
                        body_tv.setText(bodyparsed);
                        author_tv.setText(getString(R.string.weekly_challenge_author) + " - " + author);
                    }
                }
            });

        return view;
    }

}
