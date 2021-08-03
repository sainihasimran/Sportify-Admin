package com.cegep.sportify_admin.Orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.SportifyAdminApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderFragment extends Fragment {

    private OrderAdapter orderAdapter;
    private List<Order> orders = new ArrayList<>();
    public List<Order> ordersdeclined = new ArrayList<>();
    public List<Order> ordersAccepted = new ArrayList<>();
    private TextView emptyView;

    private Button btndelivered;
    private Button btncancel;

    public PendingOrderFragment() {
        // Required empty public constructor
    }

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for (DataSnapshot orderDatasnapshot : snapshot.getChildren()) {
                Order order = orderDatasnapshot.getValue(Order.class);
                if (order != null && "pending".equals(order.getStatus())) {
                    orders.add(order);
                }
            }
            if (orders.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            orderAdapter.update(orders);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);
        btndelivered = view.findViewById(R.id.btnaccept);
        btncancel = view.findViewById(R.id.btndeclined);


        setupRecyclerView(view);

        Query query = FirebaseDatabase.getInstance().getReference("Orders").orderByChild("adminId").equalTo(SportifyAdminApp.admin.adminId);
        query.addValueEventListener(valueEventListener);

//        btncancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orderdeclined();
//            }
//        });
//
//        btndelivered.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                      OrdersAccepted();
//            }
//        });
    }

    private void setupRecyclerView(View view) {
        orderAdapter = new OrderAdapter(requireContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orderAdapter);
    }

    private void orderdeclined() {

        final ValueEventListener ValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot orderDatasnapshot : snapshot.getChildren()) {
                    Order order = orderDatasnapshot.getValue(Order.class);
                    if (order != null && "pending".equals(order.getStatus())) {
                        ordersdeclined.add(order);
                        orders.remove(order);
                        order.setStatus("Cancelled");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
    }

    private void OrdersAccepted() {

        final ValueEventListener ValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot orderDatasnapshot : snapshot.getChildren()) {
                    Order order = orderDatasnapshot.getValue(Order.class);
                    if (order != null && "pending".equals(order.getStatus())) {
                        ordersAccepted.add(order);
                        orders.remove(order);
                        order.setStatus("Accepted");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
    }
}