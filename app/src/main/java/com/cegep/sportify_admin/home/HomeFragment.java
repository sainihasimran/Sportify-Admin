package com.cegep.sportify_admin.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.cegep.sportify_admin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProductsFragment();

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_clothing) {
                showProductsFragment();
                return true;
            }

            if (item.getItemId() == R.id.action_equipment) {
                showEquipmentsFragment();
                return true;
            }

            return false;
        });
    }

    private void showProductsFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ProductsListFragment())
                .commit();
    }

    private void showEquipmentsFragment() {

    }
}
