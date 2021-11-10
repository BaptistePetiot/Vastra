package fr.android.bottomnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    EditText email, password, firstName, lastName, age, country, height, weight;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setTheme(R.style.signupTheme);
        setContentView(R.layout.activity_signup);
        getActionBar().setIcon(R.drawable.ic_left_arrow);

        firstName = (EditText) findViewById(R.id.editTextFirstName);
        firstName.requestFocus();

        lastName = (EditText) findViewById(R.id.editTextLastName);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        age = (EditText) findViewById(R.id.editTextAge);
        country = (EditText) findViewById(R.id.editTextCountry);
        height = (EditText) findViewById(R.id.editTextHeight);
        weight = (EditText) findViewById(R.id.editTextWeight);
    }

    /*public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    // register data
    public void register(View view){
        //Intent intent = new Intent(this, LoginActivity.class);
        Log.d("TAG", "email : " + email.getText().toString() + " ,password : " + password.getText().toString());

        // register important data in the remote database
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        // register personal data in the sqlite local database
        //DBHandler dbHandler = new DBHandler(this);
        //dbHandler.addNewUser(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), Integer.parseInt(age.getText().toString()), country.getText().toString(), Double.parseDouble(height.getText().toString()), Double.parseDouble(weight.getText().toString()));

    }

}