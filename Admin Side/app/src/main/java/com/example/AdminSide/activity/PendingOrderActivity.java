package com.example.AdminSide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.AdminSide.Adapter.PendingOrderAdapter;
import com.example.AdminSide.Model.MobileItem;
import com.example.AdminSide.Model.OrderDetails;
import com.example.AdminSide.databinding.ActivityPendingOrderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingOrderActivity extends AppCompatActivity implements PendingOrderAdapter.OnItemClicked {

    private ActivityPendingOrderBinding binding;
    private final ArrayList<String> listOfName = new ArrayList<>();
    private final ArrayList<String> listOfTotalPrice = new ArrayList<>();
    private final ArrayList<String> listOfImage = new ArrayList<>();
    private final ArrayList<OrderDetails> listOfOrderItem = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button to finish activity
        binding.backButton.setOnClickListener(view -> finish());

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("OrderDetails");

        // Fetch order details from Firebase
        getOrdersDetails();
    }

    private void getOrdersDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear lists before adding new data
                listOfOrderItem.clear();
                listOfName.clear();
                listOfTotalPrice.clear();
                listOfImage.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = orderSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                        listOfName.add(orderDetails.getUserName());
                        listOfTotalPrice.add(orderDetails.getTotalPrice());

                        // Iterate through the mobileItems list to get the images
                        for (MobileItem mobileItem : orderDetails.getMobileItems()) {
                            listOfImage.add(mobileItem.getMobileImage());
                        }
                    }
                }

                // Set up the RecyclerView if data is available
                setupRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PendingOrderActivity", "Failed to fetch order details: " + error.getMessage());
                binding.textView5.setText("Failed to load orders: " + error.getMessage());
                binding.textView5.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupRecyclerView() {
        if (!listOfOrderItem.isEmpty()) {
            PendingOrderAdapter adapter = new PendingOrderAdapter(
                    listOfName,
                    listOfTotalPrice,
                    listOfImage,
                    this,
                    this  // Passing the OnItemClicked listener to handle clicks
            );
            binding.PendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.PendingOrderRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClickListener(int position) {
        if (position >= 0 && position < listOfOrderItem.size()) {
            OrderDetails userOrderDetails = listOfOrderItem.get(position);

            Intent intent = new Intent(this, OrderDetailsActivity.class);
            intent.putExtra("UserOrderDetails", userOrderDetails);
            startActivity(intent);
        } else {
            Log.e("PendingOrderActivity", "Invalid position: " + position);
            Toast.makeText(this, "Unable to open order details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemAcceptClickListener(int position) {
        String childItemsPushKey = listOfOrderItem.get(position).getItemPushKey();
        if (childItemsPushKey == null || childItemsPushKey.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid item key!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference clickItemOrderRef = database.getReference()
                .child("OrderDetails")
                .child(childItemsPushKey);

        clickItemOrderRef.child("orderAccepted").setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Order Accepted!", Toast.LENGTH_SHORT).show();
                    updateOrderAcceptStatus(position);
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to accept order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateOrderAcceptStatus(int position) {
        String userIdOfClickedItem = listOfOrderItem.get(position).getUserUid();
        String pushKeyOfClickedItem = listOfOrderItem.get(position).getItemPushKey();

        DatabaseReference historyRef = database.getReference()
                .child("users")
                .child(userIdOfClickedItem)
                .child("History")
                .child(pushKeyOfClickedItem);

        historyRef.child("orderAccepted").setValue(true);
        databaseReference.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true);
    }

    @Override
    public void onItemDispatchClickListener(int position) {
        String dispatchItemsPushKey = listOfOrderItem.get(position).getItemPushKey();

        if (dispatchItemsPushKey == null || dispatchItemsPushKey.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid item key!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference dispatchItemOrderRef = database.getReference()
                .child("CompletedOrder")
                .child(dispatchItemsPushKey);

        dispatchItemOrderRef.setValue(listOfOrderItem.get(position))
                .addOnSuccessListener(aVoid -> deleteThisItemFromOrderDetails(dispatchItemsPushKey))
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to dispatch order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteThisItemFromOrderDetails(String dispatchItemsPushKey) {
        DatabaseReference orderDetailsItemsRef = database.getReference()
                .child("OrderDetails")
                .child(dispatchItemsPushKey);

        orderDetailsItemsRef.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Order Dispatched!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Order Not Dispatched!", Toast.LENGTH_SHORT).show());
    }
}
