package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

/***
 * This activity class takes in a unique identifier for the data in the table,
 * downloads the content from the database and displays it on the screen
 */

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private HashMap mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /** set up the action bar */
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar_detail_activity);
        setSupportActionBar(actionBar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        String section = getIntent().getStringExtra("section");
        String docID = getIntent().getStringExtra("docID");
        int index = getIntent().getIntExtra("index", 0);

        setTitle(section, index);

        Database.getDetailedData(section, docID, this);
    }

    private void setTitle(String section, int index){
        TextView sectionTitle = (TextView) findViewById(R.id.section_detail_activity);

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
        TextView title = (TextView) findViewById(R.id.title_detail_activity);
        TextView author = (TextView) findViewById(R.id.author_detail_activity);
        TextView time = (TextView) findViewById(R.id.time_detail_activity);
        TextView content = (TextView) findViewById(R.id.content_detail_activity);

        title.setText((String) mData.get("title"));

        String authorText = "By " + (String) mData.get("author");
        author.setText(authorText);

        time.setText((String) mData.get("time"));

        content.setText((String) mData.get("content"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle back arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return true;
    }
}
