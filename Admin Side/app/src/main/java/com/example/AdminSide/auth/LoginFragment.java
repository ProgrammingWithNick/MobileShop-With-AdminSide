package com.example.AdminSide.auth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.AdminSide.R;
import com.example.AdminSide.activity.AdminMainActivity;
import com.example.AdminSide.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Override
    public void onStart() {
        super.onStart();
        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            Toast.makeText(getContext(), "Authentication service not available", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getContext(), AdminMainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate and bind the layout
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Handle forgot password click
        binding.forgot.setOnClickListener(view -> {
            Fragment forgotPasswordFragment = new ForgotPasswordFragment();
            FragmentTransaction fm = requireActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.main, forgotPasswordFragment).commit();
        });

        // Handle sign-up click
        binding.signNow.setOnClickListener(view -> {
            Fragment signUpFragment = new SignUpFragment();
            FragmentTransaction fm = requireActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.main, signUpFragment).commit();
            Toast.makeText(getActivity(), "Sign Up", Toast.LENGTH_SHORT).show();
        });

        // Handle show/hide password functionality
        binding.box.setOnCheckedChangeListener((CompoundButton compoundButton, boolean isChecked) -> {
            if (isChecked) {
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            // Set cursor to the end of the password field
            binding.password.setSelection(binding.password.length());
        });

        // Handle login button click
        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(email)) {
                Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < MIN_PASSWORD_LENGTH) {
                Toast.makeText(getActivity(), "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long", Toast.LENGTH_SHORT).show();
                return;
            }


            // Sign in with Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login success
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), AdminMainActivity.class));
                            requireActivity().finish();
                        } else {
                            // Login failure
                            showToast("Your Email and password do not match. Please check details.");
                        }
                    });
        });

        return binding.getRoot();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
}
