package com.example.MobileShop.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.MobileShop.Fragment.CongratsBottomShetFragment;
import com.example.MobileShop.Model.CartItems;
import com.example.MobileShop.Model.MobileItem;
import com.example.MobileShop.Model.OrderDetails;
import com.example.MobileShop.databinding.ActivityPayOutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PayOutActivity extends AppCompatActivity {
    private ActivityPayOutBinding binding;
    private String userId;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private String userName;
    private String userAddress;
    private String userPhone;
    private ArrayList<CartItems> cartItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding and set content view
        binding = ActivityPayOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up edge-to-edge display for immersive UI
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve list of CartItems from intent
        cartItemsList = getIntent().getParcelableArrayListExtra("CartItemsList");

        // Set user data and calculate initial total amount
        setUserData();
        updateTotalAmount();

        // Set up Place Order button to show congrats bottom sheet
        binding.PlaceMyOrder.setOnClickListener(view -> {
            userName = binding.name.getText().toString().trim();
            userAddress = binding.address.getText().toString().trim();
            userPhone = binding.phone.getText().toString().trim();

            if (userName.isEmpty() || userAddress.isEmpty() || userPhone.isEmpty()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            } else if (userPhone.length() < 10) {
                Toast.makeText(this, "Phone number must be at least 10 digits", Toast.LENGTH_SHORT).show();
            } else {
                placeOrder();
                // Show the congrats bottom sheet after placing the order
                CongratsBottomShetFragment bottomSheetFragment = new CongratsBottomShetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), "CongratsBottomSheet");
                removeItemFromCart();
            }
        });

        // Back button to finish the activity
        binding.button3.setOnClickListener(view -> finish());
    }

    private void removeItemFromCart() {
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference cartRef = databaseReference.child("users").child(userId).child("CartItems");
        cartRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                Toast.makeText(this, "Cart items removed successfully.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Items Buy successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to remove cart items.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void placeOrder() {
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        long time = System.currentTimeMillis();
        DatabaseReference orderPushKeyRef = databaseReference.child("OrderDetails").push();
        String itemPushKey = orderPushKeyRef.getKey();

        if (itemPushKey == null) {
            Toast.makeText(this, "Failed to generate order key.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the list of MobileItem objects from cartItemsList
        List<MobileItem> mobileItems = new ArrayList<>();
        for (CartItems cartItem : cartItemsList) {
            mobileItems.add(new MobileItem(
                    cartItem.getName(),
                    cartItem.getPrice(),
                    cartItem.getImage(),
                    cartItem.getQuantity()
            ));
        }

        // Create an OrderDetails object with the hierarchical structure
        OrderDetails orderDetails = new OrderDetails(
                userId,
                userName,
                mobileItems, // Pass the list of MobileItem objects
                userAddress,
                "₹" + calculateTotal(),
                userPhone,
                false,
                false,
                itemPushKey,
                time
        );

        // Save the order details to Firebase
        orderPushKeyRef.setValue(orderDetails).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        addOrderToHistory(orderDetails);
    }


    private void addOrderToHistory(OrderDetails orderDetails) {
        databaseReference.child("users").child(userId).child("History").child(orderDetails.getItemPushKey()).setValue(orderDetails)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Order added to history!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add order to history: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String getMobileNames() {
        StringBuilder names = new StringBuilder();
        for (CartItems item : cartItemsList) {
            names.append(item.getName()).append(", ");
        }
        return names.toString().replaceAll(", ₹", "");
    }

    private String getMobileImages() {
        StringBuilder images = new StringBuilder();
        for (CartItems item : cartItemsList) {
            images.append(item.getImage()).append(", ");
        }
        return images.toString().replaceAll(", ₹", "");
    }

    private String getMobilePrices() {
        StringBuilder prices = new StringBuilder();
        for (CartItems item : cartItemsList) {
            prices.append(item.getPrice()).append(", ");
        }
        return prices.toString().replaceAll(", ₹", "");
    }

    private int getMobileQuantities() {
        int totalQuantity = 0;
        for (CartItems item : cartItemsList) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    private void updateTotalAmount() {
        int totalAmount = calculateTotal();
        String finalTotalAmount = "₹" + totalAmount;
        binding.totalAmount.setText(finalTotalAmount);
    }

    private int calculateTotal() {
        int totalAmount = 0;
        if (cartItemsList != null) {
            for (CartItems item : cartItemsList) {
                if (item != null && item.getPrice() != null) {
                    String priceString = item.getPrice().replaceAll("[^\\d]", "");
                    try {
                        int priceInt = Integer.parseInt(priceString);
                        totalAmount += priceInt * item.getQuantity();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(PayOutActivity.this, "Error parsing price for item: " + priceString, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return totalAmount;
    }

    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = databaseReference.child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userName = snapshot.child("name").getValue(String.class);
                        userAddress = snapshot.child("address").getValue(String.class);
                        userPhone = snapshot.child("phone").getValue(String.class);

                        binding.name.setText(userName != null ? userName : "");
                        binding.address.setText(userAddress != null ? userAddress : "");
                        binding.phone.setText(userPhone != null ? userPhone : "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PayOutActivity.this, "Failed to load user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
