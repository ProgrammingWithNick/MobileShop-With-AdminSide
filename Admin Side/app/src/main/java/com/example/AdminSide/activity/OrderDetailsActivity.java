package com.example.AdminSide.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.AdminSide.Adapter.OrderDetailsAdapter;
import com.example.AdminSide.Model.MobileItem;
import com.example.AdminSide.Model.OrderDetails;
import com.example.AdminSide.R;
import com.example.AdminSide.databinding.ActivityOrderDetailsBinding;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;

    private String userName;
    private String address;
    private String phoneNumber;
    private String totalPrice;
    private ArrayList<String> mobileName = new ArrayList<>();  // Changed to ArrayList
    private ArrayList<String> mobileImage = new ArrayList<>(); // Changed to ArrayList
    private ArrayList<String> mobilePrice = new ArrayList<>(); // Changed to ArrayList
    private ArrayList<Integer> mobileQuantity = new ArrayList<>(); // Changed to ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.button3.setOnClickListener(view -> finish());

        // Get data passed via intent
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        // Retrieve the passed OrderDetails object from the intent
        OrderDetails recivedOrderDetails = getIntent().getParcelableExtra("UserOrderDetails");

        if (recivedOrderDetails != null) {
            // Retrieve individual details from the received OrderDetails object
            userName = recivedOrderDetails.getUserName();
            address = recivedOrderDetails.getAddress();
            phoneNumber = recivedOrderDetails.getPhoneNumber();
            totalPrice = recivedOrderDetails.getTotalPrice();

            // Iterate through the mobileItems list and extract mobile details
            for (MobileItem item : recivedOrderDetails.getMobileItems()) {
                mobileName.add(item.getMobileName());     // Add mobile name to list
                mobileImage.add(item.getMobileImage());   // Add mobile image to list
                mobileQuantity.add(item.getMobileQuantity()); // Add mobile quantity to list
                mobilePrice.add(item.getMobilePrice());   // Add mobile price to list
            }

            // Set the user details to the UI
            setUserDetail();

            // Initialize adapter with the data
            setAdapter();
        }
    }


    private void setUserDetail() {
        binding.name.setText(userName);
        binding.address.setText(address);
        binding.phone.setText(phoneNumber);
        binding.total.setText(totalPrice);
    }

    private void setAdapter() {
        // Make sure that you are passing ArrayLists to the adapter
        OrderDetailsAdapter adapter = new OrderDetailsAdapter(
                this,
                mobileName,    // ArrayList<String> for mobile names
                mobileImage,   // ArrayList<String> for mobile images
                mobileQuantity,// ArrayList<Integer> for mobile quantities
                mobilePrice    // ArrayList<String> for mobile prices
        );

        // Set up the RecyclerView adapter
        binding.orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderDetailsRecyclerView.setAdapter(adapter);
    }

}
