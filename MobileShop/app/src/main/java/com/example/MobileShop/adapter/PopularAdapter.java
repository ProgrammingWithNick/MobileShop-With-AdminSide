package com.example.MobileShop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.MobileShop.Model.MenuItem;
import com.example.MobileShop.activity.DetailsActivity;
import com.example.MobileShop.databinding.PopulerItemBinding;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private final List<MenuItem> items; // List of MenuItem objects
    private final Context context;

    public PopularAdapter(List<MenuItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PopulerItemBinding binding = PopulerItemBinding.inflate(inflater, parent, false);
        return new PopularViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.bind(item);

        // Set up the click listener
        holder.itemView.setOnClickListener(view -> {
            // Navigate to DetailsActivity with MenuItem data
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("MenuItemName", item.getName());
            intent.putExtra("MenuItemDescription", item.getDescription());
            intent.putExtra("MenuItemFeature", item.getFeature());
            intent.putExtra("MenuItemPrice", item.getPrice());
            intent.putExtra("MenuItemImage", item.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private final PopulerItemBinding binding;

        public PopularViewHolder(PopulerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MenuItem item) {
            binding.mobilename.setText(item.getName());
            binding.mobilePrice.setText(item.getPrice());
            Glide.with(context)
                    .load(item.getImageUrl()) // Load image from URL using Glide
                    .into(binding.mobileimage);
        }
    }
}
