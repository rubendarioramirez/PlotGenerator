package com.plotgen.rramirez.plotgenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DbPorter {

    private DatabaseReference dbRef;

    private Context context;

    public DbPorter(Context context){
        this.context = context;
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    String lastPostId = "";

    public void loadNode(final String node,  String lastPost){


        SharedPreferences pref = context.getSharedPreferences("LAST_ID_POST",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        String lPId = pref.getString("key","");
        if (!lPId.isEmpty()){
            lastPost = lPId;
        }


        DatabaseReference ref = dbRef.child(node);
        /*if (!lastPost.isEmpty()){

           ref.orderByKey().startAt(lastPost).limitToFirst(30).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        HashMap<String,Object> list = (HashMap<String, Object>) dataSnapshot.getValue();
                        ArrayList<String> keyslist = new ArrayList<>();
                        for(String key : list.keySet()){
                            keyslist.add(key);
                            lastPostId = key;
                            editor.putString("key",lastPostId);
                            editor.apply();
                        }
                        if (keyslist.size() > 0)
                            new MyAsync(node,list,0,keyslist).execute();
                        else
                            Log.e("Zero values ------>","Zero Values---->>>>>> ");

                    }catch (Exception e){
                        Log.e("Exception ------>","EXP---->>>>>> "+e.getMessage());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
           return;
        }*/
        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("bb",dataSnapshot+"");
                try {
                    HashMap<String,Object> list = (HashMap<String, Object>) dataSnapshot.getValue();
                    ArrayList<String> keyslist = new ArrayList<>();

                    for(String key : list.keySet()){
                        keyslist.add(key);
                        lastPostId = key;
                        editor.putString("key",lastPostId);
                        editor.apply();
                    }

                    if (keyslist.size() > 0)
                        new MyAsync(node,list,0,keyslist).execute();
                    else
                        Log.e("Zero values ------>","Zero Values---->>>>>> ");

                }catch (Exception e){
                    Log.e("Exception ------>","EXP---->>>>>> "+e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-> ",databaseError+"");
            }
        });
    }

    class MyAsync extends AsyncTask{

        HashMap<String ,Object> list ;
        int index = 0;

        ArrayList<String> keysList ;

        String node;

        public MyAsync(String node, HashMap<String, Object> map, int index, ArrayList<String> keyslist){
            this.list = map;
            this.index = index;
            this.node = node;
            this.keysList = keyslist;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference cRf = db.collection("users_1");

            if (index<keysList.size())
                Log.e("Now get from id","Key ID: "+keysList.get(index));
            try{
                Map<String ,Object> map = (Map<String, Object>) list.get(keysList.get(index));
                cRf.document(keysList.get(index))
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void documentReference) {
                                index += 1;
                                if (index < keysList.size()) {
                                    new MyAsync(node, list, index, keysList).execute();
                                }else{
                                    Log.e("Finish","Finish--------->  ");
                                    loadNode("users",lastPostId);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("dsdsdsdsdsds","dsds  "+e.getMessage());
                    }
                });
            }catch (Exception e){
                Log.e("Expertion Case","Ex- Case arised --------->  ");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        index += 1;
                        if (index < keysList.size()) {
                            new MyAsync(node, list, index, keysList).execute();
                        }else{
                            Log.e("Finish","Finish in exp case--------->  ");
                            loadNode("users",lastPostId);
                        }
                    }
                });
            }
            return null;
        }
    }
}
