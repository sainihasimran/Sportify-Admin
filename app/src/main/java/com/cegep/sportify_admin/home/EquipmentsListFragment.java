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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EquipmentsListFragment extends Fragment implements ItemClickListener<Equipment> {

    private RecyclerView recyclerView;

    private DatabaseReference equipmentsReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipments_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        // TODO: 2021-06-23 Add equipment id
        equipmentsReference = FirebaseDatabase.getInstance().getReference("Brand").child("Equipments");

        attachRecyclerAdapter();

        view.findViewById(R.id.add_equipment_button).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEquipmentActivity.class);
            requireActivity().startActivity(intent);
        });
    }

    private void attachRecyclerAdapter() {
        FirebaseRecyclerOptions<Equipment> options = new FirebaseRecyclerOptions.Builder<Equipment>()
                .setQuery(getQuery(equipmentsReference), Equipment.class)
                .setLifecycleOwner(this)
                .build();

        EquipmentsAdapter productsAdapter = new EquipmentsAdapter(options, requireContext(), this);
        recyclerView.setAdapter(productsAdapter);
    }

    private Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.orderByPriority();
    }

    @Override
    public void onClick(Equipment obj) {
    }
}
