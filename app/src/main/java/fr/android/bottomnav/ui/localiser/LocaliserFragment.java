package fr.android.bottomnav.ui.localiser;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import fr.android.bottomnav.databinding.FragmentLocaliserBinding;


public class LocaliserFragment extends Fragment {

    private LocaliserViewModel localiserViewModel;
    private FragmentLocaliserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        localiserViewModel =
                new ViewModelProvider(this).get(LocaliserViewModel.class);

        binding = FragmentLocaliserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}