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
import com.example.MobileShop.databinding.MenuItemBinding;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItems;
    private final Context context;

    public MenuAdapter(List<MenuItem> menuItems, Context context) {
        this.menuItems = menuItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(MenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private final MenuItemBinding binding;

        public MenuViewHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set the onClickListener for the item
            binding.getRoot().setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MenuItem menuItem = menuItems.get(position);

                    // Start DetailsActivity and pass data
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("MenuItemName", menuItem.getName());
                    intent.putExtra("MenuItemImage", menuItem.getImageUrl());
                    intent.putExtra("MenuItemDescription", menuItem.getDescription());
                    intent.putExtra("MenuItemFeature", menuItem.getFeature());
                    intent.putExtra("MenuItemPrice", menuItem.getPrice());

                    context.startActivity(intent);
                }
            });
        }

        public void bind(MenuItem menuItem) {
            binding.menuMobileName.setText(menuItem.getName());
            binding.manuPrice.setText(menuItem.getPrice());

            // Load the image using Glide
            Glide.with(context)
                    .load(menuItem.getImageUrl())
                    .into(binding.menuImage);
        }
    }
}
