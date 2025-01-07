package com.example.AdminSide.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.AdminSide.R;
import com.example.AdminSide.databinding.PendingOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {
    private final List<String> customerNames;
    private final List<String> quantities;
    private final List<String> images;
    private final List<Boolean> isAcceptedList;
    private final Context context;
    private final OnItemClicked itemClicked;

    // Constructor accepting ArrayLists, Context, and OnItemClicked
    public PendingOrderAdapter(ArrayList<String> customerNames, ArrayList<String> quantities, ArrayList<String> images, Context context, OnItemClicked itemClicked) {
        this.customerNames = customerNames;
        this.quantities = quantities;
        this.images = images;
        this.context = context;
        this.itemClicked = itemClicked;
        this.isAcceptedList = new ArrayList<>();

        // Initialize each item’s isAccepted status to false
        for (int i = 0; i < customerNames.size(); i++) {
            isAcceptedList.add(false);
        }
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PendingOrderItemBinding binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        holder.bind(
                customerNames.get(position),
                quantities.get(position),
                images.get(position),
                isAcceptedList.get(position),
                position
        );
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    // ViewHolder class
    public class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private final PendingOrderItemBinding binding;

        public PendingOrderViewHolder(PendingOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String customerName, String quantity, String imageUrl, boolean isAccepted, int position) {
            binding.moblieName.setText(customerName);
            binding.Pricetextview.setText(quantity);

                Glide.with(context)
                        .load(Uri.parse(imageUrl))
                        .into(binding.orderImages);

//            List<String> userImageUrls = new ArrayList<>();
//            userImageUrls.add("data:image/jpeg;base64,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");  // Image for first user
//            // Ensure the position matches the image URL list size
//            if (position < userImageUrls.size()) {
//                Glide.with(context)
//                        .load(userImageUrls.get(position))  // Load image based on position
//                        .into(binding.orderImages);
//            } else {
//                // If no image URL is available for the position, use a placeholder or default image
//                Glide.with(context)
//                        .load(R.drawable.usernew)  // Replace with your default image resource
//                        .into(binding.orderImages);
//            }

            // Set button text based on acceptance status
            binding.mobliePrice.setText(isAccepted ? "Dispatch" : "Accept");

            // Handle Accept/Dispatch logic
            binding.mobliePrice.setOnClickListener(view -> {
                if (!isAcceptedList.get(position)) {
                    // Mark as accepted
                    isAcceptedList.set(position, true);
                    binding.mobliePrice.setText("Dispatch");
                    Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                    itemClicked.onItemAcceptClickListener(position);
                } else {
                    // Dispatch the order and notify listener
                    customerNames.remove(position);
                    quantities.remove(position);
                    images.remove(position);
                    isAcceptedList.remove(position);
                    notifyItemRemoved(position);
//                    itemClicked.onItemClickListener(position);
                    Toast.makeText(context, "Order Dispatched", Toast.LENGTH_SHORT).show();
                    itemClicked.onItemDispatchClickListener(position);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClicked.onItemClickListener(position);
                }
            });
        }
    }

    // Interface for handling item clicks
    public interface OnItemClicked {
        void onItemClickListener(int position);
        void onItemAcceptClickListener(int position);
        void onItemDispatchClickListener(int position);
    }
}
