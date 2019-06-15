package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;


/**/
/*
DetailActivity class

DESCRIPTION

        This activity is a child activity of the main HomeActivity.  The parent activity displays
        on the screen the meta data for all the different topics available in the database in a
        list form.  When the user selects one of the topic, this activity is launched by the
        parent activity.  This activity takes in a documentID (unique identifier) and a section
        type and displays the complete content of the topic on the screen i.e. the
        content by downloading it from the database.  If the user who has opened it is the creator
        of the topic, he/she has the options available to delete it from the database permanently.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private HashMap mData;
    private String mSection;


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
        setContentView(R.layout.activity_detail);

        /* set up the action bar */
        Toolbar actionBar = findViewById(R.id.toolbar_detail_activity);
        setSupportActionBar(actionBar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mSection = getIntent().getStringExtra("section");
        String docID = getIntent().getStringExtra("docID");
        int index = getIntent().getIntExtra("index", 0);

        setTitle(mSection, index);

        Database.getDetailedData(mSection, docID, this);
    }


    /**/
    /*
    setTitle() setTitle()

    NAME

            setTitle - sets the title for the app bar

    SYNOPSIS

            void setTitle();

    DESCRIPTION

            This function sets the title of the DetailActivity as "<section name>"

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void setTitle(String section, int index){
        TextView sectionTitle = findViewById(R.id.section_detail_activity);

        String titleText = getSectionDisplay(section);

        /*  the index gets invalidated when there is a change in the data elements
        // to convert from 0 based index to 1 based index
        index = index + 1;
        titleText += " #" + index;
        */

        sectionTitle.setText(titleText);

        // select so that marquee can be activated if needed
        sectionTitle.setSelected(true);
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
    setData() setData()

    NAME

            setData - sets the data

    SYNOPSIS

            void setData(HashMap retrievedData);

            retrievedData -> the data retrieved from the database

    DESCRIPTION

            This function is invoked by the database and sets the data and calls showData is
            data is available.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void setData(HashMap retrievedData){
        mData = retrievedData;

        if(mData != null)
            showData();
    }


    /**/
    /*
    showData() showData()

    NAME

            showData - shows the data on screen

    SYNOPSIS

            void showData();

    DESCRIPTION

            This function shows the retrieved data on the screen.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    private void showData(){
        TextView title = findViewById(R.id.title_detail_activity);
        TextView author = findViewById(R.id.author_detail_activity);
        TextView time = findViewById(R.id.time_detail_activity);
        TextView content = findViewById(R.id.content_detail_activity);

        title.setText((String) mData.get(Database.DB_KEY_TITLE));

        String authorTextPrefix = "By ";
        author.setText(authorTextPrefix);
        Database.appendDisplayName((String) mData.get(Database.DB_KEY_AUTHORID), author);

        time.setText((String) mData.get(Database.DB_KEY_TIME));

        content.setText((String) mData.get(Database.DB_KEY_CONTENT));

        // check to see if the author is the current user, if it is show the delete button
        if(Database.getCurrentUserID().equals(mData.get(Database.DB_KEY_AUTHORID))){
            ImageButton deleteButton = findViewById(R.id.delete_button_detail_activity);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlertDialog();
                }
            });
        }
    }


    /**/
    /*
    onOptionsItemSelected() onOptionsItemSelected()

    NAME

            onOptionsItemSelected - the click handler for the selected views on screen

    SYNOPSIS

            boolean onOptionsItemSelected(MenuItem item);

            item -> item is the view that has been clicked on the menu screen

    DESCRIPTION

            This function registers clicks on the views on the menu screen and responds accordingly.

    RETURNS

            Returns true if operation is successful

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
    deleteAlertDialog() deleteAlertDialog()

    NAME

            deleteAlertDialog - safe delete prompt dialog

    SYNOPSIS

            void deleteAlertDialog();

    DESCRIPTION

            Displays the delete alert dialog which asks the user is they really want to delete the
            entry from the database.  If the user chooses yes, the entry is deleted.  If not, the
            dialog is cancelled and the activity resumed.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    public void deleteAlertDialog(){
        final Activity thisActivity = this;

        AlertDialog.Builder builder1 = new AlertDialog.Builder(thisActivity);
        builder1.setMessage("Delete this " + getSectionDisplay(mSection) + " item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Database.deleteEntry(mSection, (String) mData.get(Database.DB_KEY_DOCID));
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

        AlertDialog deleteAlert = builder1.create();
        deleteAlert.show();
    }
}
