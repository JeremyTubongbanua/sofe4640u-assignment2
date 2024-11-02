package com.sofe4640u.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationDatabase db;
    private AutoCompleteTextView editTextAddress;
    private TextView textViewLatitude, textViewLongitude;
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new LocationDatabase(this);
        editTextAddress = findViewById(R.id.editTextAddress);
        textViewLatitude = findViewById(R.id.textViewLatitude);
        textViewLongitude = findViewById(R.id.textViewLongitude);
        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        setupAutoComplete();

        Button buttonQuery = findViewById(R.id.queryBtn);
        Button buttonClear = findViewById(R.id.clearBtn);
        Button buttonEditAddresses = findViewById(R.id.buttonEditAddresses);

        buttonQuery.setOnClickListener(view -> {
            String address = editTextAddress.getText().toString();
            String[] location = db.getLocationByAddress(address);

            if (location != null) {
                String latitudeStr = location[0];
                String longitudeStr = location[1];
                textViewLatitude.setText("Latitude: " + latitudeStr);
                textViewLongitude.setText("Longitude: " + longitudeStr);

                double latitude = Double.parseDouble(latitudeStr);
                double longitude = Double.parseDouble(longitudeStr);

                // Display the location on the map
                displayLocationOnMap(latitude, longitude, address);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                clearQuery();
            }
        });

        buttonClear.setOnClickListener(view -> clearQuery());

        buttonEditAddresses.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditAddressesActivity.class);
            startActivity(intent);
        });
    }

    private void displayLocationOnMap(double latitude, double longitude, String address) {
        if (googleMap != null) {
            LatLng location = new LatLng(latitude, longitude);
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(location).title(address));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }

    private void clearQuery() {
        editTextAddress.setText("");
        textViewLatitude.setText("Latitude:");
        textViewLongitude.setText("Longitude:");
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    private void setupAutoComplete() {
        ArrayList<String> addressList = new ArrayList<>();
        Cursor cursor = db.getAllLocations();
        if (cursor.moveToFirst()) {
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                addressList.add(address);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, addressList);
        editTextAddress.setAdapter(adapter);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true); // Enable zoom controls
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
