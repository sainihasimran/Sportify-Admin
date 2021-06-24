package com.cegep.sportify_admin.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.EquipmentFilter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class EquipmentFilterFragment extends BottomSheetDialogFragment {

    private final EquipmentFilter equipmentFilter = new EquipmentFilter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSportChooser(view);
        setupApplyButtonClick(view);
    }

    private void setupSportChooser(View view) {
        Spinner sportsChooser = view.findViewById(R.id.sport_chooser);
        final List<String> sports = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Brand").child("Sports").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(requireContext(), "Failed to load filters", Toast.LENGTH_SHORT).show();
            } else {
                DataSnapshot result = task.getResult();
                if (result != null) {
                    Object value = result.getValue();
                    if (value instanceof List) {
                        List<String> remoteSports = new ArrayList<>((List<String>) value);
                        for (String remoteSport : remoteSports) {
                            sports.add(Character.toUpperCase(remoteSport.charAt(0)) + remoteSport.substring(1));
                        }
                    }
                }

                sports.add(0, "All");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports);
                sportsChooser.setAdapter(adapter);
            }
        });

        sportsChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equipmentFilter.setSportFilter(sports.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupApplyButtonClick(View view) {
        view.findViewById(R.id.apply_button).setOnClickListener(v -> {
            Fragment fragment = getTargetFragment();
            if (fragment instanceof EquipmentFilterListener) {
                ((EquipmentFilterListener) fragment).onEquipmentFilterSelected(equipmentFilter);
            }
            dismiss();
        });
    }
}
