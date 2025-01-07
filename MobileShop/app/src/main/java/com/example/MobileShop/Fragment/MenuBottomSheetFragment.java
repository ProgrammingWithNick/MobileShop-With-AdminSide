package com.example.MobileShop.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import com.example.MobileShop.Model.MenuItem;
import com.example.MobileShop.adapter.MenuAdapter;
import com.example.MobileShop.databinding.FragmentMenuButtomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentMenuButtomSheetBinding binding;
    private List<MenuItem> menuItems = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuButtomSheetBinding.inflate(inflater, container, false);

        binding.buttonBack.setOnClickListener(view -> dismiss());

        // Retrieve menu items from Firebase
        retrieveMenuItems();

        return binding.getRoot();
    }

    private void retrieveMenuItems() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference mobileRef = database.getReference().child("menu");
        mobileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = childSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        menuItems.add(menuItem);
                    }
                }
                // Only set the adapter if the fragment is attached and context is available
                if (isAdded() && getContext() != null) {
                    setAdapter(getContext());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseError", "Error", error.toException());
            }
        });
    }

    private void setAdapter(Context context) {
        MenuAdapter adapter = new MenuAdapter(menuItems, context);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.menuRecyclerView.setAdapter(adapter);
    }
}
