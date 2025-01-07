package com.example.MobileShop.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MobileItem implements Parcelable {
    private String mobileName;
    private String mobilePrice;
    private String mobileImage;
    private int mobileQuantity;

    // Default constructor (required for Firebase)
    public MobileItem() {
    }

    public MobileItem(String mobileName, String mobilePrice, String mobileImage, int mobileQuantity) {
        this.mobileName = mobileName;
        this.mobilePrice = mobilePrice;
        this.mobileImage = mobileImage;
        this.mobileQuantity = mobileQuantity;
    }

    // Getters and Setters
    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }

    public String getMobilePrice() {
        return mobilePrice;
    }

    public void setMobilePrice(String mobilePrice) {
        this.mobilePrice = mobilePrice;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public int getMobileQuantity() {
        return mobileQuantity;
    }

    public void setMobileQuantity(int mobileQuantity) {
        this.mobileQuantity = mobileQuantity;
    }

    // Parcelable implementation
    protected MobileItem(Parcel in) {
        mobileName = in.readString();
        mobilePrice = in.readString();
        mobileImage = in.readString();
        mobileQuantity = in.readInt();
    }

    public static final Creator<MobileItem> CREATOR = new Creator<MobileItem>() {
        @Override
        public MobileItem createFromParcel(Parcel in) {
            return new MobileItem(in);
        }

        @Override
        public MobileItem[] newArray(int size) {
            return new MobileItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileName);
        dest.writeString(mobilePrice);
        dest.writeString(mobileImage);
        dest.writeInt(mobileQuantity);
    }
}
