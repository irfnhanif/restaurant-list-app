package com.pamgroup.restaurantlistapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pamgroup.restaurantlistapp.helper.ImageStorage;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;
import com.pamgroup.restaurantlistapp.model.Restaurant;

public class CreateRestaurant extends AppCompatActivity implements View.OnClickListener {

    private ImageStorage imageStorage;
    private EditText etName, etAddress, etBusinessHour, etDescription;
    private Button btnCreate, btnHapus, btnChooseImage;
    private ImageView btnBack;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        imageStorage = new ImageStorage();

        etName = findViewById(R.id.etNamaRestoran);
        etAddress = findViewById(R.id.etAlamat);
        etBusinessHour = findViewById(R.id.etJamBukaTutup);
        etDescription = findViewById(R.id.etDeskripsi);
        btnBack = findViewById(R.id.btn_back);
        btnHapus = findViewById(R.id.btn_hapus);
        btnCreate = findViewById(R.id.btnCreate);
        btnChooseImage = findViewById(R.id.btn_choose_image);

        btnCreate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHapus.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageURI = result.getData().getData();
                        Toast.makeText(this, imageURI.toString(), Toast.LENGTH_SHORT).show();
                        if (imageURI != null) {
                            imageStorage.uploadImage(imageURI.toString());
                        }
                    }
                });
    }

    @Override
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
                    RestaurantDatabase database = new RestaurantDatabase();
                    database.addRestaurant(name, address, businessHour, description);
                });

                thread.start();
                finish();
                break;

            case R.id.btn_choose_image:
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

    private void selectImage() {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        imagePickerLauncher.launch(imageIntent);
        Toast.makeText(this, "tes button", Toast.LENGTH_SHORT).show();
    }
}

