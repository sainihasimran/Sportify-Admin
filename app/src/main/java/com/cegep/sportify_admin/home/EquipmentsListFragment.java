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
import com.cegep.sportify_admin.addEquipment.AddEquipmentActivity;
import com.cegep.sportify_admin.home.adapter.EquipmentsAdapter;
import com.cegep.sportify_admin.model.Equipment;
import com.cegep.sportify_admin.model.EquipmentFilter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquipmentsListFragment extends Fragment implements ItemClickListener<Equipment> {

    private View emptyView;

    private EquipmentFilter equipmentFilter = new EquipmentFilter();

    private List<Equipment> equipments = new ArrayList<>();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Equipment> equipments = new ArrayList<>();
            for (DataSnapshot equipmentDataSnapshot : snapshot.getChildren()) {
                Equipment equipment = equipmentDataSnapshot.getValue(Equipment.class);
                equipments.add(equipment);
            }

            EquipmentsListFragment.this.equipments = equipments;
            showEquipmentList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private EquipmentsAdapter equipmentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipments_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);

        setupRecyclerView(view);
        // TODO: 2021-06-23 Add equipment id
        DatabaseReference equipmentsReference = FirebaseDatabase.getInstance().getReference("Brand").child("Equipments");
        equipmentsReference.addValueEventListener(valueEventListener);

        view.findViewById(R.id.add_equipment_button).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEquipmentActivity.class);
            requireActivity().startActivity(intent);
        });
    }

    private void setupRecyclerView(View view) {
        equipmentsAdapter = new EquipmentsAdapter(requireContext(), this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(equipmentsAdapter);
    }

    @Override
    public void onClick(Equipment obj) {
    }

    private void showEquipmentList() {
        Set<Equipment> filteredEquipments = new HashSet<>();
        for (Equipment equipment : equipments) {
            if (equipmentFilter.getSportFilter().equals("All") || equipmentFilter.getSportFilter().equalsIgnoreCase(equipment.getSport())) {
                filteredEquipments.add(equipment);
            }
        }

        emptyView.setVisibility(filteredEquipments.isEmpty() ? View.VISIBLE : View.GONE);

        equipmentsAdapter.update(filteredEquipments);
    }

    public void handleFilters(EquipmentFilter equipmentFilter) {
        this.equipmentFilter = equipmentFilter;
        showEquipmentList();
    }
}
