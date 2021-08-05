package com.cegep.sportify_admin.Orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;

    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ImageView OrderImage = holder.OrderImage;
        TextView OrderName = holder.OrderName;
        TextView OrderPrice = holder.OrderPrice;
        TextView OrderQuantity = holder.OrderQuantity;
        TextView Status = holder.Status;
        Button btndelivered = holder.btndelivered;
        Button btncancel = holder.btncancel;


        if (orders.get(0).getStatus().equals("pending")) {
            btndelivered.setVisibility(View.VISIBLE);
            btncancel.setVisibility(View.VISIBLE);
        } else {
            btndelivered.setVisibility(View.GONE);
            btncancel.setVisibility(View.GONE);
        }

        List<String> images;
        if (orders.get(position).getProduct() != null) {
            images = orders.get(position).getProduct().getImages();
        } else {
            images = orders.get(position).getEquipment().getImages();
        }

        if (images != null && !images.isEmpty()) {
            Glide.with(context)
                    .load(images.get(0))
                    .error(R.drawable.no_image_bg)
                    .into(OrderImage);
        } else {
            OrderImage.setImageResource(R.drawable.no_image_bg);
        }

        if (orders.get(position).getProduct() != null) {
            OrderName.setText(orders.get(position).getProduct().getProductName());
            OrderPrice.setText("$" + String.format("%.2f", orders.get(position).getPrice()));
            OrderQuantity.setText("Quantity: " + orders.get(position).getQuantity());
            Status.setText(Character.toUpperCase(orders.get(position).getStatus().charAt(0)) + orders.get(position).getStatus().substring(1));
        } else {
            OrderName.setText(orders.get(position).getEquipment().getEquipmentName());
            OrderPrice.setText("$" + String.format("%.2f", orders.get(position).getPrice()));
            OrderQuantity.setText("Quantity: " + orders.get(position).getQuantity());
            Status.setText(Character.toUpperCase(orders.get(position).getStatus().charAt(0)) + orders.get(position).getStatus().substring(1));
        }

        if (orders.get(position).getStatus().equals("pending")) {
            Status.setTextColor(context.getResources().getColor(R.color.buttonbg));
        } else if (orders.get(position).getStatus().equals("Accepted")) {
            Status.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            Status.setTextColor(context.getResources().getColor(R.color.faded_red));
        }


        btndelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase clientapdb = Utils.getClientDatabase();

                DatabaseReference databaseReference = clientapdb.getReference("Orders")
                        .child(orders.get(position).getOrderId()).child("status");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String status = (String) dataSnapshot.getValue();

                        databaseReference.setValue("Accepted");
                        orders.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,orders.size());

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase clientapdb = Utils.getClientDatabase();

                DatabaseReference databaseReference = clientapdb.getReference("Orders")
                        .child(orders.get(position).getOrderId()).child("status");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String status = (String) dataSnapshot.getValue();

                        databaseReference.setValue("Declined");
                        orders.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,orders.size());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView OrderImage;
        TextView OrderName;
        TextView OrderPrice;
        TextView OrderQuantity;
        TextView Status;
        Button btndelivered;
        Button btncancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderImage = itemView.findViewById(R.id.OrderImage);
            OrderName = itemView.findViewById(R.id.OrderName);
            OrderPrice = itemView.findViewById(R.id.item_price);
            OrderQuantity = itemView.findViewById(R.id.Quantity);
            Status = itemView.findViewById(R.id.Status);
            btndelivered = itemView.findViewById(R.id.btnaccept);
            btncancel = itemView.findViewById(R.id.btndeclined);
        }
    }

    public void update(Collection<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        notifyDataSetChanged();
    }
}
