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

    public void getAllRestaurants(RestaurantCallback callback) {
        mDatabase.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Restaurant> restaurantList = new ArrayList<>();
                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurantList.add(restaurant);
                    }
                }
                callback.onRestaurantListReceived(restaurantList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", error.getMessage());
            }
        });
    }


    public void addRestaurant(String name, String address, String businessHour, String description, String imageURL) {
        Restaurant newRestaurant = new Restaurant(name, address, businessHour, description, imageURL);

        DatabaseReference newRestaurantRef = mDatabase.child("restaurants").push();
        String newRestaurantId = newRestaurantRef.getKey();

        newRestaurant.setRestaurantId(newRestaurantId);
        newRestaurantRef.setValue(newRestaurant);
    }

    public void editRestaurant(String restaurantId, String name, String address, String businessHour, String description, String imageURL) {
        DatabaseReference newRestaurantRef = mDatabase.child("restaurants").child(restaurantId);
        newRestaurantRef.child("name").setValue(name);
        newRestaurantRef.child("address").setValue(address);
        newRestaurantRef.child("businessHour").setValue(businessHour);
        newRestaurantRef.child("description").setValue(description);
        newRestaurantRef.child("imageURL").setValue(imageURL);
    }
    public void deleteRestaurant(String restaurantId) {
        DatabaseReference restaurantRef = mDatabase.child("restaurants").child(restaurantId);
        restaurantRef.removeValue();
    }
}
