package com.example.AdminSide.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.AdminSide.Model.AdminModel;
import com.example.AdminSide.R;
import com.example.AdminSide.activity.AdminMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private EditText editTextEmail, editTextPassword, editTextUsername;
    private CheckBox showpsw;
    private Button buttonSignup;
    private FirebaseAuth mAuth;
    private TextView textView;
    private DatabaseReference database;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getContext(), AdminMainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        editTextUsername = view.findViewById(R.id.username);
        showpsw = view.findViewById(R.id.box);
        buttonSignup = view.findViewById(R.id.btn_signup);
        textView = view.findViewById(R.id.loginNow);

        textView.setOnClickListener(v -> {
            Fragment login = new LoginFragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.main, login).commit();
            showToast("Navigate to Login");
        });

        showpsw.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            editTextPassword.setSelection(editTextPassword.length());  // Set cursor to end
        });

        buttonSignup.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                showToast("Enter Email");
                return;
            }

            if (TextUtils.isEmpty(username)) {
                showToast("Enter Your Name");
                return;
            }

            if (!isValidEmail(email)) {
                showToast("Invalid Email Format");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                showToast("Enter Password");
                return;
            }

            int minLength = 6;  // Minimum password length
            if (password.length() < minLength) {
                showToast("Password must be at least " + minLength + " characters long");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Account Created");
                            saveUserData(username, email, password);
                            Intent intent = new Intent(getActivity(), AdminMainActivity.class);
                            startActivity(intent);
//                            getActivity().finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                showToast("Email is already in use");
                            } else {
                                showToast("Authentication failed: " + task.getException().getMessage());
                            }
                        }
                    });
        });

        return view;
    }

    private void saveUserData(String username, String email, String password) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create an instance of AdminModel with the given data
        AdminModel admin = new AdminModel(username, email, password);

        // Save the AdminModel object to Firebase
        database.child("admin").child(userId).setValue(admin)
                .addOnSuccessListener(aVoid -> showToast("User data saved"))
                .addOnFailureListener(e -> showToast("Failed to save user data: " + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
}
