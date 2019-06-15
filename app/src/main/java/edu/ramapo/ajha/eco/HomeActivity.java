package edu.ramapo.ajha.eco;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private GoogleSignInClient mGoogleSignInClient;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

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


    // A FragmentPagerAdapter that returns a fragment corresponding to one of the tabs
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

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        proceedToSignInActivity();
                    }
                });
    }

    private void proceedToSignInActivity() {
        // close current activity
        this.finish();

        // proceed to the home activity screen i.e. the main screen of the app
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("signOut", true);
        startActivity(intent);
    }

    private void proceedToSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
