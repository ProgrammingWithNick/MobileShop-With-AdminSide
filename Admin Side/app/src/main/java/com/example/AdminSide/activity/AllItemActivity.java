package com.example.AdminSide.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.AdminSide.Adapter.MenuItemAdapter;
import com.example.AdminSide.Model.AllMenu;
import com.example.AdminSide.databinding.ActivityAllItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllItemActivity extends AppCompatActivity {
    private ActivityAllItemBinding binding;
    private final ArrayList<AllMenu> MenuList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        // Retrieve data and set up RecyclerView
        retrieveMenuItem();

        // Apply edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void retrieveMenuItem() {
        databaseReference.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    try {
                        AllMenu menuItem = childSnapshot.getValue(AllMenu.class);
                        if (menuItem != null) {
                            MenuList.add(menuItem);
                        }
                    } catch (Exception e) {
                        Log.e("DataParsingError", "Failed to parse menu item", e);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching data", error.toException());
            }
        });
    }

    private void setAdapter() {
        adapter = new MenuItemAdapter(MenuList, this, this::deleteMenuItems);
        binding.MenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.MenuRecyclerView.setAdapter(adapter);
    }

    private void deleteMenuItems(int position) {
        if (position >= 0 && position < MenuList.size()) {
            AllMenu menuItemToDelete = MenuList.get(position);
            String key = menuItemToDelete.getKey();

            if (key == null || key.isEmpty()) {
                Log.e("DeleteError", "Key is null or empty for position: " + position);
                return;
            }

            databaseReference.child("menu").child(key)
                    .removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DatabaseSuccess", "Item deleted successfully");
                        MenuList.remove(position);
                        adapter.notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> Log.e("DatabaseError", "Failed to delete item", e));
        } else {
            Log.e("DeleteError", "Invalid position: " + position);
        }
    }
}
