package edu.ramapo.ajha.eco;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**/
/*
AddItemActivity class

DESCRIPTION

        This activity is a child activity of the main HomeActivity.  It shows the user an
        interface to create new items and the option to push them to the database.  The
        activity has a back button that brings the user back to the parent activity.  If
        there is text entered and the user does not save it before exiting, it prompts the
        user to make sure the cancellation is not an accident.  The save button checks if the
        user has entered at least the necessary variables before exiting.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class AddItemActivity extends AppCompatActivity {

    private String mSection;
    private EditText mTitleEntry;
    private EditText mContentEntry;


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
        setContentView(R.layout.activity_add_item);

        mTitleEntry = findViewById(R.id.title_input_add_item_activity);
        mContentEntry = findViewById(R.id.content_input_add_item_activity);

        Toolbar actionBar = findViewById(R.id.toolbar_add_item_activity);
        setSupportActionBar(actionBar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mSection = getIntent().getStringExtra("section");
        setTitle();

        TextView author = findViewById(R.id.author_add_item_activity);
        Database.appendDisplayName(Database.getCurrentUserID(), author);
        ((TextView) findViewById(R.id.time_add_item_activity)).setText(Database.getTodaysDate());
    }


    /**/
    /*
    setTitle() setTitle()

    NAME

            setTitle - sets the title for the app bar

    SYNOPSIS

            void setTitle();

    DESCRIPTION

            This function sets the title of the AddItemActivity as "Add Item -> <section name>"

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setTitle(){
        TextView title = findViewById(R.id.title_add_item_activity);

        String titleText = "Add item -> " + getSectionDisplay(mSection);
        title.setText(titleText);

        // select so that marquee can be activated if needed
        title.setSelected(true);
    }


    /**/
    /*
    getSectionDisplay() getSectionDisplay()

    NAME

            getSectionDisplay - gets the display text for the sections

    SYNOPSIS

            String getSectionDisplay(String section);

            section -> the section for the data

    DESCRIPTION

            This function takes the name of the section in use which has a code name and
            returns the display name for the section.

    RETURNS

            Returns the display name with spaces and capitalization for the provided section name

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private String getSectionDisplay(String section){
        if(section.equals(getString(R.string.section_discussion)))
            return getString(R.string.title_discussion);
        else if(section.equals(getString(R.string.section_ecological_awareness)))
            return getString(R.string.title_ecological_awareness);
        else if(section.equals(getString(R.string.section_events)))
            return getString(R.string.title_events);
        else if(section.equals(getString(R.string.section_stories)))
            return getString(R.string.title_stories);
        else if(section.equals(getString(R.string.section_petitions)))
            return getString(R.string.title_petitions);

        return section;
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
            exitAlertDialog();
        }

        return true;
    }


    /**/
    /*
    saveButtonClick() saveButtonClick()

    NAME

            saveButtonClick - the click handler for the save button

    SYNOPSIS

            void saveButtonClick(View view);

            view -> the view which is the item that this function is called onClick

    DESCRIPTION

            This function is the click handler for the save button.  It gets the entered data,
            validates it and if good, saves it to the database.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void saveButtonClick(View view){
        if(mTitleEntry.getText().toString().isEmpty() || mContentEntry.getText().toString().isEmpty()) {
            Toast.makeText(AddItemActivity.this, R.string.no_data_input, Toast.LENGTH_SHORT).show();
            return;
        }

        Database.insertData(mSection, mTitleEntry.getText().toString(), mContentEntry.getText().toString());
        Toast.makeText(AddItemActivity.this, R.string.db_insertion_successful, Toast.LENGTH_SHORT).show();
        this.finish();
    }


    /**/
    /*
    cancelButtonClick() cancelButtonClick()

    NAME

            cancelButtonClick - the click handler for the cancel button

    SYNOPSIS

            void cancelButtonClick(View view);

            view -> the view which is the item that this function is called onClick

    DESCRIPTION

            This function is the click handler for the cancel button.  If no data has been entered,
            it simply finishes the activity and returns to the calling activity.  If some data has
            been entered, it calls the exitAlertDialog() function which makes sure the user wants
            to exit.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void cancelButtonClick(View view){
        // first check if there is any text in either of the input boxes
        // --> if there is none, simply exit
        if(mTitleEntry.getText().toString().isEmpty() && mContentEntry.getText().toString().isEmpty()) {
            this.finish();
            return;
        }

        // --> if there is text, prompt user before exiting
        exitAlertDialog();
    }


    /**/
    /*
    exitAlertDialog() exitAlertDialog()

    NAME

            exitAlertDialog - safe exit prompt dialog

    SYNOPSIS

            void exitAlertDialog();

    DESCRIPTION

            Displays the exit alert dialog which asks the user is they really want to exit the
            activity without saving the entered content.  If the user chooses yes, the activity
            is finished.  If not, the dialog is cancelled and the activity resumed.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void exitAlertDialog(){
        final Activity thisActivity = this;

        AlertDialog.Builder builder1 = new AlertDialog.Builder(thisActivity);
        builder1.setMessage("Discard the changes?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        thisActivity.finish();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog exitAlert = builder1.create();
        exitAlert.show();
    }
}
