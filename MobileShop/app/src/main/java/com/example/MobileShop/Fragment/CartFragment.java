package com.example.MobileShop.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.MobileShop.Model.CartItems;
import com.example.MobileShop.activity.PayOutActivity;
import com.example.MobileShop.adapter.CartAdapter;
import com.example.MobileShop.databinding.FragmentCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment  {
    private FragmentCartBinding binding;
    private List<CartItems> cartItemsList = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private CartAdapter cartAdapter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        retrieveCartItems();

        // Set up button click listener to start PayOutActivity
        binding.proccedButton.setOnClickListener(view -> {
            if (cartItemsList.isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                GetOrderItemsDetail();
            }
        });

        return binding.getRoot();
    }

    private void GetOrderItemsDetail() {
        // Ensure `userId` and `cartAdapter` are initialized
        if (userId == null || cartAdapter == null) {
            Toast.makeText(requireContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference orderRef = database.getReference("users").child(userId).child("CartItems");

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartItems> cartItemsForIntent = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    CartItems cartItem = childSnapshot.getValue(CartItems.class);
                    if (cartItem != null) {
                        cartItemsForIntent.add(cartItem);
                    }
                }

                // Ensure fragment is added and context is not null before starting activity
                if (isAdded() && getContext() != null) {
                    Intent intent = new Intent(requireContext(), PayOutActivity.class);
                    intent.putParcelableArrayListExtra("CartItemsList", cartItemsForIntent);
                    requireContext().startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Order creation failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveCartItems() {
        userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId != null) {
            DatabaseReference userCartRef = database.getReference("users").child(userId).child("CartItems");

            userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartItemsList.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        CartItems cartItem = childSnapshot.getValue(CartItems.class);
                        if (cartItem != null) {
                            cartItem.setKey(childSnapshot.getKey()); // Store the Firebase key
                            cartItemsList.add(cartItem);
                        }
                    }
                    // Ensure adapter is set if the fragment is attached
                    if (isAdded() && getContext() != null) {
                        setAdapter();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("DatabaseError", "Error", error.toException());
                }
            });
        } else {
            Log.d("FirebaseAuth", "User not authenticated");
            Toast.makeText(requireContext(), "Please log in to view your cart.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter() {
        cartAdapter = new CartAdapter(cartItemsList, requireContext());  // Pass this as the quantity change listener
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.cartRecyclerView.setAdapter(cartAdapter);
    }

    // Implement the onQuantityChanged method from the OnQuantityChangeListener interface

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
