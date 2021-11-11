package fr.android.bottomnav.ui.enregistrer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.android.bottomnav.R;
import fr.android.bottomnav.User;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private LocationManager locationManager;
    private String provider;
    private boolean geoLocPermit = false;
    private boolean geoLocRequest = false;
    private final int PERMISSION_REQUEST_LOC = 0;
    private final int GPS_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private ImageButton imgBtn;
    private FirebaseFirestore db;

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

        // PHOTO
        imgBtn = (ImageButton) view.findViewById(R.id.imageButtonPhoto);
        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takePhoto();
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

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                String address = "";

                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String a = address;

                // CODE TO INSERT A NEW TRAINING
                // Create a Map to store the data we want to set
                Map<String, Object> docData = new HashMap<>();
                docData.put("date", "21/10/2021");
                docData.put("distance", "15km");
                docData.put("duration", "1h25");
                docData.put("address_start", a);
                docData.put("address_end", a);
                docData.put("path", Arrays.asList("48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268","48.85/2.268"));
                docData.put("rythm",Arrays.asList(5,6,7.30,8,4,5,5,5,5,5.3,5.2));

                // generate the document id based on the user id and the current timestamp
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String ts = User.uid + "_" + String.valueOf(timestamp.getTime());

                // Add a new document (asynchronously) in collection "cities"
                db.collection("trainings")
                        .document(ts)
                        .set(docData);

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

    /*
    @Override
    public void onPause() {
        super.onPause();

    if (fusedLocationClient != null)
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

     */


    /*
    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        LatLng coord = new LatLng(lat, lng);

        Log.d("LOCALISATION", "test");


        // add a marker on the map && zoom in
        gMap.addMarker(new MarkerOptions().position(coord));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Store picture in picture field :
        Bitmap image = (Bitmap) data.getExtras().get("data");
        //Put picture on the imageView item :
        String filename = "bpetiot@gmail.com_16234567898765432345";
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
        //Store URI's path and name in an arraylist :
        //Path.add(getPathFile(savedImageURI));
        //Path.add(name);
        return savedImageURI;
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









