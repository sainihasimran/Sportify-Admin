package com.cegep.sportify_admin.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify_admin.Constants;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.ProductFilter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductFilterFragment extends BottomSheetDialogFragment {

    private final ProductFilter productFilter = new ProductFilter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCategoriesSpinner(view);
        setupSubCategoriesSpinner(view);
        setupOutOfStockChooser(view);
        setupApplyButtonClick(view);
    }

    private void setupCategoriesSpinner(View view) {
        List<String> categories = new ArrayList<>(Arrays.asList(Constants.CATEGORIES));
        categories.add(0, "All");
        Spinner categoryChooser = view.findViewById(R.id.category_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categoryChooser.setAdapter(adapter);
        categoryChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productFilter.setCategoryFilter(categories.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSubCategoriesSpinner(View view) {
        List<String> subcategories = new ArrayList<>(Arrays.asList(Constants.SUB_CATEGORIES));
        subcategories.add(0, "All");
        Spinner subCategoryChooser = view.findViewById(R.id.sub_category_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, subcategories);
        subCategoryChooser.setAdapter(adapter);
        subCategoryChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productFilter.setSubCategoryFilter(subcategories.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupOutOfStockChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.out_of_stock_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.out_of_stock_none_button) {
                productFilter.setOutOfStock(null);
            } else if (checkedId == R.id.out_of_stock_yes_button) {
                productFilter.setOutOfStock(true);
            } else if (checkedId == R.id.out_of_stock_no_button) {
                productFilter.setOutOfStock(false);
            }
        });
    }

    private void setupApplyButtonClick(View view) {
        view.findViewById(R.id.apply_button).setOnClickListener(v -> {
            Fragment fragment = getTargetFragment();
            if (fragment instanceof ProductFilterListener) {
                ((ProductFilterListener) fragment).onProductFilterSelected(productFilter);
            }
            dismiss();
        });
    }
}
