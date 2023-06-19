package com.pamgroup.restaurantlistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditRestaurant extends AppCompatActivity {

    private EditText etName, etAddress, etBusinessHour, etDescription;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        etName = findViewById(R.id.etNamaRestoran);
        etAddress = findViewById(R.id.etAlamat);
        etBusinessHour = findViewById(R.id.etJamBukaTutup);
        etDescription = findViewById(R.id.etDeskripsi);
        btnSave = findViewById(R.id.btnCreate);

        Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            String name = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");

            etName.setText(name);
            etAddress.setText(address);
            etBusinessHour.setText(businessHour);
            etDescription.setText(description);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the updated restaurant details
                String updatedName = etName.getText().toString();
                String updatedAddress = etAddress.getText().toString();
                String updatedBusinessHour = etBusinessHour.getText().toString();
                String updatedDescription = etDescription.getText().toString();

                Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
                if (restaurantBundle != null) {
                    String restaurantId = restaurantBundle.getString("restaurantId");

                    // Get a reference to the restaurant node in the database
                    DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurantId);

                    // Update the restaurant details
                    restaurantRef.child("name").setValue(updatedName);
                    restaurantRef.child("address").setValue(updatedAddress);
                    restaurantRef.child("businessHour").setValue(updatedBusinessHour);
                    restaurantRef.child("description").setValue(updatedDescription);

                    // Finish the activity and return to the previous screen
                    finish();
                }
            }
        });
    }
}