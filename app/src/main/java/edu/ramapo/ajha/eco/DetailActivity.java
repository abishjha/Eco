package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

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
