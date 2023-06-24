package com.pamgroup.restaurantlistapp.model;

public class Restaurant {
    private String restaurantId;
    private String name;
    private String address;
    private String businessHour;
    private String description;
    private String imageURL;

    private String longitude;

    private String latitude;

    public Restaurant(){

    }

    public Restaurant(String name, String address, String description, String businessHour, String imageURL, String longitude, String latitude){
        this.name = name;
        this.address = address;
        this.description = description;
        this.businessHour = businessHour;
        this.imageURL = imageURL;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
