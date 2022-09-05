package com.example.systemprototypev2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssAntennaInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class CarParkingMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    boolean isPermissionGranted;
    GoogleMap mgoogleMapSg;
    private FusedLocationProviderClient mLocationClient;
    private int GPS_REQUEST_CODE = 9001;
    EditText locSearch, secondSearch, distance_calculated_carpark, distance_calculated_blueSG;
    ImageView searchIcon;
    Button  cf_map_button, calculatedistance_carpark,calculatedistance_blueSG, route_button;
    TextView location_Button, carpark_button, blueSG_button;
    float distance;
    double initial_latitude, initial_longitude;
    double end_latitude, end_longitude;


    Object dataTransfer[] = new Object[3];
    int PROXIMITY_RADIUS = 400;
    String url;


    //TextView infoOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_parking_map);
        location_Button = findViewById(R.id.location_button);
        locSearch = findViewById(R.id.et_search);
        secondSearch = findViewById(R.id.second_location);
        searchIcon = findViewById(R.id.search_icon);
        cf_map_button = findViewById(R.id.cf_map_button);
        carpark_button = findViewById(R.id.carpark_location_button);
        blueSG_button = findViewById(R.id.blueSG_location_button);
        calculatedistance_carpark = findViewById(R.id.calculate_distance_button);
        calculatedistance_blueSG = findViewById(R.id.calculate_distance_button2);
        distance_calculated_carpark = findViewById(R.id.calculatedistance_edittext);
        distance_calculated_blueSG = findViewById(R.id.calculatedistance_edittext2);
        route_button = findViewById(R.id.route_destination_button);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar


        checkMyPermission();

        initMap();

        mLocationClient = new FusedLocationProviderClient(this);

        searchIcon.setOnClickListener(this::geoLocate);

        calculatedistance_carpark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float results[] = new float[10];
                Location.distanceBetween(initial_latitude,initial_longitude,end_latitude,end_longitude, results);
                distance = results[0];
                int dist = (int)distance;
                Log.d("logcat", String.valueOf(distance));
                distance_calculated_carpark.setText(String.valueOf(dist) +" m");
            }
        });

        calculatedistance_blueSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float results[] = new float[10];
                Location.distanceBetween(initial_latitude,initial_longitude,end_latitude,end_longitude, results);
                distance = results[0];
                int dist = (int)distance;
                Log.d("logcat", String.valueOf(distance));
                distance_calculated_blueSG.setText(String.valueOf(dist) +" m");
            }
        });
    }


    //Providing the URL for GetDirectionsData class
    private String getDirectionsUrl() {
        StringBuilder googleDirectionUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionUrl.append("origin=" + initial_latitude + ","+ initial_longitude);
        googleDirectionUrl.append("&destination=" + end_latitude+ "," + end_longitude);
        googleDirectionUrl.append("&key=" + "AIzaSyDdN7b-A82hHb7xunro4liW0ZX70V9XEnA");

        return googleDirectionUrl.toString();
    }

    //Providing the URL for GetNearbyPlacesData class
    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googlePlaceURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceURL.append("location="+latitude+","+longitude);
        //Log.d("cadlog", String.valueOf(latitude));
        googlePlaceURL.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceURL.append("&type="+nearbyPlace);
        googlePlaceURL.append("&keyword="+nearbyPlace);
        googlePlaceURL.append("&key="+"AIzaSyDdN7b-A82hHb7xunro4liW0ZX70V9XEnA");

        Log.d("cadlog", String.valueOf(googlePlaceURL));

        return googlePlaceURL.toString();
    }

    public void onClick(View v)
    {
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        switch(v.getId())
        {
            case R.id.location_button: {
                getCurrentLocation();
                // String latitude = String.valueOf(markerLatLng.latitude);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> blueSG_address;
                break;
            }
            case R.id.blueSG_location_button: {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationClient.getLastLocation().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        Object dataTransfer[] = new Object[2];
                        mgoogleMapSg.clear();
                        String blueSG = "BlueSG";
                        url = getUrl(location.getLatitude(), location.getLongitude(), blueSG);
                        dataTransfer[0] = mgoogleMapSg;
                        dataTransfer[1] = url;

                        //Log.d("Catlog","Im here");
                        getNearbyPlacesData.execute(dataTransfer);
                        //Log.d("Catlog","Im here");
                        //getCurrentLocation();
                        Toast.makeText(getApplicationContext(), "Showing nearby BlueSG stations", Toast.LENGTH_LONG).show();

                        mgoogleMapSg.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                LatLng markerLatLng = marker.getPosition();
                                Location markerLocation = new Location("");
                                markerLocation.setLatitude(markerLatLng.latitude);
                                markerLocation.setLongitude(markerLatLng.longitude);
                               // String latitude = String.valueOf(markerLatLng.latitude);

                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> blueSG_address;

                                try {
                                    blueSG_address = geocoder.getFromLocation(markerLatLng.latitude, markerLatLng.longitude, 1);
                                    String blueSG = blueSG_address.get(0).getAddressLine(0);

                                    if(blueSG != null){
                                            locSearch.setText(blueSG);
                                            initial_latitude = markerLatLng.latitude;
                                            initial_longitude = markerLatLng.longitude;
                                        }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                    }

                });
                break;
            }

            case R.id.carpark_location_button: {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        Object dataTransfer[] = new Object[2];
                        //mgoogleMapSg.clear();

                        String carpark = "Car park";
                        url = getUrl(location.getLatitude(), location.getLongitude(), carpark);
                        dataTransfer[0] = mgoogleMapSg;
                        dataTransfer[1] = url;
                        //mgoogleMapSg.clear();

                        getNearbyPlacesData.execute(dataTransfer);
                        Toast.makeText(getApplicationContext(), "Showing nearby carparks", Toast.LENGTH_LONG).show();
                        //getCurrentLocation();

                        mgoogleMapSg.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                LatLng markerLatLng = marker.getPosition();
                                Location markerLocation = new Location("");
                                markerLocation.setLatitude(markerLatLng.latitude);
                                markerLocation.setLongitude(markerLatLng.longitude);
                                // String latitude = String.valueOf(markerLatLng.latitude);
                                //mgoogleMapSg.clear();

                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> carpark_address;

                                try {
                                    carpark_address = geocoder.getFromLocation(markerLatLng.latitude, markerLatLng.longitude, 1);
                                    String carpark = carpark_address.get(0).getAddressLine(0);

                                    if(carpark != null) {
                                        secondSearch.setText(carpark);
                                        end_latitude = markerLatLng.latitude;
                                        end_longitude = markerLatLng.longitude;
                                        distance_calculated_carpark.setText(null);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                        //.d("cadlog", String.valueOf(location.getLatitude()));
                    }
                });
                break;
            }
            case R.id.route_destination_button:{
                Object dataTransfer[] = new Object[3];
                url = getDirectionsUrl();
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                dataTransfer[0]=  mgoogleMapSg;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(end_latitude, end_longitude);

                getDirectionsData.execute(dataTransfer);
            }
        }
    }

    private void geoLocate(View view) {

        String locationName = locSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName,1);
            String addresses = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addressList.get(0).getLocality();
            String state = addressList.get(0).getAdminArea();
            String country = addressList.get(0).getCountryName();
            String postalCode = addressList.get(0).getPostalCode();
            String knownName = addressList.get(0).getFeatureName(); // Only if available else return NULL
            /**Log.d("mylog","Complete Address:" + addressList.toString());
            Log.d("mylog","Address:"+addresses); */

            if(addressList.size()>0){
                Address address = addressList.get(0);

                gotoLocation(address.getLatitude(), address.getLongitude());

                mgoogleMapSg.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));

                Toast.makeText(this, address.getLocality(),Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
        }
    }

    private void initMap() {
        if(isPermissionGranted){
            if(isGPSenable()){
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view_sg_fragment);
                supportMapFragment.getMapAsync(this::onMapReady);
            }
        }
    }

    private boolean isGPSenable(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(providerEnable){
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("GPS is required for this app to work. Please enable GPS")
                    .setPositiveButton("Yes",((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent,GPS_REQUEST_CODE);
            }))
                    .setCancelable(false)
                    .show();
        }

        return false;

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());

                //Log.d("cadlog",String.valueOf(location.getLatitude()));
            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude,longitude);

        List<Address> address;

        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng,16);
        mgoogleMapSg.clear();
        mgoogleMapSg.moveCamera(cameraUpdate);
        mgoogleMapSg.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mgoogleMapSg.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            address = geocoder.getFromLocation(latitude,longitude,1);
            String addresses = address.get(0).getAddressLine(0);
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = address.get(0).getLocality();
            String state = address.get(0).getAdminArea();
            String country = address.get(0).getCountryName();
            String postalCode = address.get(0).getPostalCode();
            String knownName = address.get(0).getFeatureName(); // Only if available else return NULL

            //Log.d("mylog","Address:"+postalCode);
            String id = getIntent().getStringExtra("keyID");
            String fullname = getIntent().getStringExtra("keyfullname");
            String username = getIntent().getStringExtra("keyusername");
            String email = getIntent().getStringExtra("keyemail");
            String password = getIntent().getStringExtra("keypassword");
            String mobile_no = getIntent().getStringExtra("keymobilenumber");
            String driving_license = getIntent().getStringExtra("keydrivinglicense");
            String profile_picture = getIntent().getStringExtra("keyprofilepicture");

            Log.d("mylog", mobile_no);



            cf_map_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] dist_carpark = distance_calculated_carpark.getText().toString().split(" ");
                    String[] dist_blueSG = distance_calculated_blueSG.getText().toString().split(" ");
                    Log.d("mylog", dist_blueSG[0]);
                   // int distance_carpark = Integer.parseInt(dist_carpark[0]);
                    //int distance_blueSG = Integer.parseInt(dist_blueSG[0]);



                    Intent transferData = new Intent(getApplicationContext(),InformationCheck.class);
                    transferData.putExtra("keyID",id);
                    transferData.putExtra("keyfullname", fullname);
                    transferData.putExtra("keyusername", username);
                    transferData.putExtra("keyemail", email);
                    transferData.putExtra("keypassword", password);
                    transferData.putExtra("keymobilenumber", mobile_no);
                    transferData.putExtra("keydrivinglicense", driving_license);
                    transferData.putExtra("keyprofilepicture", profile_picture);
                    transferData.putExtra("keycity",city);
                    transferData.putExtra("keyaddress",addresses);
                    transferData.putExtra("keycountry",country);
                    transferData.putExtra("keyzipcode",postalCode);
                    transferData.putExtra("keyusername",username);

                    if(Integer.parseInt(dist_blueSG[0])<400 && dist_blueSG != null){
                        if(dist_carpark == null || dist_carpark[0]==""){
                            Toast.makeText(getApplicationContext(), "Please check current distance from carpark!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if ( Integer.valueOf(dist_carpark[0]) < 20) {
                                Toast.makeText(getApplicationContext(), "Entry Successful!", Toast.LENGTH_LONG).show();
                                startActivity(transferData);
                            }
                            if(locSearch.getText() == secondSearch.getText()){
                                startActivity(transferData);
                            }
                            if( Integer.valueOf(dist_carpark[0])>20){
                                Toast.makeText(getApplicationContext(), "Too far away from carpark location!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if(Integer.parseInt(dist_blueSG[0])>400){
                        Toast.makeText(getApplicationContext(), "Too far away from blueSG station!", Toast.LENGTH_LONG).show();
                    }

                    //Log.d("mylog","Address:"+ city);
                }
            });

            initial_latitude = latLng.latitude;
            initial_longitude = latLng.longitude;
            locSearch.setText(addresses); //keys address name into TextEdit box provided in google map
            Log.d("mylog","Address:"+addresses);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkMyPermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(getApplicationContext(),"Permission Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();

        }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mgoogleMapSg = googleMap;
        mgoogleMapSg.setMyLocationEnabled(true);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) { }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GPS_REQUEST_CODE){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(providerEnable){
                Toast.makeText(this,"GPS is enable", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"GPS is not enable", Toast.LENGTH_SHORT).show();
            }
        }
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
    /**@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/
}