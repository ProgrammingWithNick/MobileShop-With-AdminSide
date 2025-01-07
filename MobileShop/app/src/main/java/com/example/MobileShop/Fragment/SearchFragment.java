package com.example.MobileShop.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.MobileShop.Model.MenuItem;
import com.example.MobileShop.adapter.MenuAdapter;
import com.example.MobileShop.databinding.FragmentSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MenuAdapter adapter;
    private FirebaseDatabase database;
    private ArrayList<MenuItem> originalMenus = new ArrayList<>();
    private ArrayList<MenuItem> filteredMenus = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        setupRecyclerView();
        retrieveMenuItems();
        setupSearchView();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        // Set up RecyclerView and adapter
        adapter = new MenuAdapter(filteredMenus, getContext());
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.menuRecyclerView.setAdapter(adapter);
    }

    private void retrieveMenuItems() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference mobileRef = database.getReference().child("menu");

        mobileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                originalMenus.clear();
                for (DataSnapshot mobileSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = mobileSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        originalMenus.add(menuItem);
                    }
                }
                // Initially display all items
                filteredMenus.clear();
                filteredMenus.addAll(originalMenus);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return true;
            }
        });
    }

    private void filterMenuItems(String query) {
        filteredMenus.clear();

        if (query.isEmpty()) {
            // Show all items when query is empty
            filteredMenus.addAll(originalMenus);
        } else {
            // Filter items based on the query
            for (MenuItem item : originalMenus) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredMenus.add(item);
                }
            }
        }

        // Notify the adapter of the changes
        adapter.notifyDataSetChanged();
    }
}
