package com.cegep.sportify_admin.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.Product;

class ProductViewHolder extends RecyclerView.ViewHolder {

    private final ImageView productImageView;
    private final TextView productNameTextView;
    private final TextView productPriceTextView;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView = itemView.findViewById(R.id.product_image);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productPriceTextView = itemView.findViewById(R.id.product_price);
    }

    void bind(Product product, Context context) {
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(context)
                    .load(product.getImages().get(0))
                    .into(productImageView);
        }

        productNameTextView.setText(product.getProductName());
        productPriceTextView.setText("$" + product.getPrice());
    }
}
