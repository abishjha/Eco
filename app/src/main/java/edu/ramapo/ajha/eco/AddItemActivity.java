package edu.ramapo.ajha.eco;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddItemActivity extends AppCompatActivity {

    private EditText mTitleEntry;
    private EditText mContentEntry;

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

        String section = getIntent().getStringExtra("section");
        setTitle(section);
    }

    private void setTitle(String section){
        TextView title = findViewById(R.id.title_add_item_activity);

        String titleText = "Add item -> " + getSectionDisplay(section);
        title.setText(titleText);

        // select so that marquee can be activated if needed
        title.setSelected(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle back arrow click here
        if (item.getItemId() == android.R.id.home) {
            exitAlertDialog();
        }

        return true;
    }

    public void saveButtonClick(View view){

    }

    public void cancelButtonClick(View view){
        exitAlertDialog();
    }

    public void exitAlertDialog(){
        final Activity thisActivity = this;

        // first check if there is any text in either of the input boxes
        // --> if there is text, prompt user before exiting
        // --> if there is none, simply exit
        if(mTitleEntry.getText().toString().isEmpty() && mContentEntry.getText().toString().isEmpty()) {
            thisActivity.finish();
            return;
        }

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
