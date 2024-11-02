package com.sofe4640u.assignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewAddressActivity extends AppCompatActivity {

    private LocationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_address);

        db = new LocationDatabase(this);
        EditText editTextAddress = findViewById(R.id.editTextAddress);
        EditText editTextLatitude = findViewById(R.id.editTextLatitude);
        EditText editTextLongitude = findViewById(R.id.editTextLongitude);

        Button addNewAddressBtn = findViewById(R.id.addNewAddressBtn);
        Button cancelBtn = findViewById(R.id.btnCancel);

        addNewAddressBtn.setOnClickListener(view -> {
            String address = editTextAddress.getText().toString();
            double latitude = Double.parseDouble(editTextLatitude.getText().toString());
            double longitude = Double.parseDouble(editTextLongitude.getText().toString());

            if (db.addLocation(address, latitude, longitude)) {
                Toast.makeText(this, "Location added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add location", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(view -> finish());
    }
}
