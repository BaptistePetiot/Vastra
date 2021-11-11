package fr.android.bottomnav.ui.enregistrer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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

import fr.android.bottomnav.R;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private LocationManager locationManager;
    private String provider;
    private boolean geoLocPermit = false;
    private boolean geoLocRequest = false;
    private final int PERMISSION_REQUEST_LOC = 0;
    private final int GPS_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private ImageButton imgBtn;

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
        //LatLng sydney = new LatLng(-34, 151);
        //gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        /*
        @SuppressLint("MissingPermission") Location pos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        int lat = (int) (pos.getLatitude());
        int lng = (int) (pos.getLongitude());
        LatLng coord = new LatLng(lat, lng);
        gMap.addMarker(new MarkerOptions().position(coord));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));


         */
    }





/*
    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            // if GPS is not enabled, ask the use to enable it
            if (!locationManager.isProviderEnabled(provider)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_REQUEST_CODE);
            }

            requestLocationUpdates();

        } else
            // ask for permissions; this is asynchronous
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOC);

    }
  */



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

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
                takePhoto(v);
            }
        });



        // locate your position
        // Get the location manager
    //    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
      //  provider = LocationManager.GPS_PROVIDER;

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

                //latitude.setText(String.valueOf(location.getLatitude()));
                //longitude.setText(String.valueOf(location.getLongitude()));
            }
        };

        requestLocationUpdates();
        /*
        // check permissions before registering
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            requestLocationUpdates();
        else {
            String[] perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            // 1- ask for permissions at runtime
            // 2 - enable GPS if needed
            // 3- request location updates
            ActivityCompat.requestPermissions(getActivity(), perms, PERM_REQUEST);
        }

         */

    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
       /*
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location != null) {
                            Log.d("FUSED", "Latest location found");
                            // Logic to handle location object
                            //latitude.setText(String.valueOf(location.getLatitude()));
                            //longitude.setText(String.valueOf(location.getLongitude()));
                        } else
                            Log.d("FUSED", "Last Location unsuccessful");
                    }
                });
*/


        if (fusedLocationClient != null)
          fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());



    }

    /*
    @Override
    public void onResume() {
        super.onResume();

        // check permissions before registering
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            requestLocationUpdates();
        else {
            String[] perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            // 1- ask for permissions at runtime
            // 2 - enable GPS if needed
            // 3- request location updates
            ActivityCompat.requestPermissions(getActivity(), perms, PERM_REQUEST);
        }
    }
    */


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

        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePhoto(getView());
            }else{
                Toast.makeText(getActivity(), "Camera permission required to use camera", Toast.LENGTH_LONG).show();
            }
        }

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

    /*
    *
    *   PHOTO
    *
     */






    public void takePhoto(View view){
        //Check permission

        //Camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent, 1);

        }catch (ActivityNotFoundException e) {
            Log.d("photo", e.toString());
        }
    }

}









