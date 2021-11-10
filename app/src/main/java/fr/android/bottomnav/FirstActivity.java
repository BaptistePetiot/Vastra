package fr.android.bottomnav;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class FirstActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageSwitcher imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // stop showing the splash screen and load the default theme when the app has loaded
        setTheme(R.style.firstTheme);

        setContentView(R.layout.activity_first);

        mAuth = FirebaseAuth.getInstance();

       /* imageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);

        // Animation when switching to another image.
        Animation out= AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        Animation in= AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        // Set animation when switching images.
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);*/


        /*imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            // Returns the view to show Image
            // (Usually should use ImageView)
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());

                imageView.setBackgroundColor(Color.LTGRAY);
                imageView.setScaleType(ImageView.ScaleType.CENTER);

                ImageSwitcher.LayoutParams params= new ImageSwitcher.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
                return imageView;
            }
        });*/

        /*imageSwitcher.postDelayed(new Runnable() {
            int i = 0;
            public void run() {
                imageSwitcher.setImageResource(
                        i++ % 2 == 0 ?
                                R.drawable.coureur :
                                R.drawable.cycliste);
                imageSwitcher.postDelayed(this, 1000);
            }
        }, 1000);*/
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

}