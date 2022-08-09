package com.increff.employee.model.data;

import com.increff.employee.model.form.PlaceOrderForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderData extends PlaceOrderForm {
    private int id;
    private int order_id;
}
