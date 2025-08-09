package com.example.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PendingProductsAdapter extends RecyclerView.Adapter<PendingProductsAdapter.ProductViewHolder> {
    private final List<Product> products;
    private final OnProductApprovedListener listener;

    public interface OnProductApprovedListener {
        void onProductApproved(Product product);
    }

    public PendingProductsAdapter(List<Product> products, OnProductApprovedListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        final ImageView productThumb;
        final TextView productNameText, productPriceText,
                productCategoryText, productBrandText,
                productDiscountText, productDescriptionText;
        final Button acceptButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productThumb = itemView.findViewById(R.id.productThumb);
            productNameText = itemView.findViewById(R.id.productNameText);
            productPriceText = itemView.findViewById(R.id.productPriceText);
            productCategoryText = itemView.findViewById(R.id.productCategoryText);
            productBrandText = itemView.findViewById(R.id.productBrandText);
            productDiscountText = itemView.findViewById(R.id.productDiscountText);
            productDescriptionText = itemView.findViewById(R.id.productDescriptionText);
            acceptButton = itemView.findViewById(R.id.acceptButton);
        }

        void bind(Product product) {
            Bitmap bmp = product.getFirstImageBitmap();
            if (bmp != null) {
                // Load decoded Bitmap into Glide
                Glide.with(productThumb.getContext())
                        .load(bmp)
                        .centerCrop()
                        .into(productThumb);
            } else {
                productThumb.setImageResource(R.drawable.ic_agriculture);
            }

            productNameText.setText(product.getName());
            productPriceText.setText(String.format("Price: ₹%.2f", product.getPrice()));
            productCategoryText.setText("Category: " + product.getCategory());
            productBrandText.setText("Brand: " + product.getBrand());
            productDiscountText.setText("Discount: " + product.getDiscount() + "%");
            productDescriptionText.setText("Description: " + product.getDescription());

            acceptButton.setBackgroundTintList(
                    itemView.getContext().getResources()
                            .getColorStateList(android.R.color.holo_red_light)
            );
            acceptButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductApproved(product);
                    acceptButton.setBackgroundTintList(
                            itemView.getContext().getResources()
                                    .getColorStateList(android.R.color.holo_green_light)
                    );
                }
            });
        }

    }
}
