package com.example.AdminSide.Model;

import java.io.Serializable;

public class AllMenu implements Serializable {
    private String key;
    private String name;
    private String price;
    private String description;
    private String imageUrl; // URL of the image for web loading
    private String feature;  // Updated naming convention for consistency

    // No-argument constructor required for Firebase
    public AllMenu() {
    }

    // Constructor with parameters for easier instantiation
    public AllMenu(String key, String name, String price, String description, String imageUrl, String feature) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.feature = feature;
    }

    // Getter and Setter for key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    // Getter and Setter for feature
    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
