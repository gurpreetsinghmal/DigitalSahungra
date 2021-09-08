package com.sahungra.digitalsahungra;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.sahungra.digitalsahungra.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//import android.location.Location;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    TextView status;
    Handler h;
    ImageView locator;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE=99;
    int PROXIMITY_RADIUS=10000;
    double latitude=0.0,longitude=0.0;
    double end_latitude=31.1770324,end_longitude=76.2013626;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        status=findViewById(R.id.status);
        locator=findViewById(R.id.locator);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                status.setText("Showing Near by Hospitals");
                status.setTextColor(getResources().getColor(R.color.color2));
                locator.getDrawable().setTint(getResources().getColor(R.color.color2));
            }
        },3000);



    }

    public void loadMap()
    {   if(latitude==0.0||longitude==0.0)
        {
        latitude = end_latitude;
        longitude = end_longitude;
        }
        String Hospital="hospital";
        String url=getUrl(latitude,longitude,Hospital);
        Object dataTransfer[]=new Object[2];
        dataTransfer[0]=mMap;
        dataTransfer[1]=url;
        GetNearbyPlacesData getNearbyPlacesData=new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
       /* Toast.makeText(this, "Showing Hospitals", Toast.LENGTH_SHORT).show();
        dataTransfer=new Object[3];
        url=getDirectionsUrl();
        GetDirectionsData getDirectionsData=new GetDirectionsData();
        dataTransfer[0]=mMap;
        dataTransfer[1]=url;
        dataTransfer[2]=new LatLng(end_latitude,end_latitude);
       getDirectionsData.execute(dataTransfer);//*/

    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl=new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+ BloggerApi.key);

        return googleDirectionsUrl.toString();
    }
    private String getUrl(double latitude,double longitude,String nearbyplace )
    {
        StringBuilder googlePlaceurl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceurl.append("location="+latitude+","+longitude);
        googlePlaceurl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceurl.append("&type="+nearbyplace);
        googlePlaceurl.append("&sensor=true");
        googlePlaceurl.append("&key="+ BloggerApi.key);

        return googlePlaceurl.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if(client==null)
                        {
                            buildGoogleApiCLient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiCLient();
            mMap.setMyLocationEnabled(true);
        }
        loadMap();

    }

    protected synchronized void buildGoogleApiCLient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();

    }


    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Toast.makeText(this, latitude+","+longitude, Toast.LENGTH_SHORT).show();

        markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null) {
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
        loadMap();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);

            }
        }
    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            return false;
        }
        else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
