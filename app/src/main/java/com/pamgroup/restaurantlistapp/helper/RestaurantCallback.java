package com.pamgroup.restaurantlistapp.helper;

import com.pamgroup.restaurantlistapp.model.Restaurant;

import java.util.List;

public interface RestaurantCallback {
    void onRestaurantListReceived(List<Restaurant> restaurantList);
    void onFailure(String errorMessage);
}
