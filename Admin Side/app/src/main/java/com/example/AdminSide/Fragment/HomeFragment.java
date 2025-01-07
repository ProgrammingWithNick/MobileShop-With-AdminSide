package com.example.AdminSide.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.AdminSide.Model.OrderDetails;
import com.example.AdminSide.R;
import com.example.AdminSide.activity.AddItemActivity;
import com.example.AdminSide.activity.AllItemActivity;
import com.example.AdminSide.activity.OutForDeliveryActivity;
import com.example.AdminSide.activity.PendingOrderActivity;
import com.example.AdminSide.auth.LoginFragment;
import com.example.AdminSide.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference completedOrderRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Set click listeners for menu items
        binding.addMenu.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddItemActivity.class)));
        binding.allItemMenu.setOnClickListener(view -> startActivity(new Intent(getActivity(), AllItemActivity.class)));
        binding.outForDeliveryBtn.setOnClickListener(view -> startActivity(new Intent(getActivity(), OutForDeliveryActivity.class)));
        binding.cardView.setOnClickListener(view -> startActivity(new Intent(getActivity(), PendingOrderActivity.class)));

        // Logout button functionality
        binding.logout.setOnClickListener(view -> {
            if (auth != null) {
                auth.signOut();
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main, loginFragment).commit();
            }
        });

        // Load data
        pendingOrder();
//        completedOrders();
//        wholeTimeEarning();

        return binding.getRoot();
    }

//    private void wholeTimeEarning() {
//        ArrayList<Integer> listOfTotalPay = new ArrayList<>();
//        completedOrderRef = database.getReference().child("CompletedOrder");
//
//        completedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
//                    OrderDetails completeOrder = orderSnapshot.getValue(OrderDetails.class);
//                    if (completeOrder != null) {
//                        try {
//                            int totalPrice = Integer.parseInt(completeOrder.getTotalPrice().replace("₹", ""));
//                            listOfTotalPay.add(totalPrice);
//                        } catch (NumberFormatException e) {
//                            System.err.println("Invalid price format: " + completeOrder.getTotalPrice());
//                        }
//                    }
//                }
//
//                int sum = 0;
//                for (int price : listOfTotalPay) {
//                    sum += price;
//                }
//
//                binding.textView10.setText("₹ " + sum);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                System.err.println("Failed to read completed orders: " + error.getMessage());
//            }
//        });
//    }

//    private void completedOrders() {
//        DatabaseReference completedOrderRef = database.getReference().child("CompletedOrder");
//
//        completedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                long completedOrderItemsCount = snapshot.getChildrenCount();
//                binding.textView8.setText(String.valueOf(completedOrderItemsCount));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                System.err.println("Failed to read completed orders: " + error.getMessage());
//            }
//        });
//    }

    private void pendingOrder() {
        DatabaseReference pendingOrderRef = database.getReference().child("OrderDetails");

        pendingOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long pendingOrderItemsCount = snapshot.getChildrenCount();
                binding.textView6.setText(String.valueOf(pendingOrderItemsCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Failed to read pending orders: " + error.getMessage());
            }
        });
    }
}
