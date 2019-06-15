package edu.ramapo.ajha.eco;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


/**/
/*
SignInActivity class

DESCRIPTION

        This is the launcher activity for the app.  The first time a user launches the app, this
        activity prompts the user a welcome screen and a sign in button that allows the user to sign
        into the app using a Google account which is very convenient and can be done in a click.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;


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
        setContentView(R.layout.activity_signin);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // show toast if the user signed out
        if(getIntent().getBooleanExtra("signOut", false))
            Toast.makeText(SignInActivity.this, R.string.signed_out, Toast.LENGTH_SHORT).show();
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
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }


    /**/
    /*
    signIn() signIn()

    NAME

            signIn - get data back on activity end

    SYNOPSIS

            void signIn();

    DESCRIPTION

            This function called when for signing in.  It gets the user account and starts the
            sign in intent activity for verification.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    /**/
    /*
    onActivityResult() onActivityResult()

    NAME

            onActivityResult - get data back on activity end

    SYNOPSIS

            void onActivityResult(int requestCode, int resultCode, Intent data);

            requestCode -> the request code
            resultCode -> the result code
            data -> the returned data

    DESCRIPTION

            This function is used to check the sign in information from Google's activity and
            get the result whether the sign in was successful or not.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    /**/
    /*
    handleSignInResult() handleSignInResult()

    NAME

            handleSignInResult - handle the sign in result

    SYNOPSIS

            void handleSignInResult(Task<GoogleSignInAccount> completedTask);

            completedTask -> the completed task sent from the onActivityResult function if the
                             sign in was successful

    DESCRIPTION

            This function hand the sign in result by taking the complete task and extracting the
            Google sign in account from it and updating the view accordingly.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            updateUI(null);
        }
    }


    /**/
    /*
    updateUI() updateUI()

    NAME

            updateUI - update the UI if valid account is provided

    SYNOPSIS

            void updateUI(@Nullable GoogleSignInAccount account);

            account -> the google sign in account

    DESCRIPTION

            This function checks if the provided account has information in it and if it has,
            updates the view, registers the user in the database, and proceeds to the HomeActivity.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            Toast.makeText(SignInActivity.this, getString(R.string.signed_in_fmt, account.getDisplayName()), Toast.LENGTH_SHORT).show();
            Database.registerUser(account);
            proceedToHomeActivity();
        }
    }


    /**/
    /*
    onClick() onClick()

    NAME

            onClick - the on click event handler for the activity

    SYNOPSIS

            void onClick(View view);

            view -> the view which is the item that this function is called onClick

    DESCRIPTION

            This function is the click handler for the activity.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public void onClick(View view){
        if(view.getId() == R.id.sign_in_button)
            signIn();
    }


    /**/
    /*
    proceedToHomeActivity() proceedToHomeActivity()

    NAME

            proceedToHomeActivity - proceed to home activity

    SYNOPSIS

            void proceedToHomeActivity();

    DESCRIPTION

            This function finishes the current activity since the user signed in and proceeds
            to the HomeActivity.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void proceedToHomeActivity(){
        this.finish();

        // proceed to the home activity screen i.e. the main screen of the app
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
