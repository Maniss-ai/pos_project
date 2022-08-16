package com.increff.employee.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private String barcode;
    private Integer quantity;
    private Double selling_price;

}
