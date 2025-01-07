package com.example.MobileShop.Model;

public class MenuItem {
    private String name;
    private String price;
    private String description;
    private String imageUrl; // URL of the image for web loading
    private String Feature;

    // No-argument constructor required for Firebase
    public MenuItem() {
    }

    // Constructor with parameters for easier instantiation
    public MenuItem(String name, String price, String description, String imageUrl, String Feature) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.Feature = Feature;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for price
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter and Setter for Feature
    public String getFeature() {
        return Feature;
    }

    public void setFeature(String Feature) {
        this.Feature = Feature;
    }
}
