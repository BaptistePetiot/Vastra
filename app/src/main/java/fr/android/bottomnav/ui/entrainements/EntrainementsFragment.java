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

import fr.android.bottomnav.User;
import fr.android.bottomnav.databinding.FragmentEntrainementsBinding;
import fr.android.bottomnav.ui.Training;

public class EntrainementsFragment extends Fragment {

    private EntrainementsViewModel entrainementsViewModel;
    private FragmentEntrainementsBinding binding;
    private FirebaseFirestore db;
    private EntrainementsAdapter ea;
    private ArrayList<Training> trainings = new ArrayList<Training>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        entrainementsViewModel =
                new ViewModelProvider(this).get(EntrainementsViewModel.class);

        binding = FragmentEntrainementsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Firebase
        db = FirebaseFirestore.getInstance();

        /*final TextView textView = binding.textEntrainements;
        entrainementsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
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
                                    ArrayList<String> r = (ArrayList<String>) document.getData().get("rythm");

                                    trainings.add(new Training(String.valueOf(document.getData().get("date")),
                                            String.valueOf(document.getData().get("distance")),
                                            String.valueOf(document.getData().get("duration")),
                                            String.valueOf(document.getData().get("lat_start")),
                                            String.valueOf(document.getData().get("lng_start")),
                                            String.valueOf(document.getData().get("lat_end")),
                                            String.valueOf(document.getData().get("lng_end")),
                                            p,
                                            r
                                            ));
                                }
                            }

                            // instantiate the EntrainementsAdapter with the context and the data to display
                            ea = new EntrainementsAdapter(trainings);
                            Log.d("DEBUG", String.valueOf(trainings.size()));
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        /*// CODE TO INSERT A NEW TRAINING
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put("date", "21/10/2021");
        docData.put("distance", "15km");
        docData.put("duration", "1h25");
        docData.put("lat_start", "48.85");
        docData.put("lng_start", "2.28");
        docData.put("lat_end", "48.85");
        docData.put("lng_end", "2.28");
        docData.put("path", Arrays.asList("48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268"));
        docData.put("rythm",Arrays.asList(5,6,7.30,8,4,5,5,5,5,5.3,5.2));

        // generate the document id based on the user id and the current timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = User.uid + "_" + String.valueOf(timestamp.getTime());

        // Add a new document (asynchronously) in collection "cities"
        db.collection("trainings")
                .document(ts)
                .set(docData);*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}