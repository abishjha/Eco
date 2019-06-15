package edu.ramapo.ajha.eco;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

/**/
/*
Database class

DESCRIPTION

        This non-activity activity class is the endpoint for all the database communication.
        This has been put in a separate file so the database can be changed if needed without
        any/much change across other parts of the code.  This class communicates with the
        Firebase database to performs operations such as inserting, editing, deleting, and
        retrieving data.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
class Database{
    private static final String TAG = "Database";

    private static DatabaseReference mDatabase;
    private static GoogleSignInAccount mUserAccount;

    static final String DB_KEY_AUTHORID = "authorID";
    static final String DB_KEY_CONTENT = "content";
    static final String DB_KEY_DOCID = "docID";
    static final String DB_KEY_TIME = "time";
    static final String DB_KEY_TITLE = "title";

    static final String DB_KEY_NAME = "name";
    static final String DB_KEY_EMAIL = "email";
    static final String DB_KEY_REAL_NAME = "real-name";

    private static final String DB_DOCUMENT_USERS = "users";
    private static final String DB_DOCUMENT_META_DATA = "meta-data";
    private static final String DB_DOCUMENT_CONTENT = "content";


    /**/
    /*
    init() init()

    NAME

            init - initializes the database reference if not initialized

    SYNOPSIS

            static void init();

    DESCRIPTION

            Checks if the database reference is initialized, if not initializes it to the root
            of the database (which is a JSON tree) from where other references can traverse.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private static void init(){
        if(mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Log.w(TAG, "connected to database");
        }
    }


    /**/
    /*
    registerUser() registerUser()

    NAME

            registerUser - register the passed in user to the database

    SYNOPSIS

            static void registerUser(final GoogleSignInAccount account);

            account -> this is the account object provided by Google upon login

    DESCRIPTION

            Checks if the passed in user exists in the database, if not saves his/her info.  Also
            save the instance as a class member for later usage as a static variable

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void registerUser(final GoogleSignInAccount account){
        init();

        mUserAccount = account;

        // code to register user
        final DatabaseReference users = mDatabase.child(DB_DOCUMENT_USERS);

        if(account.getId() != null) {
            users.child(account.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.w(TAG, "user exists " + account.getDisplayName());
                            } else {
                                Log.w(TAG, "adding user " + account.getDisplayName());

                                users.child(account.getId()).child(DB_KEY_NAME).setValue(account.getDisplayName());
                                users.child(account.getId()).child(DB_KEY_REAL_NAME).setValue(account.getDisplayName());
                                users.child(account.getId()).child(DB_KEY_EMAIL).setValue(account.getEmail());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "register user operation cancelled");
                        }
                    });
        }
    }


    /**/
    /*
    getCurrentUserRealName() getCurrentUserRealName()

    NAME

            getCurrentUserRealName - gets the Google provided name of the user

    SYNOPSIS

            static String getCurrentUserRealName();

    DESCRIPTION

            Every user has two names, one real name which is immutable by the user and the other
            one which is the display name which can be changed by the user.  This function returns
            the real name.

    RETURNS

            String with the real name of the user

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static String getCurrentUserRealName(){
        return mUserAccount.getDisplayName();
    }


    /**/
    /*
    getCurrentUserID() getCurrentUserID()

    NAME

            getCurrentUserID - gets the Google provided ID of the user

    SYNOPSIS

            static String getCurrentUserID();

    DESCRIPTION

            Every user has a user id assigned by Google.  This functions return that user id and
            is used when content is saved to reference back to the creator of the content.

    RETURNS

            String with the id of the user

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static String getCurrentUserID(){
        return mUserAccount.getId();
    }


    /**/
    /*
    getCurrentUserPhoto() getCurrentUserPhoto()

    NAME

            getCurrentUserPhoto - gets the Google account photo URL of the user

    SYNOPSIS

            static String getCurrentUserPhoto();

    DESCRIPTION

            This functions gets the photo URL of the signed in user from Google.

    RETURNS

            String with the photo URL of the user

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static String getCurrentUserPhoto(){
        return mUserAccount.getPhotoUrl().toString();
    }


    /**/
    /*
    getCurrentUserEmail() getCurrentUserEmail()

    NAME

            getCurrentUserEmail - gets the Google account email of the user

    SYNOPSIS

            static String getCurrentUserEmail();

    DESCRIPTION

            This functions gets the email that the user used to sign in to the app.

    RETURNS

            String with the email of the user

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static String getCurrentUserEmail(){
        return mUserAccount.getEmail();
    }


    /**/
    /*
    appendDisplayName() appendDisplayName()

    NAME

            appendDisplayName - get the display name for the provided account id and appends to view

    SYNOPSIS

            static void appendDisplayName(final String accountID, final TextView displayView);

            accountID -> a string containing the account id of the user whose name is to be
                         extracted from the database
            displayView -> once the data is extracted, the view to which the name should be appended
                           to

    DESCRIPTION

            This functions gets the display name of the account from the database whose id is
            provided and appends that display name to the displayView provided.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void appendDisplayName(final String accountID, final TextView displayView){
        init();

        final DatabaseReference user = mDatabase.child(DB_DOCUMENT_USERS).child(accountID);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    displayView.append((String) ((HashMap) dataSnapshot.getValue()).get(DB_KEY_NAME));
                } else {
                    displayView.append("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "find user operation cancelled");
            }
        });
    }


    /**/
    /*
    changeDisplayName() changeDisplayName()

    NAME

            changeDisplayName - changes the display name for the current user

    SYNOPSIS

            static void changeDisplayName(String newName);

            newName -> the new name for the current user

    DESCRIPTION

            This function changes the display name for the current user to whatever is provided
            as argument to the function.  The changes is made in the database's entry

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void changeDisplayName(String newName){
        init();

        mDatabase.child(DB_DOCUMENT_USERS)
                .child(getCurrentUserID()).child(DB_KEY_NAME).setValue(newName);
    }


    /**/
    /*
    getMetaData() getMetaData()

    NAME

            getMetaData - gets meta data from the database

    SYNOPSIS

            static void getMetaData(final String section, final RecyclerView recyclerView);

            section -> the section for which to get the meta data
            recyclerView -> the view to which to populate the retrieved meta data

    DESCRIPTION

            This function retrieves the meta data for the given section from the database and
            populates the data into a custom adapter which is set as the adapter for the
            recycler view on screen so the items can be displayed.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void getMetaData(final String section, final RecyclerView recyclerView){
        init();

        DatabaseReference newRef = mDatabase.child(section).child(DB_DOCUMENT_META_DATA);

        final MyAdapter adapter = new MyAdapter(section);
        recyclerView.setAdapter(adapter);

        newRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.addData((HashMap) dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.changeData((HashMap) dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.removeData((HashMap) dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**/
    /*
    getDetailedData() getDetailedData()

    NAME

            getDetailedData - gets detailed data for the provided entry

    SYNOPSIS

            static void getDetailedData(String section, String entryID, final DetailActivity viewActivity);

            section -> the section that the entry belongs in
            entryID -> the entry id for the entry whose content is to be extracted
            viewActivity -> the view in which the data should be passed

    DESCRIPTION

            This function retrieves the detailed data for the given entry from the database and
            populates the data into the supplied view's field so it can be displayed.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void getDetailedData(String section, String entryID, final DetailActivity viewActivity){
        init();

        DatabaseReference newRef = mDatabase.child(section).child(DB_DOCUMENT_CONTENT).child(entryID);

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


    /**/
    /*
    insertData() insertData()

    NAME

            insertData - insert data into the database

    SYNOPSIS

            static void insertData(String section, String title, String content);

            section -> the section in which to insert data
            title -> the title for the data to be inserted
            content -> the content for the data to be inserted

    DESCRIPTION

            This function creates a HashMap from the title and content provided and invokes the
            other insertData function which does the actual insertion into the database.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void insertData(String section, String title, String content){
        HashMap<String, String> entry = new HashMap<>();
        entry.put(DB_KEY_TITLE, title);
        entry.put(DB_KEY_CONTENT, content);

        insertData(section, entry);
    }


    /**/
    /*
    insertData() insertData()

    NAME

            insertData - insert data into the database

    SYNOPSIS

            static void insertData(String section, HashMap<String, String> entry);

            section -> the section in which to insert data
            entry -> the entry to be inserted

    DESCRIPTION

            This function breaks down the provided data into meta data and content section and
            inserts the data in to the respective section in the database.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void insertData(String section, HashMap<String, String> entry){
        init();

        DatabaseReference metaDataDocument = mDatabase.child(section).child(DB_DOCUMENT_META_DATA);
        DatabaseReference contentDocument = mDatabase.child(section).child(DB_DOCUMENT_CONTENT);

        if(!entry.containsKey(DB_KEY_TITLE) || !entry.containsKey(DB_KEY_CONTENT))
            return;

        // generate a random universally unique identifier for the entry
        String entryID = UUID.randomUUID().toString();
        entry.put(DB_KEY_DOCID, entryID);

        HashMap<String, String> metaDataEntry = getMetaDataMap(entry);
        HashMap<String, String> contentEntry = getContentMap(entry);

        metaDataDocument.child(entryID).setValue(metaDataEntry);
        contentDocument.child(entryID).setValue(contentEntry);
    }


    /**/
    /*
    getMetaDataMap() getMetaDataMap()

    NAME

            getMetaDataMap - get the meta data entry

    SYNOPSIS

            static HashMap<String, String> getMetaDataMap(HashMap<String, String> entry)

            entry -> the title and content of the entry to be inserted

    DESCRIPTION

            This function removes and adds the required data in to the provided HashMap and returns
            a HashMap object that can be inserted into the meta data section.

    RETURNS

            A HashMap with the key-value pairs of data to be inserted in the meta data document.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private static HashMap<String, String> getMetaDataMap(HashMap<String, String> entry){
        HashMap<String, String> modifiedEntry = new HashMap<>(entry);
        modifiedEntry.remove(DB_KEY_CONTENT);

        modifiedEntry.put(DB_KEY_AUTHORID, getCurrentUserID());
        modifiedEntry.put(DB_KEY_TIME, getTodaysDate());
        return modifiedEntry;
    }


    /**/
    /*
    getContentMap() getContentMap()

    NAME

            getContentMap - get the content data entry

    SYNOPSIS

            static HashMap<String, String> getContentMap(HashMap<String, String> entry)

            entry -> the title and content of the entry to be inserted

    DESCRIPTION

            This function removes and adds the required data in to the provided HashMap and returns
            a HashMap object that can be inserted into the content section.

    RETURNS

            A HashMap with the key-value pairs of data to be inserted in the content document.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private static HashMap<String, String> getContentMap(HashMap<String, String> entry){
        HashMap<String, String> modifiedEntry = new HashMap<>(entry);
        modifiedEntry.put(DB_KEY_AUTHORID, getCurrentUserID());
        modifiedEntry.put(DB_KEY_TIME, getTodaysDate());
        return modifiedEntry;
    }


    /**/
    /*
    getTodaysDate() getTodaysDate()

    NAME

            getTodaysDate - get today's date

    SYNOPSIS

            static String getTodaysDate();

    DESCRIPTION

            This function gets today's date in the format YYYY/MM/DD.

    RETURNS

            A String with today's date.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static String getTodaysDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }


    /**/
    /*
    deleteEntry() deleteEntry()

    NAME

            deleteEntry - delete an entry from the database

    SYNOPSIS

            void deleteEntry(String section, String entryID)

            section -> the section to delete the entry from
            entryID -> the id of the entry to delete

    DESCRIPTION

            This function deletes the entry with the given id from the respective section in the
            database.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static void deleteEntry(String section, String entryID){
        init();

        DatabaseReference sectionRef = mDatabase.child(section);

        sectionRef.child(Database.DB_DOCUMENT_META_DATA).child(entryID).removeValue();
        sectionRef.child(Database.DB_DOCUMENT_CONTENT).child(entryID).removeValue();
    }
}
