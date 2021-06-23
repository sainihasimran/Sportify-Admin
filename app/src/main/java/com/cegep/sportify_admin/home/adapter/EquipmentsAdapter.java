package com.cegep.sportify_admin.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.cegep.sportify_admin.ItemClickListener;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.model.Equipment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EquipmentsAdapter extends FirebaseRecyclerAdapter<Equipment, EquipmentViewHolder> {

    private final Context context;
    private final ItemClickListener<Equipment> itemClickListener;

    public EquipmentsAdapter(@NonNull FirebaseRecyclerOptions<Equipment> options, Context context, ItemClickListener<Equipment> itemClickListener) {
        super(options);
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
    protected void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position, @NonNull Equipment model) {
        holder.bind(model, context);
    }
}
