package com.pamgroup.restaurantlistapp.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDatabase {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String url = "https://pam-final-project-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public RestaurantDatabase() {
        mDatabase = FirebaseDatabase.getInstance(url).getReference("/");
        mAuth = FirebaseAuth.getInstance();
    }

    public List<Restaurant> getAllRestaurants(){
        List<Restaurant> restaurantList = new ArrayList<>();
        mDatabase.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurantList.add(restaurant);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR",error.getMessage());
            }
        });
        return restaurantList;
    }

    public void addRestaurant(String name, String address, String businessHour, String description, String imageUrl) {
        Restaurant newRestaurant = new Restaurant(name, address, businessHour, description);

        DatabaseReference newItemRef = mDatabase.child("restaurants").push();
        String newRestaurantId = newItemRef.getKey();

        newRestaurant.setRestaurantId(newRestaurantId);
        newItemRef.setValue(newRestaurant);
    }
}
