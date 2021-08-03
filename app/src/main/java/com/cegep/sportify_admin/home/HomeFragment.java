package com.cegep.sportify_admin.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.cegep.sportify_admin.Orders.OrdersActivity;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.EquipmentFilter;
import com.cegep.sportify_admin.model.ProductFilter;
import com.cegep.sportify_admin.settings.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class HomeFragment extends Fragment implements ProductFilterListener, EquipmentFilterListener {

    private ProductsListFragment productListFragment;
    private EquipmentsListFragment equipmentListFragment;

    private boolean isShowingProducts = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
        showProductsFragment();

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_clothing) {
                isShowingProducts = true;
                showProductsFragment();
                return true;
            }

            if (item.getItemId() == R.id.action_equipment) {
                isShowingProducts = false;
                showEquipmentsFragment();
                return true;
            }

            return false;
        });
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_filter) {
                showFiltersFragment();
                return true;
            } else if (item.getItemId() == R.id.action_profile) {
                Intent intent = new Intent(requireContext(), ProfileActivity.class);
                startActivity(intent);
                return true;
            }else if (item.getItemId() == R.id.action_order) {
                Intent intent = new Intent(requireContext(), OrdersActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    private void showProductsFragment() {
        productListFragment = new ProductsListFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, productListFragment)
                .commit();
    }

    private void showEquipmentsFragment() {
        equipmentListFragment = new EquipmentsListFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, equipmentListFragment)
                .commit();
    }

    private void showFiltersFragment() {
        BottomSheetDialogFragment filterFragment;
        if (isShowingProducts) {
            filterFragment = new ProductFilterFragment();
        } else {
            filterFragment = new EquipmentFilterFragment();
        }
        filterFragment.setTargetFragment(this, 0);
        filterFragment.show(getParentFragmentManager(), null);
    }

    @Override
    public void onProductFilterSelected(ProductFilter filter) {
        if (productListFragment != null) {
            productListFragment.handleFilters(filter);
        }
    }

    @Override
    public void onEquipmentFilterSelected(EquipmentFilter filter) {
        if (equipmentListFragment != null) {
            equipmentListFragment.handleFilters(filter);
        }
    }
}
