package com.example.AdminSide.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.AdminSide.R;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                Navigation.findNavController(requireView()).navigate(R.id.action_firstFragment_to_loginFragment),
                4000);

        return inflater.inflate(R.layout.fragment_first, container, false);
    }
}