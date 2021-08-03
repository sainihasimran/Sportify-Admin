package com.cegep.sportify_admin.Orders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify_admin.R;

import java.util.ArrayList;
import java.util.List;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private final ImageView OrderImage;
    private final TextView OrderName;
    private final TextView OrderPrice;
    private final TextView OrderQuantity;
    private final TextView Status;
    private  final Button btndelivered;
    private  final Button btncancel;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        OrderImage = itemView.findViewById(R.id.OrderImage);
        OrderName = itemView.findViewById(R.id.OrderName);
        OrderPrice = itemView.findViewById(R.id.item_price);
        OrderQuantity = itemView.findViewById(R.id.Quantity);
        Status = itemView.findViewById(R.id.Status);
        btndelivered = itemView.findViewById(R.id.btnaccept);
        btncancel = itemView.findViewById(R.id.btndeclined);
    }

    void bind(Order order, Context context) {

         List<Order> ordersdeclined ;
         List<Order> ordersAccepted ;
        List<String> images;
        if (order.getProduct() != null) {
            images = order.getProduct().getImages();
        } else {
            images = order.getEquipment().getImages();
        }

        if (images != null && !images.isEmpty()) {
            Glide.with(context)
                    .load(images.get(0))
                    .error(R.drawable.no_image_bg)
                    .into(OrderImage);
        }  else {
            OrderImage.setImageResource(R.drawable.no_image_bg);
        }

        if (order.getProduct() != null) {
            OrderName.setText(order.getProduct().getProductName());
            OrderPrice.setText("$" + String.format("%.2f", order.getPrice()));
            OrderQuantity.setText("Quantity: "+ order.getQuantity());
            Status.setText(Character.toUpperCase(order.getStatus().charAt(0)) + order.getStatus().substring(1));
        } else {
            OrderName.setText(order.getEquipment().getEquipmentName());
            OrderPrice.setText("$" + String.format("%.2f", order.getPrice()));
            OrderQuantity.setText("Quantity: "+ order.getQuantity());
            Status.setText(Character.toUpperCase(order.getStatus().charAt(0)) + order.getStatus().substring(1));
        }

        if (order.getStatus().equals("pending")) {
            Status.setTextColor(itemView.getResources().getColor(R.color.buttonbg));
        }


    }


}
