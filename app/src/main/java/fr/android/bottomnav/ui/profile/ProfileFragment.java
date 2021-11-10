package fr.android.bottomnav.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.nio.charset.Charset;
import java.util.Random;

import fr.android.bottomnav.User;
import fr.android.bottomnav.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private TextView runnerCode;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextAge, editTextCountry, editTextHeight, editTextWeight;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        runnerCode = binding.runnerCode;

        // generate runner code
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        runnerCode.setText(generatedString);

        /*final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        displayUser();

        return root;
    }

    public void displayUser(){
        editTextFirstName.setText(User.firstName);
        editTextLastName.setText(User.lastName);
        editTextEmail.setText(User.email);
        editTextAge.setText(User.age);
        editTextCountry.setText(User.country);
        editTextHeight.setText((int) User.height);
        editTextWeight.setText((int) User.weight);

        /*
        try {
            editTextFirstName.setText(User.firstName);
            editTextLastName.setText(User.lastName);
            editTextEmail.setText(User.email);
            editTextAge.setText(User.age);
            editTextCountry.setText(User.country);
            editTextHeight.setText((int) User.height);
            editTextWeight.setText((int) User.weight);

        }catch (Exception e){
            editTextFirstName.setText("");
            editTextLastName.setText("");
            editTextEmail.setText("");
            editTextAge.setText("");
            editTextCountry.setText("");
            editTextHeight.setText("");
            editTextWeight.setText("");
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** Called when the user taps the 'edit User' button */
    public void editUser(View view) {
        User.firstName = editTextFirstName.getText().toString();
        User.lastName = editTextLastName.getText().toString();
        User.email = editTextEmail.getText().toString();
        User.age = Integer.parseInt(editTextAge.getText().toString());
        User.country = editTextCountry.getText().toString();
        User.height = Double.parseDouble(editTextHeight.getText().toString());
        User.weight = Double.parseDouble(editTextWeight.getText().toString());

        displayUser();
    }
}