package com.cegep.sportify_admin.Orders;

public interface OrderItemClickListener {

    void btndeclinedonClick(Order order, boolean declined);
    void btnacceptedonClick(Order order, boolean accept);

}
