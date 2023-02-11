package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class findLocation extends AppCompatActivity implements OnMapReadyCallback {
    boolean isPermissionGranter;
    GoogleMap googleMap;
    ImageView imageViewSearch;
    EditText inputLocation;
    String location, bankName, phoneNo, selectedLocation;
    Button getLoc;
    DatabaseReference bloodBankDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        imageViewSearch = findViewById(R.id.imageViewSearch);
        inputLocation = findViewById(R.id.inputLocation);
        getLoc = findViewById(R.id.button11);
        bloodBankDB = FirebaseDatabase.getInstance().getReference("Blood Banks");

        checkMapPermission();
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = inputLocation.getText().toString().trim();

                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(findLocation.this, "Please enter location", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(findLocation.this, Locale.getDefault());
                    try {
                        List<Address> listAddress = geocoder.getFromLocationName(location, 1);
                        if (listAddress.size() > 0) {
                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title("My location");
                            markerOptions.position(latLng);
                            googleMap.addMarker(markerOptions);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
                            googleMap.animateCamera(cameraUpdate);
                            selectedLocation = latLng.toString().trim();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        getLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    bankName = bundle.getString("key1");
                    phoneNo = bundle.getString("key2");
                    System.out.println("Blood bank user: " + bankName + phoneNo + selectedLocation);
                }
                insertBloodBank();
            }
        });
    }

    private void insertBloodBank() {
        bloodBankUser bloodBankUser = new bloodBankUser(bankName, phoneNo, selectedLocation);
        bloodBankDB.push().setValue(bloodBankUser);
        Toast.makeText(findLocation.this, "Profile complete", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(findLocation.this, BloodBankMain.class));
    }

    private void checkMapPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranter = true;
                Toast.makeText(findLocation.this, "Permission granted", Toast.LENGTH_SHORT).show();
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
        LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("My location");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
        googleMap.animateCamera(cameraUpdate);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }
}