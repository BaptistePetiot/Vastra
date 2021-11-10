package fr.android.bottomnav.ui.accueil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import fr.android.bottomnav.databinding.FragmentAccueilBinding;

public class AccueilFragment extends Fragment {

    private AccueilViewModel accueilViewModel;
    private FragmentAccueilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accueilViewModel =
                new ViewModelProvider(this).get(AccueilViewModel.class);

        binding = FragmentAccueilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAccueil;
        accueilViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}