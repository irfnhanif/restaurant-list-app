package com.pamgroup.restaurantlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;
import com.pamgroup.restaurantlistapp.model.Restaurant;

public class CreateRestaurant extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etAddress, etBusinessHour, etDescription;
    private Button btnSubmit;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        etName = findViewById(R.id.etNamaRestoran);
        etAddress = findViewById(R.id.etAlamat);
        etBusinessHour = findViewById(R.id.etJamBukaTutup);
        etDescription = findViewById(R.id.etDeskripsi);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String businessHour = etBusinessHour.getText().toString();
        String description = etDescription.getText().toString();

        if (!validateForm(name, address, businessHour, description))
            return;

        Thread thread = new Thread(() -> {
            RestaurantDatabase database = new RestaurantDatabase();
            database.addRestaurant(name, address, businessHour, description);
        });

        thread.start();
        finish();
    }

    private boolean validateForm(String name, String address, String businessHour, String description) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("Masukkan nama restoran");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Masukkan alamat restoran");
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Masukkan deskripsi restoran");
            return false;
        }

        if (TextUtils.isEmpty(businessHour)) {
            etBusinessHour.setError("Masukkan jam buka-tutup restoran");
            return false;
        }

        return true;
    }
}
//
//    public void submitData(){
//        if(!validateForm()){
//            return;
//        }
//
//        String NamaRestoran = binding.etNamaRestoran.getText().toString();
//        String Alamat = binding.etAlamat.getText().toString();
//        String Description = binding.etDeskripsi.getText().toString();
//        String BusinessHour = binding.etJamBukaTutup.getText().toString();
//
//        Restaurant baru = new Restaurant(NamaRestoran, Alamat, Description, BusinessHour);
//
//        databaseReference.child("restaurants").push().setValue(baru)
//            .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(CreateRestaurant.this, "Berhasil", Toast.LENGTH_SHORT).show();
//                }
//        }).addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(CreateRestaurant.this, "Gagal", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
