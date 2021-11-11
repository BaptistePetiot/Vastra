package fr.android.bottomnav.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.nio.charset.Charset;
import java.util.Random;

import fr.android.bottomnav.DBHandler;
import fr.android.bottomnav.R;
import fr.android.bottomnav.User;
import fr.android.bottomnav.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private TextView tvRunnerCode, tvEmail;
    private EditText editTextFirstName, editTextLastName, editTextAge, editTextCountry, editTextHeight, editTextWeight;
    private DBHandler dbHandler;
    private Button editUserBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextFirstName = binding.editTextFirstName;
        editTextLastName = binding.editTextLastName;
        tvEmail = binding.tvEmail;
        editTextAge = binding.editTextAge;
        editTextCountry = binding.editTextCountry;
        editTextHeight = binding.editTextHeight;
        editTextWeight = binding.editTextWeight;
        tvRunnerCode = binding.tvRunnerCode;

        // retrieve user info in the sqlite database
        dbHandler = new DBHandler(getContext());
        dbHandler.getUser(User.email);

        displayUser();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editUserBtn = (Button) view.findViewById(R.id.editUserBtn);
        editUserBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editUser();
            }
        });
    }

    public void displayUser(){
        editTextFirstName.setText(User.firstName);
        editTextLastName.setText(User.lastName);
        tvEmail.setText(User.email);
        editTextAge.setText(Integer.toString(User.age));
        editTextCountry.setText(User.country);
        editTextHeight.setText(Double.toString(User.height));
        editTextWeight.setText(Double.toString(User.weight));
        tvRunnerCode.setText(User.code);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** Called when the user taps the 'edit User' button */
    public void editUser() {
        // retrieve new values
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        int age = Integer.parseInt(editTextAge.getText().toString());
        String country = editTextCountry.getText().toString();
        double height = Double.parseDouble(editTextHeight.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());

        // update sqlite db
        dbHandler.updateTable(User.email, firstName, lastName, age, country, height, weight);
        // upadte user info
        dbHandler.getUser(User.email);
        Toast.makeText(getActivity(), "Datas have been saved", Toast.LENGTH_LONG).show();

        displayUser();
    }
}