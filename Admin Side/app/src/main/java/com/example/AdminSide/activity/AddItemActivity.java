package com.example.AdminSide.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.AdminSide.Model.AllMenu;
import com.example.AdminSide.R;
import com.example.AdminSide.databinding.ActivityAddItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private String key;
    private String name;
    private String price;
    private String description;
    private Uri imageUri;
    private String Feature;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Set up button listeners
        binding.Additembtn.setOnClickListener(view -> addItem());
        binding.selectImage.setOnClickListener(view -> openGallery());
        binding.backButton.setOnClickListener(view -> finish());

        // Set up image gallery launcher
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
    }

    private void addItem() {
        // Retrieve and trim text inputs
        name = binding.name.getText().toString().trim();
        price = binding.price.getText().toString().trim();
        description = binding.description.getText().toString().trim();
        Feature = binding.ingredient.getText().toString().trim();

        // Check for empty fields
        if (name.isEmpty() || price.isEmpty() || description.isEmpty() || Feature.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void uploadData() {
        DatabaseReference menuRef = database.getReference("menu");
        String newItemKey = menuRef.push().getKey();

        if (newItemKey != null && imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("menu_images/" + newItemKey + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Retrieve download URL of the uploaded image
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(downloadUrl -> {
                                    // Create an AllMenu object and set its properties
                                    AllMenu newItem = new AllMenu();
                                    newItem.setKey(newItemKey);
                                    newItem.setName(name);
                                    newItem.setPrice(price);
                                    newItem.setDescription(description);
                                    newItem.setFeature(Feature);
                                    newItem.setImageUrl(downloadUrl.toString());

                                    // Add the new item to Firebase Database
                                    menuRef.child(newItemKey).setValue(newItem)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Item added to database successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Failed to add item to database", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No image selected or database key missing", Toast.LENGTH_SHORT).show();
        }
    }
}
