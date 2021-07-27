package com.cegep.sportify_admin.Orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify_admin.R;

import java.util.Collection;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private final Context context;

    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position), context);
    }



    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void update(Collection<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        notifyDataSetChanged();
    }
}
