package com.example.MobileShop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.MobileShop.Model.CartItems;
import com.example.MobileShop.activity.DetailsActivity;
import com.example.MobileShop.databinding.CartItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<CartItems> cartItems;
    private final Context context;
    private final FirebaseAuth auth;
    private final DatabaseReference cartItemsReference;

    public CartAdapter(List<CartItems> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            cartItemsReference = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("CartItems");
        } else {
            Toast.makeText(context, "Please log in to manage your cart.", Toast.LENGTH_SHORT).show();
            cartItemsReference = null;
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItems cartItem = cartItems.get(position);

        holder.binding.cartName.setText(cartItem.getName());
        holder.binding.cartItemPrice.setText(cartItem.getPrice());
        holder.binding.quantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context).load(cartItem.getImage()).into(holder.binding.cartImage);

        holder.binding.minusButton.setOnClickListener(view -> decrementQuantity(position));
        holder.binding.pluseButton.setOnClickListener(view -> incrementQuantity(position));
        holder.binding.detelebutton.setOnClickListener(view -> deleteItem(position));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("MenuItemName", cartItem.getName());
            intent.putExtra("MenuItemImage", cartItem.getImage());
            intent.putExtra("MenuItemDescription", cartItem.getDescription());
            intent.putExtra("MenuItemFeature", cartItem.getFeature());
            intent.putExtra("MenuItemPrice", cartItem.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void incrementQuantity(int position) {
        CartItems item = cartItems.get(position);
        if (item.getQuantity() < 10) {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            updateQuantityInDatabase(item);
        }
    }

    private void decrementQuantity(int position) {
        CartItems item = cartItems.get(position);
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            notifyItemChanged(position);
            updateQuantityInDatabase(item);
        }
    }

    private void deleteItem(int position) {
        CartItems cartItem = cartItems.get(position);
        String uniqueKey = cartItem.getKey();
        if (cartItemsReference != null && uniqueKey != null) {
            cartItemsReference.child(uniqueKey).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.e("CartAdapter", "Failed to delete item", e));
        }
    }

    private void updateQuantityInDatabase(CartItems item) {
        if (cartItemsReference != null && item.getKey() != null) {
            cartItemsReference.child(item.getKey()).child("quantity").setValue(item.getQuantity());
        }
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
