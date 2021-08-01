package com.cegep.sportify_admin.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify_admin.ItemClickListener;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.SportifyAdminApp;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.home.adapter.ProductsAdapter;
import com.cegep.sportify_admin.model.Product;
import com.cegep.sportify_admin.model.ProductFilter;
import com.cegep.sportify_admin.product.addProduct.AddProductActivity;
import com.cegep.sportify_admin.product.editProduct.EditProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProductsListFragment extends Fragment implements ItemClickListener<Product> {

    public static Product selectedProduct = null;

    private View emptyView;

    private ProductFilter productFilter = new ProductFilter();

    private List<Product> products = new ArrayList<>();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Product> products = new ArrayList<>();
            for (DataSnapshot productDataSnapshot : snapshot.getChildren()) {
                Product product = productDataSnapshot.getValue(Product.class);
                products.add(product);
            }

            ProductsListFragment.this.products = products;
            showProductList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private ProductsAdapter productsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);

        setupRecyclerView(view);

        Query query = Utils.getProductsReference().orderByChild("adminId").equalTo(SportifyAdminApp.admin.adminId);
        query.addValueEventListener(valueEventListener);

        view.findViewById(R.id.add_product_button).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddProductActivity.class);
            requireActivity().startActivity(intent);
        });
    }

    private void setupRecyclerView(View view) {
        productsAdapter = new ProductsAdapter(requireContext(), this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(productsAdapter);
    }

    @Override
    public void onClick(Product obj) {
        selectedProduct = obj;
        Intent intent = new Intent(requireContext(), EditProductActivity.class);
        intent.putExtra("product_name", obj.getProductName());
        requireActivity().startActivity(intent);
    }

    private void showProductList() {
        Set<Product> filteredProducts = new HashSet<>();
        for (Product product : products) {
            String filterCategory = productFilter.getCategoryFilter();
            String filterSubCategory = productFilter.getSubCategoryFilter();
            if (filterCategory.equals("All") || filterCategory.equals(product.getCategory())) {
                if (filterSubCategory.equals("All") || filterSubCategory.equals(product.getSubCategory())) {
                    filteredProducts.add(product);
                }
            }
        }

        if (productFilter.getOutOfStock() != null) {
            boolean outOfStock = productFilter.getOutOfStock();
            Iterator<Product> iterator = filteredProducts.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                if (product.isOutOfStock() != outOfStock) {
                    iterator.remove();
                }
            }
        }

        emptyView.setVisibility(filteredProducts.isEmpty() ? View.VISIBLE : View.GONE);

        productsAdapter.update(filteredProducts);
    }

    public void handleFilters(ProductFilter productFilter) {
        this.productFilter = productFilter;
        showProductList();
    }
}
