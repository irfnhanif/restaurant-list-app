package com.pamgroup.restaurantlistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pamgroup.restaurantlistapp.helper.RestaurantCallback;
import com.pamgroup.restaurantlistapp.helper.RestaurantDatabase;
import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RestaurantDatabase mDatabase;
    private RecyclerView rvRestaurantList;
    private RestaurantAdapter adapter;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRestaurantList = findViewById(R.id.rv_restaurant_list);
        btnAdd = findViewById(R.id.btn_add);

        adapter = new RestaurantAdapter(this.getApplicationContext());

        mDatabase.getAllRestaurants(restaurantList -> {
            adapter.setRestaurantList(restaurantList);
            adapter.notifyDataSetChanged();
        });

        rvRestaurantList.setAdapter(adapter);
        rvRestaurantList.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent addIntent = new Intent(this, CreateRestaurant.class);
        startActivity(addIntent);
    }
}
