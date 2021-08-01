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
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.model.EquipmentFilter;
import com.cegep.sportify_admin.model.SportWithTeams;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
        setupOutOfStockChooser(view);
        setupApplyButtonClick(view);
    }

    private void setupSportChooser(View view) {
        Spinner sportsChooser = view.findViewById(R.id.sport_chooser);
        final List<String> sports = new ArrayList<>();
        Utils.getSportWithTeamsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SportWithTeams> sportWithTeamsList = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    SportWithTeams sportWithTeams = childSnapshot.getValue(SportWithTeams.class);
                    sportWithTeamsList.add(sportWithTeams);
                }

                for (SportWithTeams sportWithTeams : sportWithTeamsList) {
                    String remoteSport = sportWithTeams.getSport();
                    sports.add(Character.toUpperCase(remoteSport.charAt(0)) + remoteSport.substring(1));
                }

                sports.add(0, "All");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports);
                sportsChooser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void setupOutOfStockChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.out_of_stock_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.out_of_stock_none_button) {
                equipmentFilter.setOutOfStock(null);
            } else if (checkedId == R.id.out_of_stock_yes_button) {
                equipmentFilter.setOutOfStock(true);
            } else if (checkedId == R.id.out_of_stock_no_button) {
                equipmentFilter.setOutOfStock(false);
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
