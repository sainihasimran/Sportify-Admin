package com.cegep.sportify_admin.Orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.model.Equipment;
import com.cegep.sportify_admin.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;

    private final List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        Collections.sort(orders);
        Collections.reverse(orders);
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
        Button btndecline = holder.btncancel;

        if (orders.get(0).getStatus().equals("pending")) {
            btndelivered.setVisibility(View.VISIBLE);
            btndecline.setVisibility(View.VISIBLE);
        } else {
            btndelivered.setVisibility(View.GONE);
            btndecline.setVisibility(View.GONE);
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

        if (Utils.ORDER_PENDING.equalsIgnoreCase(orders.get(position).getStatus())) {
            Status.setTextColor(context.getResources().getColor(R.color.buttonbg));
        } else if (Utils.ORDER_ACCEPTED.equalsIgnoreCase(orders.get(position).getStatus())) {
            Status.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            Status.setTextColor(context.getResources().getColor(R.color.faded_red));
        }

        btndelivered.setOnClickListener(v -> {

            FirebaseDatabase clientapdb = Utils.getClientDatabase();

            Order order = orders.get(position);
            boolean isProduct = orders.get(position).getProduct() != null;
            boolean isEquipment = orders.get(position).getEquipment() != null;
            DatabaseReference databaseReference = null;
            if (isProduct) {
                databaseReference = Utils.getProductsReference().child(orders.get(position).getProduct().getProductId());
            } else if (isEquipment) {
                databaseReference = Utils.getEquipmentsReference().child(orders.get(position).getEquipment().getEquipmentId());
            }

            if (databaseReference == null) {
                return;
            }

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String stock_inefficient_to_fulfil_order = "Inefficient stock to fulfill order";
                    if (isProduct) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null || product.isOutOfStock()) {
                            Toast.makeText(context, stock_inefficient_to_fulfil_order, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String sizeStr = null;
                        int finalQuantity = 0;
                        if ("xs".equalsIgnoreCase(order.getSize())) {
                            sizeStr = "xSmallSize";
                            finalQuantity = product.getxSmallSize() - order.getQuantity();
                        } else if ("s".equalsIgnoreCase(order.getSize())) {
                            sizeStr = "smallSize";
                            finalQuantity = product.getSmallSize() - order.getQuantity();
                        } else if ("m".equalsIgnoreCase(order.getSize())) {
                            sizeStr = "mediumSize";
                            finalQuantity = product.getMediumSize() - order.getQuantity();
                        } else if ("l".equalsIgnoreCase(order.getSize())) {
                            sizeStr = "largeSize";
                            finalQuantity = product.getLargeSize() - order.getQuantity();
                        } else if ("xl".equalsIgnoreCase(order.getSize())) {
                            sizeStr = "xLargeSize";
                            finalQuantity = product.getxLargeSize() - order.getQuantity();
                        }

                        if (finalQuantity < 0) {
                            Toast.makeText(context, stock_inefficient_to_fulfil_order, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (sizeStr != null) {
                            Utils.getProductsReference().child(orders.get(position).getProduct().getProductId()).child(sizeStr)
                                    .setValue(finalQuantity);
                        }
                    } else {
                        Equipment equipment = snapshot.getValue(Equipment.class);
                        if (equipment == null || equipment.isOutOfStock() || equipment.getStock() < order.getQuantity()) {
                            Toast.makeText(context, stock_inefficient_to_fulfil_order, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Utils.getEquipmentsReference().child(orders.get(position).getEquipment().getEquipmentId()).child("stock")
                                .setValue(equipment.getStock() - order.getQuantity());
                    }

                    DatabaseReference statusReference = clientapdb.getReference("Orders")
                            .child(orders.get(position).getOrderId()).child("status");
                    statusReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            statusReference.setValue(Utils.ORDER_ACCEPTED);
                            orders.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, orders.size());

                            Toast.makeText(context, "Ordered Accepted", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        btndecline.setOnClickListener(v -> {

            FirebaseDatabase clientapdb = Utils.getClientDatabase();

            DatabaseReference databaseReference = clientapdb.getReference("Orders")
                    .child(orders.get(position).getOrderId()).child("status");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.setValue(Utils.ORDER_DECLINED);
                    orders.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, orders.size());

                    Toast.makeText(context, "Order declined", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
        List<Order> newOrders = new ArrayList<>(orders);
        Collections.sort(newOrders);
        Collections.reverse(newOrders);

        this.orders.clear();
        this.orders.addAll(newOrders);
        notifyDataSetChanged();
    }
}
