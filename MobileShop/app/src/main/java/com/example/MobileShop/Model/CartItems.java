package com.example.MobileShop.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItems implements Parcelable {
    private String key;
    private String name;
    private String price;
    private String description;
    private String Feature;
    private String image;
    private int quantity;

    // Default constructor required by Firebase
    public CartItems() {
        this.key = "";
        this.name = "";
        this.price = "";
        this.description = "";
        this.Feature = "";
        this.image = "";
        this.quantity = 0;
    }

    // Constructor with parameters
    public CartItems(String key, String name, String price, String description, String image, int quantity, String Feature) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.Feature = Feature;
    }

    // Parcelable implementation
    protected CartItems(Parcel in) {
        key = in.readString();
        name = in.readString();
        price = in.readString();
        description = in.readString();
        Feature = in.readString();
        image = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<CartItems> CREATOR = new Creator<CartItems>() {
        @Override
        public CartItems createFromParcel(Parcel in) {
            return new CartItems(in);
        }

        @Override
        public CartItems[] newArray(int size) {
            return new CartItems[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(Feature);
        dest.writeString(image);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String Feature) {
        this.Feature = Feature;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
