package com.increff.employee.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateForm {
    private String barcode;
    private String product;
    private Double mrp;
}
