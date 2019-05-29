package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/***
 * This activity class takes in a unique identifier for the data in the table,
 * downloads the content from the database and displays it on the screen
 *
 * TODO: do this --> set the title of the activity as the title of the event
 *               --> set a back button on the the title bar -- DONE
 *               --> make the title auto scroll if it does not fit into view
 *               --> content on the page
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar mActionBar = (Toolbar) findViewById(R.id.toolbar_detail_activity);
        setSupportActionBar(mActionBar);

        if(mActionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // get the docID passed in from the fragment handler.  docID is used to get content from the database
        /*** may also need the database path instance or the title of the fragment from which to extra the docID data
         * if not may have to restructure database to store all the contents in the same route regardless of section **/
        ((TextView) findViewById(R.id.text_docID)).setText(getIntent().getStringExtra("docID"));
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
