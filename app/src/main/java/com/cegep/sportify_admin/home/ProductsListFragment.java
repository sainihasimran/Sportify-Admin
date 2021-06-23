package com.cegep.sportify_admin.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.home.adapter.ProductAdapter;
import com.cegep.sportify_admin.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProductsListFragment extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference productsReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        // TODO: 2021-06-23 Add product id
        productsReference = FirebaseDatabase.getInstance().getReference("Brand").child("Products");

        attachRecyclerAdapter();
    }

    private void attachRecyclerAdapter() {
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(getQuery(productsReference), Product.class)
                .setLifecycleOwner(this)
                .build();

        ProductAdapter productAdapter = new ProductAdapter(options, requireContext());
        recyclerView.setAdapter(productAdapter);
    }

    private Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.orderByPriority();
    }
}
