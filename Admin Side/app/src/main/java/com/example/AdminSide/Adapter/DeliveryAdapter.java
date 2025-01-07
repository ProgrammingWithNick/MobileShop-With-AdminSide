package com.example.AdminSide.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AdminSide.databinding.DeliveryItemBinding;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private final List<String> customerNames;
    private final List<Boolean> moneyStatuses;

    public DeliveryAdapter(List<String> customerNames, List<Boolean> moneyStatuses) {
        this.customerNames = customerNames;
        this.moneyStatuses = moneyStatuses;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeliveryViewHolder(DeliveryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        String customerName = customerNames.get(position);
        Boolean moneyStatus = moneyStatuses.get(position);
        holder.bind(customerName, moneyStatus);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final DeliveryItemBinding binding;

        public DeliveryViewHolder(DeliveryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String customerName, Boolean moneyStatus) {
            // Set customer name
            binding.customerName.setText(customerName);

            // Set status text and determine color
            String statusText = moneyStatus ? "Received" : "Not Received";
            binding.MoneyStatus.setText(statusText);

            // Determine the color based on the status
            int color = moneyStatus ? Color.GREEN : Color.RED;

            // Apply color to text and background
            binding.MoneyStatus.setTextColor(color);
            binding.StatusColor.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}
