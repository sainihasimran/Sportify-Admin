package com.cegep.sportify_admin.equipment.editEquipment;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.SportifyAdminApp;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.home.EquipmentsListFragment;
import com.cegep.sportify_admin.model.Equipment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEquipmentFragment extends Fragment {

    private Equipment equipment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipment = new Equipment(EquipmentsListFragment.selectedEquipment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_equipment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupEquipmentNameInput(view);
        setupEquipmentPriceInput(view);
        setupSaleInput(view);
        setupEquipmentDescriptionInput(view);
        setupStockInput(view);
        setupEditButtonClick(view);
    }

    private void setupEquipmentNameInput(View view) {
        EditText nameEditText = view.findViewById(R.id.name_editText);
        nameEditText.setText(equipment.getEquipmentName());
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setEquipmentName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupEquipmentPriceInput(View view) {
        String priceStr = String.format("%.2f", equipment.getPrice());
        EditText priceEditText = view.findViewById(R.id.price_editText);
        priceEditText.setText(priceStr);
        Selection.setSelection(priceEditText.getText(), priceEditText.length());
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setEquipmentPrice(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSaleInput(View view) {
        EditText saleEditText = view.findViewById(R.id.sale_editText);
        saleEditText.setText(String.valueOf(equipment.getSale()));
        saleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    equipment.setSale(0);
                } else {
                    equipment.setSale(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupEquipmentDescriptionInput(View view) {
        EditText descriptionEditText = view.findViewById(R.id.description_editText);
        descriptionEditText.setText(equipment.getDescription());
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupStockInput(View view) {
        EditText stockEditText = view.findViewById(R.id.stock_text);
        stockEditText.setText(String.valueOf(equipment.getStock()));
        stockEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    equipment.setStock(0);
                } else {
                    equipment.setStock(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupEditButtonClick(View view) {
        Button editEquipmentButton = view.findViewById(R.id.edit_equipment_button);
        editEquipmentButton.setOnClickListener(v -> {
            if (equipment.isValid(requireContext())) {
                editEquipment();
            }
        });
    }

    private void editEquipment() {
        DatabaseReference equipmentsReference = Utils.getEquipmentsReference();
        DatabaseReference equipmentReference = equipmentsReference.child(equipment.getEquipmentId());
        if (equipment.isOnSale()) {
            float salePrice = equipment.getPrice() - ((equipment.getPrice() * equipment.getSale()) / 100);
            equipment.setSalePrice(salePrice);
        }
        equipmentReference.setValue(equipment);
        requireActivity().finish();
    }
}