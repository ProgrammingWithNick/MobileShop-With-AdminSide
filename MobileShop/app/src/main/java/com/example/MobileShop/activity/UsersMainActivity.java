package com.example.MobileShop.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.MobileShop.R;
import com.example.MobileShop.databinding.ActivityUsersMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UsersMainActivity extends AppCompatActivity {
    private ActivityUsersMainBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and bind the layout using ViewBinding
        binding = ActivityUsersMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FirebaseAuth and DatabaseReference
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch user data if signed in
        fetchAndSetUserName();

        // Handle logout button click
        binding.logout.setOnClickListener(view -> {
            if (auth != null) {
                auth.signOut(); // Sign out user

                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView2);

                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();

                    // Dynamically set the auth_nav graph to the NavController
                    navController.setGraph(R.navigation.auth_nav);

                    // Navigate to loginFragment after logout
                    navController.navigate(R.id.loginFragment);
                } else {
                    Log.e("UsersMainActivity", "NavHostFragment not found.");
                }
            }
        });

        // Initialize NavController and set up BottomNavigationView
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView2);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = binding.bottom;

            // Setup BottomNavigationView with NavController
            NavigationUI.setupWithNavController(bottomNav, navController);

            // Handle BottomNavigationView visibility based on current destination
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (isAuthNavDestination(destination.getId())) {
                    bottomNav.setVisibility(View.GONE); // Hide BottomNavigationView for auth_nav
                } else {
                    bottomNav.setVisibility(View.VISIBLE); // Show BottomNavigationView for users_nav
                }
            });
        } else {
            // Log a warning if NavHostFragment is not found
            Log.w("UsersMainActivity", "NavHostFragment with ID fragmentContainerView2 not found");
        }
    }

    // Fetch and set the user's name if the user is signed in
    private void fetchAndSetUserName() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();
            DatabaseReference userRef = databaseReference.child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        binding.textView4.setText(name != null ? name : "No Name Found");
                    } else {
                        binding.textView4.setText("User not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UsersMainActivity", "Error fetching user data", error.toException());
                    binding.textView4.setText("Error fetching user name");
                }
            });
        } else {
            binding.textView4.setText("No user signed in");
        }
    }

    // Check if the current destination ID belongs to the auth_nav graph
    private boolean isAuthNavDestination(int destinationId) {
        return destinationId == R.id.firstFragment
                || destinationId == R.id.loginFragment
                || destinationId == R.id.signUpFragment
                || destinationId == R.id.forgotPasswordFragment;
    }
}
