package com.example.AdminSide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.AdminSide.databinding.OrderDetailItemsBinding;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private final Context context;
    private final ArrayList<String> mobileNames;  // ArrayList<String> for mobile names
    private final ArrayList<String> mobileImages; // ArrayList<String> for mobile images
    private final ArrayList<Integer> mobileQuantities; // ArrayList<Integer> for quantities
    private final ArrayList<String> mobilePrices; // ArrayList<String> for mobile prices

    // Constructor to initialize adapter data
    public OrderDetailsAdapter(Context context, ArrayList<String> mobileNames, ArrayList<String> mobileImages,
                               ArrayList<Integer> mobileQuantities, ArrayList<String> mobilePrices) {
        this.context = context;
        this.mobileNames = mobileNames;
        this.mobileImages = mobileImages;
        this.mobileQuantities = mobileQuantities;
        this.mobilePrices = mobilePrices;
    }


    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        OrderDetailItemsBinding binding = OrderDetailItemsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new OrderDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        // Bind the data for each item
        holder.bind(mobileNames.get(position), mobileImages.get(position), mobileQuantities.get(position), mobilePrices.get(position));
    }

    @Override
    public int getItemCount() {
        return mobileNames.size();
    }

    // ViewHolder class
    public static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {

        private final OrderDetailItemsBinding binding;

        public OrderDetailsViewHolder(@NonNull OrderDetailItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String name, String imageUrl, int quantity, String price) {
            // Set data to the views
            binding.moblieName.setText(name);
            binding.mobliePrice.setText(price);
            binding.quantity.setText(String.valueOf(quantity));

            // Load image using Glide
            Glide.with(binding.orderImages.getContext())
                    .load(imageUrl)
                    .into(binding.orderImages);
        }
    }
}
