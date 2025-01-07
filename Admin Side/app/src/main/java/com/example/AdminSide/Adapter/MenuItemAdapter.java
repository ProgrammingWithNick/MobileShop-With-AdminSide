package com.example.AdminSide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.AdminSide.Model.AllMenu;
import com.example.AdminSide.activity.UpdateActivity;
import com.example.AdminSide.databinding.ItemItemBinding;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {
    private final List<AllMenu> menuList;
    private final Context context;
    private final OnDeleteClickListener onDeleteClickListener;
    private final int[] itemQuantities;

    // Constructor
    public MenuItemAdapter(List<AllMenu> menuList, Context context, OnDeleteClickListener onDeleteClickListener) {
        this.menuList = menuList;
        this.context = context;
        this.onDeleteClickListener = onDeleteClickListener;
        this.itemQuantities = new int[menuList.size()]; // Initialize item quantities
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuItemViewHolder(ItemItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        AllMenu menuItem = menuList.get(position);

        // Set data to views
        holder.binding.NametextView.setText(menuItem.getName());
        holder.binding.Pricetextview.setText(menuItem.getPrice());
//        holder.binding.QuantitytextView.setText(String.valueOf(itemQuantities[position]));

        // Load image using Glide
        Glide.with(context)
                .load(menuItem.getImageUrl())
                .into(holder.binding.imageResId);


        holder.binding.updatebtn.setOnClickListener(view -> {
            // Pass the AllMenu object to UpdateActivity
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("menu", menuItem); // Passing the specific menuItem
            context.startActivity(intent);
        });

        holder.binding.Deletebtn.setOnClickListener(view -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDelete(position); // Notify delete action
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    // ViewHolder class
    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemBinding binding;

        public MenuItemViewHolder(@NonNull ItemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    // Interface for delete callback
    public interface OnDeleteClickListener {
        void onDelete(int position);
    }
}
