package com.pamgroup.restaurantlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pamgroup.restaurantlistapp.databinding.ActivityCreateRestaurantBinding;
import com.pamgroup.restaurantlistapp.model.Restaurant;

public class CreateRestaurant extends AppCompatActivity implements View.OnClickListener {

    private ActivityCreateRestaurantBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Restaurant restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateRestaurantBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_create_restaurant);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        restaurant = new Restaurant();

        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private boolean validateForm(){
        boolean result = true;

        if (TextUtils.isEmpty(binding.etNamaRestoran.getText().toString())){
            binding.etNamaRestoran.setError("Required");
            result = false;
        } else {
            binding.etNamaRestoran.setError(null);
        }

        if (TextUtils.isEmpty(binding.etAlamat.getText().toString())){
            binding.etAlamat.setError("Required");
            result = false;
        } else {
            binding.etAlamat.setError(null);
        }

        if (TextUtils.isEmpty(binding.etDeskripsi.getText().toString())){
            binding.etDeskripsi.setError("Required");
            result = false;
        } else {
            binding.etDeskripsi.setError(null);
        }

        if (TextUtils.isEmpty(binding.etJamBukaTutup.getText().toString())){
            binding.etJamBukaTutup.setError("Required");
            result = false;
        } else {
            binding.etJamBukaTutup.setError(null);
        }

        return result;
    }

    public void submitData(){
        if(!validateForm()){
            return;
        }

        Thread t = new Thread(() -> {

        });
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
    }
}