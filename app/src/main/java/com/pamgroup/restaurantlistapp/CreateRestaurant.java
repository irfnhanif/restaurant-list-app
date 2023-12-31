package com.pamgroup.restaurantlistapp;

import static java.io.File.createTempFile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.pamgroup.restaurantlistapp.helper.ImageStorage;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;
import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;

public class CreateRestaurant extends AppCompatActivity implements View.OnClickListener {

    private ImageStorage imageStorage;
    private EditText etName, etAddress, etBusinessHour, etDescription, etLongitude, etLatitude;
    private Button btnCreate, btnHapus;
    private ImageView btnBack,  btnChooseImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        imageStorage = new ImageStorage();

        etName = findViewById(R.id.etNamaRestoran);
        etAddress = findViewById(R.id.etAlamat);
        etBusinessHour = findViewById(R.id.etDeskripsi);
        etDescription = findViewById(R.id.etJamBukaTutup);
        btnBack = findViewById(R.id.btn_back);
        btnHapus = findViewById(R.id.btn_hapus);
        btnCreate = findViewById(R.id.btnCreate);
        btnChooseImage = findViewById(R.id.upload_image);

        etLongitude = findViewById(R.id.etLongitude);
        etLatitude = findViewById(R.id.etLatitude);

        btnCreate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHapus.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                String name = etName.getText().toString();
                String address = etAddress.getText().toString();
                String businessHour = etBusinessHour.getText().toString();
                String description = etDescription.getText().toString();
                String longitude = etLongitude.getText().toString();
                String latitude = etLatitude.getText().toString();

                if (!validateForm(name, address, businessHour, description, longitude, latitude))
                    return;

                Thread thread = new Thread(() -> {
                    String imageURL = imageStorage.getImageURL();
                    RestaurantDatabase database = new RestaurantDatabase();
                    database.addRestaurant(name, address, businessHour, description, imageURL, longitude, latitude);
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
                etLongitude.setText("");
                etLatitude.setText("");
                break;
        }
    }

    private boolean validateForm(String name, String address, String businessHour, String description, String longitude, String latitude) {
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

//        if (TextUtils.isEmpty(longitude)) {
//            etBusinessHour.setError("Masukkan Longitude");
//            return false;
//        }
//
//        if (TextUtils.isEmpty(latitude)) {
//            etBusinessHour.setError("Masukkan Latitude");
//            return false;
//        }

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
            Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show();
            runOnUiThread(() -> btnChooseImage.setImageURI(selectedImageUri));
        }
    }
}

