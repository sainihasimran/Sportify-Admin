package com.cegep.sportify_admin.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.sportify_admin.ItemClickListener;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.Product;

class ProductViewHolder extends RecyclerView.ViewHolder {

    private final ImageView productImageView;
    private final TextView productNameTextView;
    private final TextView productPriceTextView;

    private final ImageView saleBgImageView;
    private final TextView saleTextView;

    private final TextView outOfStockOverlay;

    private Product product;

    public ProductViewHolder(@NonNull View itemView, ItemClickListener<Product> itemClickListener) {
        super(itemView);

        itemView.setOnClickListener(v -> itemClickListener.onClick(product));

        productImageView = itemView.findViewById(R.id.product_image);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productPriceTextView = itemView.findViewById(R.id.product_price);

        saleBgImageView = itemView.findViewById(R.id.sale_bg);
        saleTextView = itemView.findViewById(R.id.sale_text);

        outOfStockOverlay = itemView.findViewById(R.id.out_of_stock_overlay);
    }

    void bind(Product product, Context context) {
        this.product = product;
        if (!product.isOutOfStock()) {
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                Glide.with(context)
                        .load(product.getImages().get(0))
                        .centerCrop()
                        .into(productImageView);
            } else {
                Glide.with(context)
                        .load(R.drawable.no_image_bg)
                        .into(productImageView);
            }
        } else {
            productImageView.setImageDrawable(null);
        }

        productNameTextView.setText(product.getProductName());
        productPriceTextView.setText("$" + product.getPrice());

        boolean isOutOfStock = product.isOutOfStock();
        if (product.getSale() > 0 && !isOutOfStock) {
            saleTextView.setText(product.getSale() + "%\noff");
            saleTextView.setVisibility(View.VISIBLE);
            saleBgImageView.setVisibility(View.VISIBLE);
        } else {
            saleTextView.setVisibility(View.GONE);
            saleBgImageView.setVisibility(View.GONE);
        }

        if (isOutOfStock) {
            outOfStockOverlay.setVisibility(View.VISIBLE);
        } else {
            outOfStockOverlay.setVisibility(View.GONE);
        }
    }
}
