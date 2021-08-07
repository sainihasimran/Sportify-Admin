package com.cegep.sportify_admin.product.editProduct;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.SportifyAdminApp;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.home.ProductsListFragment;
import com.cegep.sportify_admin.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProductFragment extends Fragment {

    private Product product;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = new Product(ProductsListFragment.selectedProduct);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupProductNameInput(view);
        setupPriceInput(view);
        setupSaleEditText(view);
        setupDescription(view);
        setupSizesInput(view);
        setupEditButtonClick(view);
    }

    private void setupProductNameInput(View view) {
        EditText nameEditText = view.findViewById(R.id.name_editText);
        nameEditText.setText(product.getProductName());
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setProductName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPriceInput(View view) {
        String priceStr = String.format("%.2f", product.getPrice());
        EditText priceEditText = view.findViewById(R.id.price_editText);
        priceEditText.setText(priceStr);
        Selection.setSelection(priceEditText.getText(), priceEditText.length());
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setProductPrice(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSaleEditText(View view) {
        String saleStr = String.valueOf(product.getSale());
        EditText saleEditText = view.findViewById(R.id.sale_editText);
        saleEditText.setText(saleStr);
        saleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setSale(0);
                } else {
                    product.setSale(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupDescription(View view) {
        String descriptionStr = product.getDescription();
        EditText descriptionEditText = view.findViewById(R.id.description_editText);
        descriptionEditText.setText(descriptionStr);
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSizesInput(View view) {
        String xSmallStr = String.valueOf(product.getxSmallSize());
        EditText xSmallEditText = view.findViewById(R.id.x_small_text);
        xSmallEditText.setText(xSmallStr);
        xSmallEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setxSmallSize(0);
                } else {
                    product.setxSmallSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String smallStr = String.valueOf(product.getSmallSize());
        EditText smallEditText = view.findViewById(R.id.small_text);
        smallEditText.setText(smallStr);
        smallEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setSmallSize(0);
                } else {
                    product.setSmallSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String mediumStr = String.valueOf(product.getMediumSize());
        EditText mediumEditText = view.findViewById(R.id.medium_text);
        mediumEditText.setText(mediumStr);
        mediumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setMediumSize(0);
                } else {
                    product.setMediumSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String largeStr = String.valueOf(product.getLargeSize());
        EditText largeEditText = view.findViewById(R.id.large_text);
        largeEditText.setText(largeStr);
        largeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setLargeSize(0);
                } else {
                    product.setLargeSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String xLargeStr = String.valueOf(product.getxLargeSize());
        EditText xLargeEditText = view.findViewById(R.id.x_large_text);
        xLargeEditText.setText(xLargeStr);
        xLargeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setxLargeSize(0);
                } else {
                    product.setxLargeSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupEditButtonClick(View view) {
        Button editProductButton = view.findViewById(R.id.edit_product_button);
        editProductButton.setOnClickListener(v -> {
            if (product.isValid(requireContext())) {
                editProduct();
            }
        });
    }

    private void editProduct() {
        DatabaseReference productsReference = Utils.getProductsReference();
        DatabaseReference productReference = productsReference.child(product.getProductId());
        if (product.isOnSale()) {
            float salePrice = product.getPrice() - ((product.getPrice() * product.getSale()) / 100);
            product.setSalePrice(salePrice);
        }
        productReference.setValue(product);
        requireActivity().finish();
    }
}
