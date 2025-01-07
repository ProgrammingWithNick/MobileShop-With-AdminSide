package com.example.MobileShop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.MobileShop.databinding.RecentBuyItemBinding;

import java.util.List;

public class RecentBuyAdapter extends RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder> {
    private final List<String> mobileNameList;
    private final List<String> mobilePriceList;
    private final List<String> mobileImageList;
    private final List<Integer> mobileQuantityList;

    public RecentBuyAdapter(List<String> mobileNameList, List<String> mobilePriceList, List<String> mobileImageList, List<Integer> mobileQuantityList) {
        this.mobileNameList = mobileNameList;
        this.mobilePriceList = mobilePriceList;
        this.mobileImageList = mobileImageList;
        this.mobileQuantityList = mobileQuantityList;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecentBuyItemBinding binding = RecentBuyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        String name = mobileNameList.get(position);
        String price = mobilePriceList.get(position);
        String imageUrl = mobileImageList.get(position);
        Integer quantity = mobileQuantityList.get(position);

        holder.bind(name, price, imageUrl, quantity);
    }

    @Override
    public int getItemCount() {
        return mobileNameList.size();
    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder {
        private final RecentBuyItemBinding binding;

        public RecentViewHolder(RecentBuyItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String name, String price, String imageUrl, Integer quantity) {
            binding.mobilename.setText(name);
            binding.mobilePrice.setText(price);
            binding.mobileQuantity.setText(String.valueOf(quantity)); // Convert quantity to String
            Glide.with(binding.mobileimage.getContext()).load(imageUrl).into(binding.mobileimage);
        }
    }
}
