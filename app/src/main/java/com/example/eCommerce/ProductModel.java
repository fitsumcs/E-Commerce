package com.example.eCommerce;

public class ProductModel {

    String type;
    float amount;
    String title;
    String date;
    String id;
    String imageUrl;

    String longitude;
    String latitude;
    public ProductModel(){

    }


    public ProductModel(String type, float amount, String title, String date, String id, String imageUrl, String longitude, String latitude) {
        this.type = type;
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.id = id;
        this.imageUrl = imageUrl;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
