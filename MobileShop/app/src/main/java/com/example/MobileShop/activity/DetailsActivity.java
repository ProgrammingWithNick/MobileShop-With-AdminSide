package com.example.MobileShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.MobileShop.Model.CartItems;
import com.example.MobileShop.databinding.ActivityDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private String key;
    private String name;
    private String price;
    private String description;
    private String imageUrl;
    private String Feature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Inflate the binding for this activity
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the back button
        binding.imageButton.setOnClickListener(view -> finish());

        // Set up the "Add Item" button
        binding.addItembutton.setOnClickListener(view -> addItembutton());

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("MenuItemName");
            description = intent.getStringExtra("MenuItemDescription");
            Feature = intent.getStringExtra("MenuItemFeature");
            price = intent.getStringExtra("MenuItemPrice");
            imageUrl = intent.getStringExtra("MenuItemImage");
        }

        // Set data to views if available
        if (name != null) {
            binding.detailsName.setText(name);
        }
        if (description != null) {
            binding.DescriptionTextView.setText(description);
        }
        if (Feature != null) {
            binding.IngredientsTextView.setText(Feature);
        }
//        if (price != null) {
//            binding.priceTextView.setText(price);
//        }

        // Load image with Glide from URL
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(binding.DetailsImage);
        }

        // Handle window insets (for edge-to-edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addItembutton() {
        if (auth.getCurrentUser() == null) {
            Log.e("FirebaseAuth", "User not authenticated");
            showToast("Please log in to add items to the cart.");
            return;
        }

        database = FirebaseDatabase.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference userCartRef = database.getReference("users").child(userId).child("CartItems");

        // Create CartItems object with the required data
        CartItems cartItem = new CartItems(userCartRef.getKey(), name, price, description, imageUrl, 1,Feature);  // Quantity is initialized as "1"

        // Push the item to the user's cart in Firebase
        userCartRef.push().setValue(cartItem)
                .addOnSuccessListener(aVoid -> showToast("Item added to cart successfully! 😊"))
                .addOnFailureListener(e -> showToast("Failed to add item to cart 😒: " + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
