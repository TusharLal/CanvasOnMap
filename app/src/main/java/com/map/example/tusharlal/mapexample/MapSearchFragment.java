package com.map.example.tusharlal.mapexample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapSearchFragment extends Fragment implements OnMapReadyCallback {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE = 3; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME = (long) (1_000 * 60); // 1 minute

    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapsearch, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onStart() {
        Log.i("Logsssss", "MapSearchFragment: onStart()");
        super.onStart();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        Log.i("Logsssss", "MapSearchFragment: onResume()");
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        Log.i("Logsssss", "MapSearchFragment: onPause()");
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onStop() {
        Log.i("Logsssss", "MapSearchFragment: onStop()");
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());
        this.googleMap = googleMap;
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    this.googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                    this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
                    addCircleToMap(latLng, googleMap);
//                    drawMarkerWithCircle(latLng, googleMap);
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private void addCircleToMap(LatLng latLng, GoogleMap mGoogleMap) {
        float radius20MilesInMeters = 32186.9f;
        // circle settings
        int radiusM = 500;// your radius in meters
        // draw circle
        int d = 1000; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);

        Paint blue = new Paint();
        blue.setColor(0x550000ff);
        blue.setStyle(Paint.Style.FILL);
        blue.setAntiAlias(true);
        Path largePath = new Path();
        largePath.addCircle(d / 2, d / 2, radiusM, Path.Direction.CW);
        Path smallPath = new Path();
        smallPath.addCircle(d / 2, d / 2, (radiusM * 3) / 4, Path.Direction.CW);

        c.clipPath(smallPath, Region.Op.DIFFERENCE);
        c.drawPath(largePath, blue);

        // generate BitmapDescriptor from circle Bitmap
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

        googleMap.addGroundOverlay(new GroundOverlayOptions().
                image(bmD).
                position(latLng, radius20MilesInMeters, radius20MilesInMeters));
    }

    private void drawMarkerWithCircle(LatLng position, GoogleMap googleMap) {
        double radius20MilesInMeters = 32186.9;
        double radius15MilesInMeters = 24140.2;
        int shadeColor = 0x220000ff; //opaque transparent fill

        CircleOptions outerCircleOptions = new CircleOptions().center(position).radius(radius20MilesInMeters).strokeColor(0x00000000).fillColor(shadeColor);
        CircleOptions innerCircleOptions = new CircleOptions().center(position).radius(radius15MilesInMeters).strokeColor(0x00000000).fillColor(shadeColor);

        googleMap.addCircle(outerCircleOptions);
        googleMap.addCircle(innerCircleOptions);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            // Nothing to do
        }

        @Override
        public void onProviderEnabled(String s) {
            // Nothing to do
        }

        @Override
        public void onProviderDisabled(String s) {
            // Nothing to do
        }
    };

    private double meterToPixels(float distanceInMeter, GoogleMap mGoogleMap) {
        double metersToPixels = (Math.cos(mGoogleMap.getCameraPosition().target.latitude * Math.PI / 180) * 2 * Math.PI * 6378137) / (256 * Math.pow(2, mGoogleMap.getCameraPosition().zoom));
        return metersToPixels * distanceInMeter;
    }
}
