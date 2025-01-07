package com.example.AdminSide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.AdminSide.Model.OrderDetails;
import com.example.AdminSide.auth.LoginFragment;
import com.example.AdminSide.databinding.ActivityAdminMainBinding;
import com.example.AdminSide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {
    private ActivityAdminMainBinding binding;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference completedOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button listeners
//        binding.addMenu.setOnClickListener(view -> startActivity(new Intent(this, AddItemActivity.class)));
//        binding.allItemMenu.setOnClickListener(view -> startActivity(new Intent(this, AllItemActivity.class)));
//        binding.outForDeliveryBtn.setOnClickListener(view -> startActivity(new Intent(this, OutForDeliveryActivity.class)));
//        binding.pendingtextview.setOnClickListener(view -> startActivity(new Intent(this, PendingOrderActivity.class)));

//        binding.logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Ensure FirebaseAuth is initialized
//                if (auth == null) {
//                    auth = FirebaseAuth.getInstance();
//                }
//
//                // Perform sign-out
//                auth.signOut();
//
//                // Replace current fragment with LoginFragment
//                Fragment loginFragment = new LoginFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.main, loginFragment) // Use your container ID
//                        .commit();
//            }
//        });


        // Initialize NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView2);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
        }

//        // Initialize Firebase database
//        database = FirebaseDatabase.getInstance();
//        completedOrderRef = database.getReference().child("CompletedOrder");
//
//        // Load data
//        pendingOrder();
//        completedOrders();
//        wholeTimeEarning();
    }

//    private void wholeTimeEarning() {
//        ArrayList<Integer> listOfTotalPay = new ArrayList<>();
//        completedOrderRef = FirebaseDatabase.getInstance().getReference().child("CompletedOrder");
//        // Fetch completed orders and calculate earnings
//        completedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
//                    OrderDetails completeOrder = orderSnapshot.getValue(OrderDetails.class);
//                    if (completeOrder != null) {
//                        try {
//                            // Remove ₹ and parse total price as integer
//                            int totalPrice = Integer.parseInt(completeOrder.getTotalPrice().replace("₹", ""));
//                            listOfTotalPay.add(totalPrice);
//                        } catch (NumberFormatException e) {
//                            // Handle invalid price format
//                            System.err.println("Invalid price format: " + completeOrder.getTotalPrice());
//                        }
//                    }
//                }
//
//                // Calculate sum of total prices
//                int sum = 0;
//                for (int price : listOfTotalPay) {
//                    sum += price;
//                }
//
//                // Update the TextView with the total earnings
////                binding.textView10.setText("₹ "+sum);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Log the error
//                System.err.println("Failed to read completed orders: " + error.getMessage());
//            }
//        });
//    }
//
//    private void completedOrders() {
//        DatabaseReference completedOrderRef = database.getReference().child("CompletedOrder");
//
//        // Fetch completed orders count
//        completedOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Get the count of completed orders
//                long completedOrderItemsCount = snapshot.getChildrenCount();
//
//                // Update the TextView with the count
////                binding.textView8.setText(String.valueOf(completedOrderItemsCount));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Log the error
//                System.err.println("Failed to read completed orders: " + error.getMessage());
//            }
//        });
//    }
//
//    private void pendingOrder() {
//        DatabaseReference pendingOrderRef = database.getReference().child("OrderDetails");
//
//        // Fetch pending orders count
//        pendingOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Get the count of pending orders
//                long pendingOrderItemsCount = snapshot.getChildrenCount();
//
//                // Update the TextView with the count
////                binding.textView6.setText(String.valueOf(pendingOrderItemsCount));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Log the error
//                System.err.println("Failed to read pending orders: " + error.getMessage());
//            }
//        });
//    }
}
