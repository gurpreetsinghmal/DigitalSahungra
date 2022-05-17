package com.technominds.digitalsahungrav2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.technominds.digitalsahungrav2.DirectionHelpers.TaskLoadedCallback;

public class ReachLocation extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private Polyline currentply;
    private GoogleMap mMap;
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 10;
    Button getdir;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reach_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        GetlastLocation();

        getdir = findViewById(R.id.getdir);

    }

    private void GetlastLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, REQUEST_CODE);
                return;
            }
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mlocation = location;
                    //Toast.makeText(getApplicationContext(), "loc:"+mlocation, Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.testmap);
                    supportMapFragment.getMapAsync(ReachLocation.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        final LatLng origin = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());
        final LatLng dest = new LatLng(31.1774, 76.2037);
        MarkerOptions markerOptions1 = new MarkerOptions().position(origin)
                .title("Your are here now");

        googleMap.addMarker(markerOptions1);
        MarkerOptions markerOptions2 = new MarkerOptions().position(dest).title("Sahungra");
        googleMap.addMarker(markerOptions2);
        builder.include(markerOptions1.getPosition());
        builder.include(markerOptions2.getPosition());
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);

        getdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "http://maps.google.com/maps?q=loc:31.1774,76.2037 (Sahungra)";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
               // new FetchURL(ReachLocation.this).execute(getURL(origin, dest, "driving"), "driving");


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetlastLocation();
                }
                break;
        }
    }


    private String getURL(LatLng origin, LatLng destination, String dirmode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=" + dirmode;
        String parameter = str_origin + "&" + str_dest + "&" + mode;
        String format = "json";
       // String url = "https://maps.googleapis.com/maps/api/directions/" + format + "?" + parameter + "&key=" + BloggerApi.direction_key;
      //  Log.d("urlx", url);
        return "";
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentply != null)
            currentply.remove();

        currentply = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
