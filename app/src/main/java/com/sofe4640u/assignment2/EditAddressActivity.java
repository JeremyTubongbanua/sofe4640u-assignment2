package com.sofe4640u.assignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EditAddressActivity extends AppCompatActivity {

    private LocationDatabase db;
    private EditText editTextAddress, editTextLatitude, editTextLongitude;
    private String originalAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_address);

        db = new LocationDatabase(this);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);

        originalAddress = getIntent().getStringExtra("address");
        String[] location = db.getLocationByAddress(originalAddress);

        if (location != null) {
            editTextAddress.setText(originalAddress);
            editTextLatitude.setText(location[0]);
            editTextLongitude.setText(location[1]);
        }

        Button updateBtn = findViewById(R.id.addNewAddressBtn);
        Button deleteBtn = findViewById(R.id.btnCancel);

        updateBtn.setOnClickListener(view -> {
            String address = editTextAddress.getText().toString();
            double latitude = Double.parseDouble(editTextLatitude.getText().toString());
            double longitude = Double.parseDouble(editTextLongitude.getText().toString());

            if (db.updateLocation(originalAddress, latitude, longitude)) {
                Toast.makeText(this, "Location updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update location", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(view -> {
            if (db.deleteLocation(originalAddress)) {
                Toast.makeText(this, "Location deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete location", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
