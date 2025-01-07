package com.example.MobileShop.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.MobileShop.Model.OrderDetails;
import com.example.MobileShop.Model.MobileItem;
import com.example.MobileShop.adapter.RecentBuyAdapter;
import com.example.MobileShop.databinding.ActivityRecentOrderItemsBinding;

import java.util.ArrayList;

public class RecentOrderItems extends AppCompatActivity {
    private ActivityRecentOrderItemsBinding binding;

    // Lists for storing data to pass to the adapter
    private final ArrayList<String> allMobileName = new ArrayList<>();
    private final ArrayList<String> allMobilePrice = new ArrayList<>();
    private final ArrayList<String> allMobileImage = new ArrayList<>();
    private final ArrayList<Integer> allMobileQuantity = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ViewBinding
        binding = ActivityRecentOrderItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle window insets for proper layout adjustment
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle back button click
        binding.backbutton.setOnClickListener(view -> finish());

        // Retrieve the order items passed via Intent
        ArrayList<OrderDetails> recentOrderItems = (ArrayList<OrderDetails>) getIntent().getSerializableExtra("RecentBuyOrderItem");

        if (recentOrderItems != null && !recentOrderItems.isEmpty()) {
            for (OrderDetails orderDetails : recentOrderItems) {
                // Extract data from the List<MobileItem> inside OrderDetails
                for (MobileItem mobileItem : orderDetails.getMobileItems()) {
                    allMobileName.add(mobileItem.getMobileName());
                    allMobilePrice.add(mobileItem.getMobilePrice());
                    allMobileImage.add(mobileItem.getMobileImage());
                    allMobileQuantity.add(mobileItem.getMobileQuantity());
                }
            }
        } else {
            // Show a message if no order items were passed
            binding.RecyclerView.setVisibility(View.VISIBLE); // Make sure you have an emptyTextView in your layout for this
        }

        // Set up RecyclerView with RecentBuyAdapter
        RecentBuyAdapter adapter = new RecentBuyAdapter(allMobileName, allMobilePrice, allMobileImage, allMobileQuantity);
        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.RecyclerView.setAdapter(adapter);
    }
}
