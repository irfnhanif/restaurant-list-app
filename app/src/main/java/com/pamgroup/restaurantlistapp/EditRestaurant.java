package com.pamgroup.restaurantlistapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pamgroup.restaurantlistapp.helper.ImageStorage;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;

public class EditRestaurant extends AppCompatActivity implements View.OnClickListener {

    private ImageStorage imageStorage;
    private RestaurantDatabase restaurantDatabase;
    private EditText etName, etAddress, etBusinessHour, etDescription;
    private Button btnCreate, btnHapus;
    private ImageView btnBack, btnChooseImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String restaurantId, imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        etName = findViewById(R.id.etNamaRestoran);
        etAddress = findViewById(R.id.etAlamat);
        etBusinessHour = findViewById(R.id.etJamBukaTutup);
        etDescription = findViewById(R.id.etDeskripsi);
        btnBack = findViewById(R.id.btn_back);
        btnHapus = findViewById(R.id.btn_hapus);
        btnCreate = findViewById(R.id.btnCreate);
        btnChooseImage = findViewById(R.id.upload_image);

        imageStorage = new ImageStorage();

        Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            restaurantId = restaurantBundle.getString("restaurantId");
            imageURL = restaurantBundle.getString("imageURL");
            String name = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");

            etName.setText(name);
            etAddress.setText(address);
            etBusinessHour.setText(businessHour);
            etDescription.setText(description);
        }

        btnCreate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHapus.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                String name = etName.getText().toString();
                String address = etAddress.getText().toString();
                String businessHour = etBusinessHour.getText().toString();
                String description = etDescription.getText().toString();

                if (!validateForm(name, address, businessHour, description))
                    return;

                Thread thread = new Thread(() -> {
                    if (imageStorage.getImageURL() != null) {
                        imageURL = imageStorage.getImageURL();
                    }
                    RestaurantDatabase database = new RestaurantDatabase();
                    database.editRestaurant(restaurantId, name, address, businessHour, description, imageURL);
                });
                thread.start();
                finish();
                break;

            case R.id.upload_image:
                selectImage();
                break;

            case R.id.btn_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hapus:
                etName.setText("");
                etAddress.setText("");
                etBusinessHour.setText("");
                etDescription.setText("");
                break;
        }
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

        if (TextUtils.isEmpty(businessHour)) {
            etBusinessHour.setError("Masukkan jam buka-tutup restoran");
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Masukkan deskripsi restoran");
            return false;
        }


        return true;
    }

    private void selectImage() {
        Thread thread = new Thread(() -> {
            Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imageIntent.setType("image/*");
            imageIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(imageIntent, "Select Image"), PICK_IMAGE_REQUEST);
        });
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            Thread thread = new Thread(() -> {
                if (selectedImageUri != null) {
                    imageStorage.uploadImage(selectedImageUri);
                }
            });
            thread.start();
            runOnUiThread(() -> btnChooseImage.setImageURI(selectedImageUri));
        }
    }
}