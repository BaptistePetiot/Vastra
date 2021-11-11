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
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignupActivity extends AppCompatActivity {

    EditText email, password, firstName, lastName, age, country, height, weight;
    private FirebaseAuth mAuth;
    private DBHandler dbHandler;
    private String generatedString;
    private FirebaseFirestore db;

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

        // generate runner code
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();

        generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        dbHandler = new DBHandler(this);

        // Firebase
        db = FirebaseFirestore.getInstance();
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
    }

    // register data
    public void register(View view){
        Intent intent = new Intent(this, LoginActivity.class);

        // register personal data in the sqlite local database
        dbHandler.addNewUser(firstName.getText().toString(), lastName.getText().toString(),
                email.getText().toString(), Integer.parseInt(age.getText().toString()),
                country.getText().toString(), Double.parseDouble(height.getText().toString()),
                Double.parseDouble(weight.getText().toString()), generatedString);

        // register important data in the remote database
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        // register the code to firebase
        Map<String, Object> docData = new HashMap<>();
        docData.put("code", generatedString);
        docData.put("location", "");

        String id = email.getText().toString();

        // Add a new document (asynchronously) in collection "cities"
        db.collection("locations").document(id).set(docData);
    }

}