package fr.android.bottomnav.ui.localiser;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import fr.android.bottomnav.R;
import fr.android.bottomnav.User;
import fr.android.bottomnav.databinding.FragmentLocaliserBinding;
import fr.android.bottomnav.ui.Training;


public class LocaliserFragment extends Fragment implements OnMapReadyCallback {

    private LocaliserViewModel localiserViewModel;
    private FragmentLocaliserBinding binding;
    private EditText editTextCode;
    private Button buttonLocate;
    private FirebaseFirestore db;
    private GoogleMap gMap;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        localiserViewModel =
                new ViewModelProvider(this).get(LocaliserViewModel.class);

        binding = FragmentLocaliserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Firebase
        db = FirebaseFirestore.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapLocate);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        editTextCode = (EditText) binding.editTextCode;
        buttonLocate = (Button) binding.buttonLocate;
        buttonLocate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!editTextCode.getText().toString().equals("")){

                    // search in firebase
                    db.collection("locations")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                String code = String.valueOf(document.getData().get("code"));
                                                String location = String.valueOf(document.getData().get("location"));
                                                if(editTextCode.getText().toString().equals(code)){
                                                    double lat = Double.parseDouble(location.split("/")[0]);
                                                    double lng = Double.parseDouble(location.split("/")[1]);
                                                    LatLng coord = new LatLng(lat, lng);
                                                    gMap.clear();
                                                    gMap.addMarker(new MarkerOptions().position(coord));
                                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
                                                }
                                                Log.d("coord", location);
                                            }
                                    } else {
                                        Log.w("TAG", "Error getting documents.", task.getException());
                                    }
                                }
                            });

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}