package fr.android.bottomnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setTheme(R.style.signupTheme);
        setContentView(R.layout.activity_login);
        getActionBar().setIcon(R.drawable.ic_left_arrow);

        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        email.requestFocus();

        password = (EditText) findViewById(R.id.editTextTextPassword);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }

    /** Called when the user taps the login button */
    public void loginApp(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");

                            // update user if needed
                            user = mAuth.getCurrentUser();

                            if (user != null) {
                                for (UserInfo profile : user.getProviderData()) {
                                    // UID specific to the provider
                                    String uid = profile.getUid();
                                    User.uid = uid;
                                }
                            }

                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}