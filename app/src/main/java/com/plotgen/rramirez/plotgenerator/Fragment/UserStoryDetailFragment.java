package com.plotgen.rramirez.plotgenerator.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.StorageReference;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Notify;
import com.plotgen.rramirez.plotgenerator.Model.Comment;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.CommentViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserStoryDetailFragment extends Fragment {

    @BindView(R.id.ivTemplatePic)
    ImageView ivTemplatePic;

    @BindView(R.id.ivUserPicDetail)
    ImageView ivUserPicDetail;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvGenre)
    TextView tvGenre;

    @BindView(R.id.tvStory)
    TextView tvStory;

    @BindView(R.id.tvLoves)
    TextView tvLoves;

    @BindView(R.id.ivLoves)
    ImageView ivLoves;

    private FirebaseFirestore mDatabase;
    private DocumentReference mReference;

    public UserStoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_story_detail, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mDatabase = FirebaseFirestore.getInstance();
        mDatabase = Common.currentDatabase;

        ButterKnife.bind(this, view);

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        mReference = mDatabase.collection("stories").document(Common.currentStory.getUser().getEmail());

        ivTemplatePic.setImageResource(R.drawable.typewriter);

        tvTitle.setText(Common.currentStory.getTitle());
        tvGenre.setText(Common.currentStory.getGenre());
        //tvStory.setText(Common.currentUserStory.getStory());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvStory.setText(Html.fromHtml(Common.currentStory.getChalenge(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvStory.setText(Html.fromHtml(Common.currentStory.getChalenge()));
        }

        if (Common.currentUser != null) {
            if (Common.currentStory.likes.containsKey(Common.currentUser.getEmail())) {
                ivLoves.setImageResource(R.drawable.ic_love_red);
            } else {
                ivLoves.setImageResource(R.drawable.ic_love_outline);
            }
        } else {
            ivLoves.setImageResource(R.drawable.ic_love_outline);
        }

        ivLoves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLikeClicked(Common.currentDocumentReference);
            }
        });

        tvLoves.setText(String.valueOf(Common.currentStory.getLikeCount()));

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getActivity().getApplicationContext())
                .load(getHiResUrl(Common.currentStory.getUser().getUriString()))
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserPicDetail);
    }

    private String getHiResUrl(String s) {

        // Variable holding the original String portion of the url that will be replaced
        String originalPieceOfUrl = "s96-c/photo.jpg";

        // Variable holding the new String portion of the url that does the replacing, to improve image quality
        String newPieceOfUrlToAdd = "s400-c/photo.jpg";

        // Check if the Url path is null
        if (s != null) {

            // Convert the Url to a String and store into a variable
            String photoPath = s.toString();

            // Replace the original part of the Url with the new part

            return photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
        } else
            return s;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }

    private void onLikeClicked(final DocumentReference documentReference1) {
        if (Common.currentUser != null) {
            mDatabase.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                    Story p = transaction.get(documentReference1).toObject(Story.class);

                    if (p.likes.containsKey(Common.currentUser.getEmail())) {
                        // Unstar the post and remove self from stars
                        p.likeCount = p.likeCount - 1;
                        p.likes.remove(Common.currentUser.getEmail());
                    } else {
                        // Star the post and add self to stars
                        p.likeCount = p.likeCount + 1;
                        p.likes.put(Common.currentUser.getEmail(), true);
                    }

                    // Set value and report transaction success

                    transaction.set(documentReference1, p);
                    // documentReference.set(p);
                    Common.tempStory = p;
                    return null;

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Common.tempStory.likes.containsKey(Common.currentUser.getEmail())) {
                                ivLoves.setImageResource(R.drawable.ic_love_red);
                            } else {
                                ivLoves.setImageResource(R.drawable.ic_love_outline);
                            }
                            tvLoves.setText(String.valueOf(Common.tempStory.getLikeCount()));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v("matilda", Common.tempStory.getId());
                    Log.v("matilda", Common.tempStory.getUser().toString());
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.e("Failed", "like failed" + e);
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.login_first), Toast.LENGTH_LONG).show();
        }
    }
}
