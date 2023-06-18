package com.pamgroup.restaurantlistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pamgroup.restaurantlistapp.helper.RestaurantCallback;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;
import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RestaurantDatabase mDatabase;
    private RecyclerView rvRestaurantList;
    private RestaurantAdapter adapter;
    private ImageView btnAdd_restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRestaurantList = findViewById(R.id.rv_restaurant_list);
        btnAdd_restaurant = findViewById(R.id.btn_add_restaurant);

        adapter = new RestaurantAdapter(this.getApplicationContext());
        mDatabase = new RestaurantDatabase();

        rvRestaurantList.setAdapter(adapter);
        rvRestaurantList.setLayoutManager(new LinearLayoutManager(this));

        btnAdd_restaurant.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.getAllRestaurants(new RestaurantCallback() {
            @Override
            public void onRestaurantListReceived(List<Restaurant> restaurantList) {
                adapter.setRestaurantList(restaurantList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ERROR-GET-DATA", errorMessage);
                Toast.makeText(MainActivity.this, "Failed to get restaurants data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_restaurant:
                Intent addIntent = new Intent(this, CreateRestaurant.class);
                startActivity(addIntent);
                break;
            //tinggal nambahin case lain
        }
    }
}
