package com.cegep.sportify_admin.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify_admin.ItemClickListener;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.Equipment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EquipmentsAdapter extends RecyclerView.Adapter<EquipmentViewHolder> {

    private final Context context;
    private final ItemClickListener<Equipment> itemClickListener;

    private final List<Equipment> equipments = new ArrayList<>();

    public EquipmentsAdapter(Context context, ItemClickListener<Equipment> itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        holder.bind(equipments.get(position), context);
    }

    @Override
    public int getItemCount() {
        return equipments.size();
    }

    public void update(Collection<Equipment> equipments) {
        this.equipments.clear();
        this.equipments.addAll(equipments);
        notifyDataSetChanged();
    }
}
