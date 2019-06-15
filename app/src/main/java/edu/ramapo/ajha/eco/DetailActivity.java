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

/***
 * This activity class takes in a unique identifier for the data in the table,
 * downloads the content from the database and displays it on the screen
 */

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private HashMap mData;
    private String mSection;

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

    private void setTitle(String section, int index){
        TextView sectionTitle = findViewById(R.id.section_detail_activity);

        // to convert from 0 based index to 1 based index
        index = index + 1;
        String titleText = getSectionDisplay(section) + " #" + index;
        sectionTitle.setText(titleText);

        // select so that marquee can be activated if needed
        sectionTitle.setSelected(true);
    }

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

    public void setData(HashMap retrievedData){
        mData = retrievedData;

        if(mData != null)
            showData();
    }

    private void showData(){
        TextView title = findViewById(R.id.title_detail_activity);
        TextView author = findViewById(R.id.author_detail_activity);
        TextView time = findViewById(R.id.time_detail_activity);
        TextView content = findViewById(R.id.content_detail_activity);

        title.setText((String) mData.get(Database.DB_KEY_TITLE));

        String authorText = "By " + mData.get(Database.DB_KEY_AUTHOR);
        if(Database.getCurrentUserID().equals(mData.get(Database.DB_KEY_AUTHORID)))
            authorText += " (You)";
        author.setText(authorText);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle back arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return true;
    }

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
