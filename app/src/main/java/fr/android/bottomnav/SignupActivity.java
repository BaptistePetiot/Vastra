package fr.android.bottomnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        firstName = (EditText) findViewById(R.id.editTextSignupFirstName);
        firstName.requestFocus();

        lastName = (EditText) findViewById(R.id.editTextSignupLastName);
        email = (EditText) findViewById(R.id.editTextSignupEmail);
        password = (EditText) findViewById(R.id.editTextSignupPassword);
        age = (EditText) findViewById(R.id.editTextSignupAge);
        country = (EditText) findViewById(R.id.editTextSignupCountry);
        height = (EditText) findViewById(R.id.editTextSignupHeight);
        weight = (EditText) findViewById(R.id.editTextSignupWeight);
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
        Intent intent = new Intent(this, LoginActivity.class);

        // register important data in the remote database
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        // register personal data in the sqlite local database
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addNewUser(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), Integer.parseInt(age.getText().toString()), country.getText().toString(), Double.parseDouble(height.getText().toString()), Double.parseDouble(weight.getText().toString()));

    }

}