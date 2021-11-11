package fr.android.bottomnav;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class FirstActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageSwitcher imageSwitcher;
    private boolean languageLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // stop showing the splash screen and load the default theme when the app has loaded
        setTheme(R.style.firstTheme);

        setContentView(R.layout.activity_first);

        mAuth = FirebaseAuth.getInstance();

        if(!languageLoaded){
            chooseLanguage();
            languageLoaded = true;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.w(String.valueOf(currentUser), "test_TAG");

        if(currentUser != null){
            //reload();
        }
    }

    /** Called when the user taps the signup button */
    public void signupEmail(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the login button */
    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //This method allow us to choose a language from the default device language :
    public void chooseLanguage(){
        //Get default language :
        Locale myLocale = new Locale(Resources.getSystem().getConfiguration().locale.getLanguage());
        //Define the right res file to use for the application which is strings.xml or strings.xml(en) :
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        startActivity(getIntent());
    }

}