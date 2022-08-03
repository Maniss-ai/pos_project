package com.increff.employee.model.data;

import com.increff.employee.model.form.PlaceOrderForm;

public class PlaceOrderData extends PlaceOrderForm {
    int id;
    int order_id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
