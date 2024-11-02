package com.sofe4640u.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditAddressesActivity extends AppCompatActivity {

    private LocationDatabase db;

    private Button backBtn, removeButton, editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_addresses);

        db = new LocationDatabase(this);
        LinearLayout addressListLayout = findViewById(R.id.linearLayoutAddressList);
        Button addNewAddressBtn = findViewById(R.id.addNewAddressBtn);

        loadAddressList(addressListLayout);

        addNewAddressBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EditAddressesActivity.this, AddNewAddressActivity.class);
            startActivity(intent);
        });

        backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EditAddressesActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void loadAddressList(LinearLayout layout) {
        layout.removeAllViews();
        Cursor cursor = db.getAllLocations();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));

                View itemView = inflater.inflate(R.layout.address_list_item, layout, false);

                TextView addressTextView = itemView.findViewById(R.id.textViewAddress);
                TextView detailsTextView = itemView.findViewById(R.id.textViewDetails);
                Button removeButton = itemView.findViewById(R.id.removeBtn);

                addressTextView.setText(address);
                detailsTextView.setText(String.format("Longitude: %.4f, Latitude: %.4f", longitude, latitude));

                itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(EditAddressesActivity.this, EditAddressActivity.class);
                    intent.putExtra("address", address);
                    startActivity(intent);
                });

                removeButton.setOnClickListener(view -> {
                    if (db.deleteLocation(address)) {
                        Toast.makeText(this, "Address removed", Toast.LENGTH_SHORT).show();
                        loadAddressList(layout);
                    } else {
                        Toast.makeText(this, "Failed to remove address", Toast.LENGTH_SHORT).show();
                    }
                });

                Button editButton = itemView.findViewById(R.id.editBtn);
                editButton.setOnClickListener(view -> {
                    Intent intent = new Intent(EditAddressesActivity.this, EditAddressActivity.class);
                    intent.putExtra("address", address);
                    startActivity(intent);
                });

                layout.addView(itemView);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No addresses found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout addressListLayout = findViewById(R.id.linearLayoutAddressList);
        loadAddressList(addressListLayout);
    }
}
