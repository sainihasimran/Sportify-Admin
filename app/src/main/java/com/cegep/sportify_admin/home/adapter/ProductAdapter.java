package com.cegep.sportify_admin.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.cegep.sportify_admin.ItemClickListener;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductViewHolder> {

    private final Context context;
    private final ItemClickListener<Product> itemClickListener;

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options, Context context, ItemClickListener<Product> itemClickListener) {
        super(options);
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, itemClickListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.bind(model, context);
    }
}
