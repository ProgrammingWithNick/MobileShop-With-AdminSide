package com.example.MobileShop.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.MobileShop.Model.MobileItem;
import com.example.MobileShop.Model.OrderDetails;
import com.example.MobileShop.adapter.RecentBuyAdapter;
import com.example.MobileShop.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String userId;
    private ArrayList<OrderDetails> listOfOrderItem = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Toast.makeText(requireContext(), "User not authenticated.", Toast.LENGTH_SHORT).show();
            return binding.getRoot();
        }

        retrieveBuyHistory();

        // Cancel button logic
        binding.cancelbtn.setOnClickListener(view -> {
            if (!listOfOrderItem.isEmpty()) {
                cancelOrder(listOfOrderItem.get(0).getItemPushKey());
            } else {
                Toast.makeText(requireContext(), "No recent order to cancel.", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void retrieveBuyHistory() {
        binding.recentBuyItem.setVisibility(View.INVISIBLE); // Hide recent item initially

        if (userId == null) {
            Toast.makeText(requireContext(), "User ID is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference historyRef = database.getReference("users").child(userId).child("History");
        Query query = historyRef.orderByChild("currentTime");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfOrderItem.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = itemSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                    }
                }

                if (!listOfOrderItem.isEmpty()) {
                    Collections.reverse(listOfOrderItem); // Show latest first
                    displayRecentBuyItem();
                    setupPreviousBuyItemsRecycler();
                } else {
                    Toast.makeText(requireContext(), "No history found.", Toast.LENGTH_SHORT).show();
                    binding.recentBuyItem.setVisibility(View.GONE); // Hide recent item UI
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to retrieve history: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelOrder(String orderPushKey) {
        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference orderRef = database.getReference("OrderDetails").child(orderPushKey);

        orderRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Order canceled successfully.", Toast.LENGTH_SHORT).show();
                    retrieveBuyHistory();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to cancel order: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


    private void displayRecentBuyItem() {
        if (listOfOrderItem.isEmpty()) return;

        OrderDetails recentOrder = listOfOrderItem.get(0);
        binding.recentBuyItem.setVisibility(View.VISIBLE);

        if (recentOrder.getMobileItems() != null && !recentOrder.getMobileItems().isEmpty()) {
            MobileItem firstItem = recentOrder.getMobileItems().get(0);

            binding.BuyAgainName.setText(firstItem.getMobileName() != null ? firstItem.getMobileName() : "Unknown");
            binding.BuyAgainPrice.setText(firstItem.getMobilePrice() != null ? firstItem.getMobilePrice() : "N/A");

            Glide.with(requireContext())
                    .load(firstItem.getMobileImage() != null ? Uri.parse(firstItem.getMobileImage()) : null)
                    .into(binding.BuyAgainImage);

            updateButtonVisibility(recentOrder);
        }
    }

    private void updateButtonVisibility(OrderDetails orderDetails) {
        if (orderDetails.isOrderAccepted()) {
            if (orderDetails.isPaymentReceived()) {
                binding.receivedbtn.setVisibility(View.GONE);
                binding.cancelbtn.setVisibility(View.VISIBLE);
            } else {
                binding.receivedbtn.setVisibility(View.VISIBLE);
                binding.cancelbtn.setVisibility(View.GONE);
                binding.orderdStatus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                binding.receivedbtn.setOnClickListener(view -> updateOrderStatus(orderDetails.getItemPushKey()));
            }
        } else {
            binding.receivedbtn.setVisibility(View.GONE);
            binding.cancelbtn.setVisibility(View.VISIBLE);
        }
    }

    private void updateOrderStatus(String itemPushKey) {
        DatabaseReference completeOrderRef = database.getReference().child("CompletedOrder").child(itemPushKey);

        completeOrderRef.child("paymentReceived").setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Order marked as received.", Toast.LENGTH_SHORT).show();
                    retrieveBuyHistory(); // Refresh UI to reflect updated status
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setupPreviousBuyItemsRecycler() {
        if (listOfOrderItem.size() <= 1) return;

        ArrayList<String> mobileNames = new ArrayList<>();
        ArrayList<String> mobilePrices = new ArrayList<>();
        ArrayList<String> mobileImages = new ArrayList<>();
        ArrayList<Integer> mobileQuantities = new ArrayList<>();

        for (int i = 1; i < listOfOrderItem.size(); i++) {
            OrderDetails order = listOfOrderItem.get(i);
            if (order.getMobileItems() != null && !order.getMobileItems().isEmpty()) {
                MobileItem item = order.getMobileItems().get(0);
                mobileNames.add(item.getMobileName());
                mobilePrices.add(item.getMobilePrice());
                mobileImages.add(item.getMobileImage());
                mobileQuantities.add(item.getMobileQuantity());
            }
        }

        RecentBuyAdapter adapter = new RecentBuyAdapter(mobileNames, mobilePrices, mobileImages, mobileQuantities);
        binding.BuyAgainRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.BuyAgainRecyclerView.setAdapter(adapter);
    }
}
