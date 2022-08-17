package com.increff.employee.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemUpdateForm {
    private Integer quantity;
    private Double sellingPrice;
}
