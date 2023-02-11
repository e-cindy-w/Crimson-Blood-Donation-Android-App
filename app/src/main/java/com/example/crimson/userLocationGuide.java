package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class userLocationGuide extends AppCompatActivity implements OnMapReadyCallback{
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean isPermissionGranter;
    Button getDirections;
    String bankLocation, bankLatitude, bankLongitude;
    GoogleMap googleMap;
    Double doublebankLat, doubleBankLongt;
    DatabaseReference bloodBankDBRef;
    ArrayList<LatLng> locationArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_guide);
        getDirections = findViewById(R.id.getDirections);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        checkMapPermission();
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(userLocationGuide.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(userLocationGuide.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        Double lat = location.getLatitude();
                                        Double longt = location.getLongitude();
                                        bloodBankDBRef = FirebaseDatabase.getInstance().getReference("Blood Banks");
                                        bloodBankDBRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot bloodBankSnap : snapshot.getChildren()) {
                                                    bankLocation = bloodBankSnap.child("location").getValue().toString();
                                                    String bankLocation3 = bankLocation.substring(10);
                                                    String bankLocation4 = bankLocation3.substring(0, bankLocation3.length()-1);
                                                    String[] bankLocation2 = bankLocation4.split(",");
                                                    bankLatitude = bankLocation2[0];
                                                    bankLongitude = bankLocation2[1];
                                                    doublebankLat = Double.parseDouble(bankLatitude);
                                                    doubleBankLongt = Double.parseDouble(bankLongitude);
                                                }
                                                com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(lat, longt);
                                                com.google.android.gms.maps.model.LatLng latLng2 = new com.google.android.gms.maps.model.LatLng(doublebankLat, doubleBankLongt);
                                                locationArrayList = new ArrayList<com.google.android.gms.maps.model.LatLng>();
                                                locationArrayList.add(latLng);
                                                locationArrayList.add(latLng2);
                                                for(int k = 0; k<locationArrayList.size(); k++){
                                                    googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(0)).title("Donor"));
                                                    googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(1)).title("Blood bank"));
                                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationArrayList.get(k), 15);
                                                    googleMap.animateCamera(cameraUpdate);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                }

            }

        });

    }

        private void checkMapPermission() {
            Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    isPermissionGranter = true;
                    Toast.makeText(userLocationGuide.this, "Permission granted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), "");
                    intent.setData(uri);
                    startActivity(intent);
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("My location");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
        googleMap.animateCamera(cameraUpdate);
    }
}