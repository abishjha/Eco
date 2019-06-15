package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;


/**/
/*
SettingsActivity class

DESCRIPTION

        This activity is a child activity of the main HomeActivity.  The parent activity has a
        action bar from which this activity is launched.  It is a basic activity which shows the
        currently signed in user's profile photo, name, and email.  It also shows the users display
        name and provides the option to change it so it will be changed across the contents of the
        app.  An empty name or name shorter than three letters is not allowed.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class SettingsActivity extends AppCompatActivity {

    Button mEditButton;
    Button mSaveAndContinueButton;
    EditText mDisplayName;


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
        setContentView(R.layout.activity_settings);

        /* set up the action bar */
        Toolbar actionBar = findViewById(R.id.toolbar_settings_activity);
        setSupportActionBar(actionBar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mEditButton = findViewById(R.id.edit_button_settings_activity);

        mSaveAndContinueButton = findViewById(R.id.save_and_continue_button_settings_activity);
        mSaveAndContinueButton.setVisibility(View.GONE);

        mDisplayName = findViewById(R.id.name_settings_activity);
        mDisplayName.setEnabled(false);

        setTitle();
        setImage();
        setDisplayName();
        setRealName();
        setEmail();
    }


    /**/
    /*
    setTitle() setTitle()

    NAME

            setTitle - sets the title for the app bar

    SYNOPSIS

            void setTitle();

    DESCRIPTION

            This function sets the title of the SettingsActivity as "Settings"

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setTitle(){
        TextView title = findViewById(R.id.title_settings_activity);
        title.setText(getString(R.string.settings_activity_header));

        // select so that marquee can be activated if needed
        title.setSelected(true);
    }


    /**/
    /*
    setImage() setImage()

    NAME

            setImage - displays the image of the user

    SYNOPSIS

            void setImage();

    DESCRIPTION

            This function gets and sets the image of the current user profile to be displayed on
            the screen.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setImage(){
        String photoURL = Database.getCurrentUserPhoto();
        ImageView photo = findViewById(R.id.image_settings_activity);

        Glide.with(this).load(photoURL).into(photo);
    }


    /**/
    /*
    setDisplayName() setDisplayName()

    NAME

            setDisplayName - displays the display name of the user

    SYNOPSIS

            void setDisplayName();

    DESCRIPTION

            This function gets and sets the display name of the current user to be displayed on the
            screen.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setDisplayName() {
        mDisplayName.setText("");
        Database.appendDisplayName(Database.getCurrentUserID(), mDisplayName);
    }


    /**/
    /*
    setRealName() setRealName()

    NAME

            setRealName - displays the real name of the user

    SYNOPSIS

            void setRealName();

    DESCRIPTION

            This function gets and sets the real name of the current user to be displayed on the
            screen.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setRealName() {
        TextView name = findViewById(R.id.real_name_settings_activity);
        name.setText(Database.getCurrentUserRealName());
    }


    /**/
    /*
    setEmail() setEmail()

    NAME

            setEmail - displays the email of the user

    SYNOPSIS

            void setEmail();

    DESCRIPTION

            This function gets and sets the email of the current user to be displayed on the screen.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setEmail() {
        TextView email = findViewById(R.id.email_settings_activity);
        email.setText(Database.getCurrentUserEmail());
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
        // handle back arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return true;
    }


    /**/
    /*
    editClick() editClick()

    NAME

            editClick - the click handler for the edit button

    SYNOPSIS

            void editClick(View view);

            view -> the view which is the item that this function is called onClick

    DESCRIPTION

            This function is the click handler for the edit display name button.  It makes the
            display name field editable and swaps the edit button with the save and continue button.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void editClick(View view){
        mEditButton.setVisibility(View.GONE);

        mDisplayName.setEnabled(true);
        mDisplayName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mSaveAndContinueButton.setVisibility(View.VISIBLE);
    }


    /**/
    /*
    saveAndContinueClick() saveAndContinueClick()

    NAME

            saveAndContinueClick - the click handler for the save and continue button

    SYNOPSIS

            void saveAndContinueClick(View view);

            view -> the view which is the item that this function is called onClick

    DESCRIPTION

            This function is the click handler for the save and continue button.  It gets the text
            from the display name field and verifies it.  If it is good, it enters it into the
            database and finishes the activity.  If it is not good, it does not change what is in
            the database and finishes the activity.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void saveAndContinueClick(View view){
        String name = ((EditText) findViewById(R.id.name_settings_activity)).getText().toString();
        if(name.isEmpty() || name.length() < 3) {
            Toast.makeText(SettingsActivity.this, R.string.no_name_input_revert_to_previous, Toast.LENGTH_SHORT).show();
        } else {
            Database.changeDisplayName(name);
            Toast.makeText(SettingsActivity.this, R.string.name_changed, Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }

}
