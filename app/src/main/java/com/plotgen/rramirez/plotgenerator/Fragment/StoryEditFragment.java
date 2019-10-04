package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryEditFragment extends Fragment {

    @BindView(R.id.etEditStory)
    MaterialEditText etEditStory;

    private FirebaseFirestore mDatabase;
    private CollectionReference mReference;
    private DocumentReference mNewStoryReference;

    @OnClick(R.id.btnSaveEdit)
    public void saveEdit(View v)
    {
        final String newStory = etEditStory.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(newStory)) {
            etEditStory.setError("Required");
            return;
        }
         mDatabase = FirebaseFirestore.getInstance();
        String key = Common.currentStory.getId();
         mReference = mDatabase.collection(getString(R.string.weekly_challenge_db_name)).document("posts").collection("posts");

        mNewStoryReference = mReference.document(key);

        mNewStoryReference.update("chalenge",newStory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        /*mNewStoryReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Story s = mutableData.getValue(Story.class);
                if (s == null) {
                    return Transaction.success(mutableData);
                }
                s.setChalenge(newStory);
                // Set value and report transaction success
                mutableData.setValue(s);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Log.d("UpdateStory", "postTransaction:onComplete:" + databaseError);
            }
        });
*/
        weekly_challenge_container nextFragment = new weekly_challenge_container();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment,transaction);
    }


    public StoryEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_edit, container, false);

        ButterKnife.bind(this, view);

        mDatabase = FirebaseFirestore.getInstance();
        mReference = mDatabase.collection(getString(R.string.weekly_challenge_db_name)).document("posts").collection("posts");

        etEditStory.setText(Common.currentStory.getChalenge());


        return view;
    }

}
