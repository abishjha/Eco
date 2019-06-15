package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SettingsActivity extends AppCompatActivity {

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

        setTitle();
        setProfileImage();
        setProfileName();
        setProfileEmail();
    }

    private void setTitle(){
        TextView title = findViewById(R.id.title_settings_activity);
        title.setText(getString(R.string.settings_activity_header));

        // select so that marquee can be activated if needed
        title.setSelected(true);
    }

    private void setProfileImage(){
        String photoURL = Database.getCurrentUserPhoto();
        ImageView photo = findViewById(R.id.image_settings_activity);

        Glide.with(this).load(photoURL).into(photo);
    }

    private void setProfileName() {
        EditText name = findViewById(R.id.name_settings_activity);
        name.setText(Database.getCurrentUserName());
    }

    private void setProfileEmail() {
        TextView email = findViewById(R.id.email_settings_activity);
        email.setText(Database.getCurrentUserEmail());
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
