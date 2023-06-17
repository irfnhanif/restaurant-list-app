package com.pamgroup.restaurantlistapp.model;

public class Restaurant {
    private String restaurantId;
    private String name;
    private String address;
    private String businessHour;
    private String description;
    private String imageURL;

    public Restaurant(){

    }

    public Restaurant(String name, String address, String description, String businessHour){
        this.name = name;
        this.address = address;
        this.description = description;
        this.businessHour = businessHour;
    }
    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(String businessHour) {
        this.businessHour = businessHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
