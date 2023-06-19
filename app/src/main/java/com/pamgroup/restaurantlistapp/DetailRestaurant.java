package com.pamgroup.restaurantlistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class DetailRestaurant extends AppCompatActivity {

    private TextView tvName, tvAddress, tvBusinessHour, tvDescription;
    private ImageView ivRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        tvName = findViewById(R.id.tvRestoran);
        tvAddress = findViewById(R.id.tvAlamat);
        tvBusinessHour = findViewById(R.id.tvJam);
        tvDescription = findViewById(R.id.tvDeskripsi);
        ivRestaurant = findViewById(R.id.ivMaps);

        Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            String name = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");
            String imageURL = restaurantBundle.getString("imageURL");

            tvName.setText(name);
            tvAddress.setText(address);
            tvBusinessHour.setText(businessHour);
            tvDescription.setText(description);

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_restaurant)
                    .error(R.drawable.ic_error);

            Glide.with(this)
                    .load(imageURL)
                    .apply(requestOptions)
                    .into(ivRestaurant);
        }
    }
}