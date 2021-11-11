package fr.android.bottomnav.ui.enregistrer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.android.bottomnav.R;
import fr.android.bottomnav.User;
import fr.android.bottomnav.ui.Training;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private LocationManager locationManager;
    private String provider;
    private boolean geoLocPermit = false;
    private boolean geoLocRequest = false;
    private final int PERMISSION_REQUEST_LOC = 0;
    private final int GPS_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private ImageButton imgBtn;
    private Button button_start, button_stop;
    private FirebaseFirestore db;

    private Training training;
    private Geocoder geocoder;

    int PERM_REQUEST = 1;
    private final int CAMERA_PERM_CODE = 101;

    // fused location version; don't forget to add the dependency to your build.gradle file
    // https://developers.google.com/android/guides/setup
    FusedLocationProviderClient fusedLocationClient;
    boolean askedOnce = false;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Firebase
        db = FirebaseFirestore.getInstance();

        // Gecoder
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // create new training
        training = new Training();

        // PHOTO
        imgBtn = (ImageButton) view.findViewById(R.id.imageButtonPhoto);
        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takePhoto();
            }
        });

        // START
        button_start = (Button) view.findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // set the training to started
                training.setStarted(true);

                // determine the date and beginning hour
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                training.setDateAndHour(formatter.format(date));
            }
        });

        // STOP
        button_stop = (Button) view.findViewById(R.id.button_stop);
        button_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // set the training to finished
                training.setFinished(true);

                // determine the strating and finishing addresses
                ArrayList<String> path = training.getPath();
                String startCoord = path.get(0);
                String endCoord = path.get(path.size() - 1);
                Double start_lat = Double.parseDouble(startCoord.split("/")[0]);
                Double start_lng = Double.parseDouble(startCoord.split("/")[1]);
                Double end_lat = Double.parseDouble(endCoord.split("/")[0]);
                Double end_lng = Double.parseDouble(endCoord.split("/")[1]);

                try {
                    String startAddress = geocoder.getFromLocation(start_lat, start_lng, 1).get(0).getAddressLine(0); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String endAddress = geocoder.getFromLocation(end_lat, end_lng, 1).get(0).getAddressLine(0); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    training.setAddress_start(startAddress);
                    training.setAddress_end(endAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // compute the total duration
                LocalTime beginningHour = LocalTime.parse(training.getDateAndHour().split(" ")[1]);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                LocalTime finishingHour = LocalTime.parse(formatter.format(date));
                Duration duration = Duration.between(beginningHour, finishingHour);
                training.setDuration(duration.toString().replace("PT", ""));

                // compute the total distance
                float totalDistance = 0;
                for(int i = 1; i < training.getPath().size(); i++){
                    String sm1 = training.getPath().get(i-1);
                    Double latm1 = Double.parseDouble(sm1.split("/")[0]);
                    Double lngm1 = Double.parseDouble(sm1.split("/")[1]);

                    String s = training.getPath().get(i);
                    Double lat = Double.parseDouble(s.split("/")[0]);
                    Double lng = Double.parseDouble(s.split("/")[1]);

                    float[] results = new float[1];
                    Location.distanceBetween(latm1, lngm1, lat, lng, results);
                    totalDistance += results[0];
                }
                training.setDistance(String.valueOf(totalDistance));

                // save the training to firebase
                Map<String, Object> docData = new HashMap<>();
                docData.put("dateAndHour", training.getDateAndHour());
                docData.put("distance", training.getDistance());
                docData.put("duration", training.getDuration());
                docData.put("address_start", training.getAddress_start());
                docData.put("address_end", training.getAddress_end());
                docData.put("path", training.getPath());
                docData.put("images", training.getImages());

                // generate the document id based on the user id and the timestamp at start
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String ts = User.uid + "_" + String.valueOf(timestamp.getTime());

                // Add a new document (asynchronously) in collection "cities"
                db.collection("trainings").document(ts).set(docData);

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Log.d("FUSED", "New location");
                // get the latest location available
                Location location = locationResult.getLastLocation();

                Log.d("FUSED", "lat: " + String.valueOf(location.getLatitude()) + ", long: " + (String.valueOf(location.getLongitude())));

                double lat =  location.getLatitude();
                double lng = location.getLongitude();
                LatLng coord = new LatLng(lat, lng);
                gMap.addMarker(new MarkerOptions().position(coord));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));

                // add new location to path
                if(training.isStarted() && !training.isFinished()){
                    String newCoord = lat + "/" + lng;
                    ArrayList<String> path = training.getPath();
                    path.add(newCoord);
                    training.setPath(path);
                }


            }
        };

        requestLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {

        if (fusedLocationClient != null)
          fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       askedOnce = true;

        if (requestCode == PERM_REQUEST) {
            // check grantResults
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
        }

        /*if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }else{
                Toast.makeText(getActivity(), "Camera permission required to use camera", Toast.LENGTH_LONG).show();
            }
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Store picture in picture field :
        Bitmap image = (Bitmap) data.getExtras().get("data");
        //Put picture on the imageView item :
        String filename = "image";
        picGallerySaver(image,filename,"test");
        //ArrayList<String> finalPaths = new ArrayList<String>();
        //finalPaths is an arraylist filled with the path of picture with a specific name (name of the run you want to display) :
        //finalPaths = getPathfromName(filename);

        /*//Display the picture :
        for(int i=0; i<finalPaths.size(); i++){
            File file = new File(finalPaths.get(i));
            imageView.get(i).setImageURI(Uri.fromFile(file));
        }*/
    }

    //This method stock the picture into device gallery. The method also stock the picture's path and name in the "Path" arraylist :
    public Uri picGallerySaver(Bitmap pic, String name, String description){
        //Stock in gallery :
        String savedImageURL = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),pic,name,description);
        //Parse the gallery image url to uri :
        Uri savedImageURI = Uri.parse(savedImageURL);

        ArrayList<String> imgs = training.getImages();
        imgs.add(getPathFile(savedImageURI));
        training.setImages(imgs);

        //Store URI's path and name in an arraylist :
        //Path.add(getPathFile(savedImageURI));
        //Path.add(name);
        return savedImageURI;
    }

    //This method allow us to get the file's path from the URI (this method is useful for picGallerySaver) :
    public String getPathFile(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void takePhoto(){
        //Camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent, 1);

        }catch (ActivityNotFoundException e) {
            Log.d("photo", e.toString());
        }
    }

}









