package edu.ramapo.ajha.eco;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class Database{
    private static final String TAG = "Database";

    private static DatabaseReference mDatabase;

    // initializes and/or resets the mDatabase reference to the top of the JSON tree
    private static void init(){
        if(mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Log.w(TAG, "connected to database");
        }
    }

    // checks if a user is present in the app database, if not, adds the user
    public static void registerUser(final GoogleSignInAccount account){
        init();

        // code to register user
        final DatabaseReference users = mDatabase.child(AppContext.getContext().getString(R.string.db_users));

        if(account.getId() != null) {
            users.child(account.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.w(TAG, "user exists " + account.getDisplayName());
                            } else {
                                Log.w(TAG, "adding user " + account.getDisplayName());

                                users.child(account.getId()).child("name").setValue(account.getDisplayName());
                                users.child(account.getId()).child("email").setValue(account.getEmail());
                                users.child(account.getId()).child("idToken").setValue(account.getIdToken());
                                //users.child(account.getId()).child("photo").setValue(account.getPhotoUrl());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "[REGISTER_USER] DB operation cancelled");
                        }
                    });
        }
    }

    public static void getMetaData(final String section, final RecyclerView recyclerView){
        init();

        DatabaseReference newRef = mDatabase.child(section).child("meta-data");

        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG, "[GET DATA] data extracted from DB");

                recyclerView.setAdapter(new MyAdapter((HashMap) dataSnapshot.getValue(), section));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "[GET DATA] DB operation cancelled");
            }
        });
    }

    public static void getDetailedData(String section, String docID, final DetailActivity viewActivity){
        init();

        DatabaseReference newRef = mDatabase.child(section).child("content").child(docID);

        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG, "[GET DATA] data extracted from DB");

                viewActivity.setData((HashMap) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "[GET DATA] DB operation cancelled");
            }
        });
    }

    // print the entire database for debug purposes
    public static void printDB(){
        init();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.w(TAG, "[DEBUG] data found in DB.  printing...");
                    System.out.println(dataSnapshot.getValue());
                }
                else{
                    Log.w(TAG, "[DEBUG] data not found in DB");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "[DEBUG] DB operation cancelled");
            }
        });
    }
}
