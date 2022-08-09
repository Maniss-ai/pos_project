package com.increff.employee.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderForm {
    private String barcode;
    private int quantity;
    private Double selling_price;

}
