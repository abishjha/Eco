package edu.ramapo.ajha.eco;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class Database {

    public final static String DB_USERS = "users";
    public final static String DB_ECO = "ecological_awareness";
    public final static String DB_EVENTS = "events";
    public final static String DB_STORIES = "stories";
    public final static String DB_DISCUSSION = "discussion";
    public final static String DB_PETITIONS = "petitions";

    private static DatabaseReference mDatabase;

    // initializes and/or resets the mDatabase reference to the top of the JSON tree
    public static void init(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // use this to clear the database of all objects
        //mDatabase.setValue(null);
    }

    // checks if a user is present in the app database, if not, adds the user
    public static void registerUser(final GoogleSignInAccount account){
        init();

        // code to register user
        final DatabaseReference users = mDatabase.child(DB_USERS);

        if(account.getId() != null) {
            users.child(account.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                System.out.println("user exists " + account.getDisplayName());
                            } else {
                                System.out.println("adding user " + account.getDisplayName());

                                users.child(account.getId()).child("name").setValue(account.getDisplayName());
                                users.child(account.getId()).child("email").setValue(account.getEmail());
                                users.child(account.getId()).child("idToken").setValue(account.getIdToken());
                                //users.child(account.getId()).child("photo").setValue(account.getPhotoUrl());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("[REGISTER_USER] DB operation cancelled");
                        }
                    });
        }
    }

    public static String fragmentToDatabaseURLMap(String fragmentName){
        String url = "";

        switch(fragmentName){
            case "Ecological Awareness":
                url = DB_ECO;
                break;
            case "Events":
                url = DB_EVENTS;
                break;
            case "Stories":
                url = DB_STORIES;
                break;
            case "Discussion":
                url = DB_DISCUSSION;
                break;
            case "Petitions":
                url = DB_PETITIONS;
                break;
        }

        return url;
    }

    public static void getMetaData(String fragmentName, final RecyclerView recyclerView){
        init();

        DatabaseReference newRef = mDatabase.child(fragmentToDatabaseURLMap(fragmentName)).child("meta-data");

        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("[GET DATA] data extracted from DB");

                recyclerView.setAdapter(new MyAdapter((HashMap) dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("[GET DATA] DB operation cancelled");
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
                            System.out.println("[DEBUG] data found in DB");
                            System.out.println(dataSnapshot.getValue());
                        }
                        else{
                            System.out.println("[DEBUG] data not found in DB");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("[DEBUG] DB operation cancelled");
                    }
                });
    }
}
