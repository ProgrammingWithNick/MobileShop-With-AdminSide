package com.example.AdminSide.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.AdminSide.Adapter.DeliveryAdapter;
import com.example.AdminSide.Model.OrderDetails;
import com.example.AdminSide.databinding.ActivityOutForDeliveryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class OutForDeliveryActivity extends AppCompatActivity {
    private ActivityOutForDeliveryBinding binding;

    private FirebaseDatabase database;
    private final ArrayList<OrderDetails> listOfCompleteOrderList = new ArrayList<>();
    private DeliveryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutForDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button to finish activity
        binding.backButton.setOnClickListener(view -> finish());

        // Fetch and display data
        retrieveCompleteOrderDetails();
    }

    private void retrieveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference completeOrderRef = database.getReference().child("CompletedOrder");

        // Query to order by 'currentTime'
        Query query = completeOrderRef.orderByChild("currentTime");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfCompleteOrderList.clear();
                // Process the retrieved data
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderDetails completedOrder = data.getValue(OrderDetails.class);
                    if (completedOrder != null) {
                        listOfCompleteOrderList.add(completedOrder);
                    }
                }
                // Reverse the list to display the most recent first
                Collections.reverse(listOfCompleteOrderList);

                // Update RecyclerView
                setDataIntoRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch completed orders: " + error.getMessage());
            }
        });
    }

    private void setDataIntoRecyclerView() {
        // Prepare data for the adapter
        ArrayList<String> customerNames = new ArrayList<>();
        ArrayList<Boolean> moneyStatuses = new ArrayList<>();

        for (OrderDetails order : listOfCompleteOrderList) { // Fixing the iteration syntax
            customerNames.add(order.getUserName()); // Extract customer name
            moneyStatuses.add(order.isPaymentReceived()); // Extract payment status
        }

        // Set up RecyclerView and Adapter
        adapter = new DeliveryAdapter(customerNames, moneyStatuses);
        binding.deleveryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.deleveryRecyclerView.setAdapter(adapter);
    }
}
