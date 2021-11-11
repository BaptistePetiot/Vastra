package fr.android.bottomnav.ui.accueil;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import fr.android.bottomnav.User;
import fr.android.bottomnav.databinding.FragmentAccueilBinding;
import fr.android.bottomnav.ui.Training;
import fr.android.bottomnav.ui.entrainements.EntrainementsAdapter;

public class AccueilFragment extends Fragment {

    private AccueilViewModel accueilViewModel;
    private FragmentAccueilBinding binding;
    private FirebaseFirestore db;
    private ArrayList<Training> trainings = new ArrayList<Training>();
    private ImageView ivAccueil;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accueilViewModel =
                new ViewModelProvider(this).get(AccueilViewModel.class);

        binding = FragmentAccueilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Firebase
        db = FirebaseFirestore.getInstance();

        ivAccueil = (ImageView) binding.ivAccueil;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db.collection("trainings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            long max = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // if the document id of the training contains the uid (ie email) then display it
                                if(document.getId().contains(User.uid) && Long.parseUnsignedLong(document.getId().split("_")[1]) > max){
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

                            // get the most recent training of the user
                            Training training = trainings.get(trainings.size()-1);
                            // get the path of the last picture taken
                            String uri = training.getImages().get(training.getImages().size()-1);
                            File file = new File(uri);
                            ivAccueil.setImageURI(Uri.fromFile(file));

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