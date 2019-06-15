package edu.ramapo.ajha.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class SettingsActivity extends AppCompatActivity {

    Button mEditButton;
    Button mSaveAndContinueButton;
    EditText mDisplayName;

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

    private void setTitle(){
        TextView title = findViewById(R.id.title_settings_activity);
        title.setText(getString(R.string.settings_activity_header));

        // select so that marquee can be activated if needed
        title.setSelected(true);
    }

    private void setImage(){
        String photoURL = Database.getCurrentUserPhoto();
        ImageView photo = findViewById(R.id.image_settings_activity);

        Glide.with(this).load(photoURL).into(photo);
    }

    private void setDisplayName() {
        mDisplayName.setText("");
        Database.appendDisplayName(Database.getCurrentUserID(), mDisplayName);
    }

    private void setRealName() {
        TextView name = findViewById(R.id.real_name_settings_activity);
        name.setText(Database.getCurrentUserRealName());
    }

    private void setEmail() {
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

    public void editClick(View view){
        mEditButton.setVisibility(View.GONE);

        mDisplayName.setEnabled(true);
        mDisplayName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mSaveAndContinueButton.setVisibility(View.VISIBLE);
    }

    public void saveAndContinueClick(View view){
        String name = ((EditText) findViewById(R.id.name_settings_activity)).getText().toString();
        if(name.isEmpty()) {
            Toast.makeText(SettingsActivity.this, R.string.no_name_input_revert_to_previous, Toast.LENGTH_SHORT).show();
        } else {
            Database.changeDisplayName(name);
            Toast.makeText(SettingsActivity.this, R.string.name_changed, Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }

}
