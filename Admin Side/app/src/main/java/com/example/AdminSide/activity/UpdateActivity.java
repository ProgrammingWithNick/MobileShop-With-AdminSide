package com.example.AdminSide.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.AdminSide.Model.AllMenu;
import com.example.AdminSide.R;
import com.example.AdminSide.databinding.ActivityUpdateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateActivity extends AppCompatActivity {
    private ActivityUpdateBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private String key;
    private String existingImageUrl;
    private Uri imageUri;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adjust padding for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("menu")) {
            AllMenu menu = (AllMenu) intent.getSerializableExtra("menu");
            if (menu != null) {
                populateFields(menu); // Populate the fields
            } else {
                Toast.makeText(this, "Failed to load menu data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No menu data received", Toast.LENGTH_SHORT).show();
//            finish();
        }

        // Initialize gallery launcher to pick an image
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imageUri = selectedImageUri;
                            binding.selectedImage.setImageURI(selectedImageUri);
                        }
                    }
                });

        // Set up button listeners
        binding.updateItemBtn.setOnClickListener(view -> updateItem());
        binding.selectImage.setOnClickListener(view -> openGallery());
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void populateFields(AllMenu menu) {
        key = menu.getKey();
        binding.name.setText(menu.getName());
        binding.price.setText(menu.getPrice());
        binding.description.setText(menu.getDescription());
        binding.ingredient.setText(menu.getFeature());
        existingImageUrl = menu.getImageUrl();

        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            Glide.with(this).load(existingImageUrl).into(binding.selectedImage);
        } else {
            binding.selectedImage.setImageResource(R.drawable.google); // Placeholder image
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void updateItem() {
        String name = binding.name.getText().toString().trim();
        String price = binding.price.getText().toString().trim();
        String description = binding.description.getText().toString().trim();
        String feature = binding.ingredient.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || description.isEmpty() || feature.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference menuRef = database.getReference("menu").child(key);

        if (imageUri != null) {
            // Upload the new image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("menu_images/" + key + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                            .addOnSuccessListener(downloadUrl -> {
                                updateMenu(menuRef, name, price, description, feature, downloadUrl.toString());
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to get new image URL", Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Use existing image URL if no new image is selected
            updateMenu(menuRef, name, price, description, feature, existingImageUrl);
        }
    }

    private void updateMenu(DatabaseReference menuRef, String name, String price, String description, String feature, String imageUrl) {
        AllMenu updatedMenu = new AllMenu();
        updatedMenu.setKey(key);
        updatedMenu.setName(name);
        updatedMenu.setPrice(price);
        updatedMenu.setDescription(description);
        updatedMenu.setFeature(feature);
        updatedMenu.setImageUrl(imageUrl);

        menuRef.setValue(updatedMenu)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
                });
    }
}
