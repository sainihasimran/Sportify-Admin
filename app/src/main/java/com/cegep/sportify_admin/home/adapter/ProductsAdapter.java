package com.cegep.sportify_admin.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify_admin.ItemClickListener;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.Product;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private final Context context;
    private final ItemClickListener<Product> itemClickListener;

    private final List<Product> products = new ArrayList<>();

    public ProductsAdapter(Context context, ItemClickListener<Product> itemClickListener) {
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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), context);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(Collection<Product> products) {
        List<Product> newProducts = new ArrayList<>(products);
        Collections.sort(newProducts);
        Collections.reverse(newProducts);

        this.products.clear();
        this.products.addAll(newProducts);
        notifyDataSetChanged();
    }
}
