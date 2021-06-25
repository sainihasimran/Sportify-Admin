package com.cegep.sportify_admin.equipment.editEquipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.home.EquipmentsListFragment;
import com.cegep.sportify_admin.model.Equipment;

public class EditEquipmentFragment extends Fragment {

    private Equipment equipment = EquipmentsListFragment.selectedEquipment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_equipment, container, false);
    }
}
