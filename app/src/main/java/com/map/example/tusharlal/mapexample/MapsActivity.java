package com.map.example.tusharlal.mapexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;

    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        requestPermission();
        launchMapSearchFragment();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

    void launchMapSearchFragment() {
        MapSearchFragment mapsearchFragment = new MapSearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.map, mapsearchFragment, mapsearchFragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void requestPermission() {
        final String[][] permissions = new String[][]{{Manifest.permission.INTERNET, "Required to run GPS"}, {Manifest.permission.ACCESS_FINE_LOCATION, "Required to get location"}, {Manifest.permission.ACCESS_COARSE_LOCATION, "Alternative to get location"}};
        List<String> required = new ArrayList<>();
        for (String[] permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[0])) {
//                Toast.makeText(this, permission[1], Toast.LENGTH_SHORT).show()
            }
            required.add(permission[0]);
        }
        if (!required.isEmpty()) {
            ActivityCompat.requestPermissions(this, required.toArray(new String[0]), MY_PERMISSIONS_REQUEST);
        }
    }
}
