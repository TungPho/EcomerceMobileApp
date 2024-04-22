package com.example.ecomerceapp1.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecomerceapp1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLocation;
    SearchView mapSearch;
    FusedLocationProviderClient fusedLocationProviderClient;

    Button confirmButton;

    String addressString = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_map);
        mapSearch = findViewById(R.id.map_search);
        confirmButton = findViewById(R.id.confirm_location_button);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearch.getQuery().toString();
                List<Address> addressesList = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(GoogleMapActivity.this);
                    try {
                        addressesList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressesList.get(0);
                    addressString =  address.getSubAdminArea() + "," + address.getAdminArea() + "," + address.getFeatureName() + "," + address.getCountryName();
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    myMap.addMarker(new MarkerOptions().position(latLng).title("location"));
                    myMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(GoogleMapActivity.this);

        //confirm the address on Google map
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressString == null){
                    Toast.makeText(GoogleMapActivity.this, "Please select a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(GoogleMapActivity.this, addressString, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GoogleMapActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("addressGoogle", addressString);
                startActivity(intent);
            }
        });

    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(GoogleMapActivity.this, "" + currentLocation, Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(GoogleMapActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
//        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng myLocation = new LatLng(-131, 25);

        Toast.makeText(this, "" + currentLocation, Toast.LENGTH_SHORT).show();
        myMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, Please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}