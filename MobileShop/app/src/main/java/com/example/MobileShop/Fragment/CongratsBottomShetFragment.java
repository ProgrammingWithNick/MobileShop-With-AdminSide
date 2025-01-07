package com.example.MobileShop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.MobileShop.activity.UsersMainActivity;
import com.example.MobileShop.databinding.FragmentCongratsBottomShetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CongratsBottomShetFragment extends BottomSheetDialogFragment {
    private FragmentCongratsBottomShetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCongratsBottomShetBinding.inflate(inflater, container, false);

        // Set up GoHome button click listener
        binding.GoHome.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), UsersMainActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
