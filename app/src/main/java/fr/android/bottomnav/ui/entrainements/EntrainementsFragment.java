package fr.android.bottomnav.ui.entrainements;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.android.bottomnav.R;
import fr.android.bottomnav.User;
import fr.android.bottomnav.databinding.FragmentEntrainementsBinding;
import fr.android.bottomnav.ui.Training;

public class EntrainementsFragment extends Fragment {

    private EntrainementsViewModel entrainementsViewModel;
    private FragmentEntrainementsBinding binding;
    private FirebaseFirestore db;
    private ArrayList<Training> trainings = new ArrayList<Training>();
    private ViewGroup container;
    private LayoutInflater inflater;
    private EntrainementsAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        entrainementsViewModel =
                new ViewModelProvider(this).get(EntrainementsViewModel.class);

        binding = FragmentEntrainementsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inflater = inflater;
        container = container;

        recyclerView = (RecyclerView) binding.rvTrainings;

        // Firebase
        db = FirebaseFirestore.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.d("created", "onViewCreated: ");

        db.collection("trainings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // if the document id of the training contains the uid (ie email) then display it
                                if(document.getId().contains(User.uid)){
                                    Log.d("inserted", document.getId() + " => " + document.getData());

                                    // list des trainings récupérés
                                    // ajouter tous les trainings à la liste trainings
                                    ArrayList<String> p = (ArrayList<String>) document.getData().get("path");
                                    ArrayList<String> i = (ArrayList<String>) document.getData().get("images");

                                    trainings.add(new Training(String.valueOf(document.getData().get("dateAndHour")),
                                            String.valueOf(document.getData().get("distance")),
                                            String.valueOf(document.getData().get("duration")),
                                            String.valueOf(document.getData().get("address_start")),
                                            String.valueOf(document.getData().get("address_end")),
                                            p,
                                            i
                                            ));
                                }
                            }
                            for(Training training : trainings){
                                String date = training.getDateAndHour();
                                String distance = training.getDistance();
                                String duration = training.getDuration();
                            }

                            // set up the RecyclerView
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new EntrainementsAdapter(trainings);
                            //adapter.setClickListener(this);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
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