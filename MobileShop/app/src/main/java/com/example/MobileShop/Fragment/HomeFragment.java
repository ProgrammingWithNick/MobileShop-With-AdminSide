package com.example.MobileShop.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.example.MobileShop.Model.MenuItem;
import com.example.MobileShop.R;
import com.example.MobileShop.adapter.PopularAdapter;
import com.example.MobileShop.auth.LoginFragment;
import com.example.MobileShop.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private List<MenuItem> menuItems = new ArrayList<>();
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Handle logout button click
//        binding.logout.setOnClickListener(view -> {
//            if (auth != null) {
//                auth.signOut();
//
//                // Navigate to LoginFragment
//                Fragment loginFragment = new LoginFragment();
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragmentContainerView2, loginFragment).commit();
//            }
//        });

        // Set up "View All" button
        binding.viewAllMenu.setOnClickListener(view -> {
            MenuBottomSheetFragment bottomSheetDialog = new MenuBottomSheetFragment();
            bottomSheetDialog.show(getParentFragmentManager(), "Test");
        });

        // Set up the image slider
        setupImageSlider();

        // Fetch menu items from Firebase
        retrieveMenuItems();

        // Set up RecyclerView
        binding.populerRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.populerRecyclerview.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        return binding.getRoot();
    }

    private void setupImageSlider() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.iphone16128gb, ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.s23, ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.note40pro, ScaleTypes.CENTER_INSIDE));
        binding.imageSlider.setImageList(imageList);
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
                // Ensure adapter is set if the fragment is attached
                if (isAdded() && getContext() != null) {
                    setPopularAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error retrieving menu items", error.toException());
            }
        });
    }

    private void setPopularAdapter() {
        // Get a random subset of menuItems for popular items display
        List<MenuItem> popularItems = getRandomSubset(menuItems, 6);
        PopularAdapter adapter = new PopularAdapter(popularItems, requireContext());
        binding.populerRecyclerview.setAdapter(adapter);
    }

    // Helper function to get a random subset of MenuItem
    private List<MenuItem> getRandomSubset(List<MenuItem> list, int subsetSize) {
        Collections.shuffle(list);
        return list.subList(0, Math.min(subsetSize, list.size()));
    }
}
