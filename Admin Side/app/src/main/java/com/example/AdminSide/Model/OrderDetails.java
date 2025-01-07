package com.example.AdminSide.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails implements Parcelable {

    private String userUid;
    private String userName;
    private List<MobileItem> mobileItems; // Hierarchical structure
    private String address;
    private String totalPrice;
    private String phoneNumber;
    private boolean orderAccepted;
    private boolean paymentReceived;
    private String itemPushKey;
    private long currentTime;

    // No-argument constructor for Firebase compatibility
    public OrderDetails() {
        this("", "", new ArrayList<>(), "", "", "", false, false, "", System.currentTimeMillis());
    }

    // Constructor with all parameters
    public OrderDetails(String userUid, String userName, List<MobileItem> mobileItems, String address, String totalPrice,
                        String phoneNumber, boolean orderAccepted, boolean paymentReceived, String itemPushKey, long currentTime) {
        this.userUid = userUid != null ? userUid : "";
        this.userName = userName != null ? userName : "";
        this.mobileItems = mobileItems != null ? mobileItems : new ArrayList<>();
        this.address = address != null ? address : "";
        this.totalPrice = totalPrice != null ? totalPrice : "";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.orderAccepted = orderAccepted;
        this.paymentReceived = paymentReceived;
        this.itemPushKey = itemPushKey != null ? itemPushKey : "";
        this.currentTime = currentTime > 0 ? currentTime : System.currentTimeMillis();
    }

    // Parcelable implementation
    protected OrderDetails(Parcel in) {
        userUid = in.readString();
        userName = in.readString();
        mobileItems = in.createTypedArrayList(MobileItem.CREATOR); // MobileItem must implement Parcelable as well
        address = in.readString();
        totalPrice = in.readString();
        phoneNumber = in.readString();
        orderAccepted = in.readByte() != 0;
        paymentReceived = in.readByte() != 0;
        itemPushKey = in.readString();
        currentTime = in.readLong();
    }

    public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUid);
        dest.writeString(userName);
        dest.writeTypedList(mobileItems); // Write the list of MobileItems
        dest.writeString(address);
        dest.writeString(totalPrice);
        dest.writeString(phoneNumber);
        dest.writeByte((byte) (orderAccepted ? 1 : 0));
        dest.writeByte((byte) (paymentReceived ? 1 : 0));
        dest.writeString(itemPushKey);
        dest.writeLong(currentTime);
    }

    // Getters and setters

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid != null ? userUid : "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName != null ? userName : "";
    }

    public List<MobileItem> getMobileItems() {
        return mobileItems;
    }

    public void setMobileItems(List<MobileItem> mobileItems) {
        this.mobileItems = mobileItems != null ? mobileItems : new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address != null ? address : "";
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice != null ? totalPrice : "";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    }

    public boolean isOrderAccepted() {
        return orderAccepted;
    }

    public void setOrderAccepted(boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public boolean isPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey != null ? itemPushKey : "";
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime > 0 ? currentTime : System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "userUid='" + userUid + '\'' +
                ", userName='" + userName + '\'' +
                ", mobileItems=" + mobileItems +
                ", address='" + address + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", orderAccepted=" + orderAccepted +
                ", paymentReceived=" + paymentReceived +
                ", itemPushKey='" + itemPushKey + '\'' +
                ", currentTime=" + currentTime +
                '}';
    }
}
