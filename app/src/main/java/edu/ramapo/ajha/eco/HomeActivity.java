package edu.ramapo.ajha.eco;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**/
/*
HomeActivity class

DESCRIPTION

        This activity is the main activity.  It has an app bar and the tab bar to display all the
        available tabs.  This activity has a SectionsPagerAdapter which displays the appropriate
        tab on the screen inside the ViewPager element.  It also has a floating action button
        which launches the AddItemActivity where a user can insert content into the currently
        selected tab/section.  On the app bar, there is a action menu with two options, one is
        settings which launches the SettingsActivity and the other is the SignOut action which
        logs out the current user from the the app.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private GoogleSignInClient mGoogleSignInClient;


    /**/
    /*
    SectionsPagerAdapter class

    DESCRIPTION

            This adapter class returns a fragment corresponding to each of the sections/tabs.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String currFragment = indexToNameFragmentMap(position);
            return DisplayListFragment.newInstance(currFragment);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }


    /**/
    /*
    onCreate() onCreate()

    NAME

            onCreate - function called on activity creation

    SYNOPSIS

            void onCreate(Bundle savedInstanceState);

            savedInstanceState -> the saved instance state passed by the OS

    DESCRIPTION

            The function called on create of the activity and is used to do initial setups.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the five
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    /**/
    /*
    onStart() onStart()

    NAME

            onStart - called on activity start

    SYNOPSIS

            void onStart();

    DESCRIPTION

            The function called when the view has been rendered and everything is setup on the call
            stack so variables and items can be initialized.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    protected void onStart() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onStart();
    }


    /**/
    /*
    onCreateOptionsMenu() onCreateOptionsMenu()

    NAME

            onCreateOptionsMenu - function called on activity creation for menu

    SYNOPSIS

            boolean onCreateOptionsMenu(Menu menu);

            menu -> the view where we can populate our action menu

    DESCRIPTION

            The function is called to setup the action menu.

    RETURNS

            Returns true if menu was successfully attached and inflated.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    /**/
    /*
    onOptionsItemSelected() onOptionsItemSelected()

    NAME

            onOptionsItemSelected - the click handler for the selected views on screen

    SYNOPSIS

            boolean onOptionsItemSelected(MenuItem item);

            item -> the view that has been clicked on the menu screen

    DESCRIPTION

            This function registers clicks on the views on the menu screen and responds accordingly.

    RETURNS

            Returns true if operation is successful.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            proceedToSettingsActivity();
        } else if (id == R.id.action_signout) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    /**/
    /*
    newItemFabOnClick() newItemFabOnClick()

    NAME

            newItemFabOnClick - the click handler for the save button

    SYNOPSIS

            void newItemFabOnClick(View view);

            view -> the view which is the item that this function is called onClick

    DESCRIPTION

            This function is the click handler for the fab button.  It gets the section it is
            currently on and calls the AddItemActivity for that section.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void newItemFabOnClick(View view) {
        int tabIndex = mViewPager.getCurrentItem();

        // proceed to the add item activity screen
        Intent intent = new Intent(view.getContext(), AddItemActivity.class);

        // include the data point unique identifier for the next activity
        intent.putExtra("section", indexToNameFragmentMap(tabIndex));

        ((Activity) view.getContext()).startActivityForResult(intent, 0);

        // Snackbar.make(view, indexToNameFragmentMap(tabIndex), Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
    }


    /**/
    /*
    indexToNameFragmentMap() indexToNameFragmentMap()

    NAME

            indexToNameFragmentMap - gets the section name from the provided tab index

    SYNOPSIS

            String indexToNameFragmentMap(int index);

            index -> the index of the current section/tab

    DESCRIPTION

            This function takes the index of the section in use and returns the name for the
            section.

    RETURNS

            Returns the section name.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public String indexToNameFragmentMap(int index) {
        String currFragment = "";
        switch (index) {
            case 0:
                currFragment = getResources().getString(R.string.section_ecological_awareness);
                break;
            case 1:
                currFragment = getResources().getString(R.string.section_events);
                break;
            case 2:
                currFragment = getResources().getString(R.string.section_stories);
                break;
            case 3:
                currFragment = getResources().getString(R.string.section_discussion);
                break;
            case 4:
                currFragment = getResources().getString(R.string.section_petitions);
                break;
        }
        return currFragment;
    }


    /**/
    /*
    signOut() signOut()

    NAME

            signOut - sign out from the account

    SYNOPSIS

            void signOut();

    DESCRIPTION

            This function signs the user out of the logged in account and invokes the
            proceedToSignInActivity screen.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        proceedToSignInActivity();
                    }
                });
    }


    /**/
    /*
    proceedToSignInActivity() proceedToSignInActivity()

    NAME

            proceedToSignInActivity - proceed to sign in activity

    SYNOPSIS

            void proceedToSignInActivity();

    DESCRIPTION

            This function finishes the current activity since the user signed out and proceeds
            to the SignInActivity.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void proceedToSignInActivity() {
        // close current activity
        this.finish();

        // proceed to the home activity screen i.e. the main screen of the app
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("signOut", true);
        startActivity(intent);
    }


    /**/
    /*
    proceedToSettingsActivity() proceedToSettingsActivity()

    NAME

            proceedToSettingsActivity - proceed to settings activity

    SYNOPSIS

            void proceedToSettingsActivity();

    DESCRIPTION

            This function proceeds to the SettingsActivity but without finishing this activity so
            it can be returned to.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void proceedToSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
