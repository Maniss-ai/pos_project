package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
public class OrderInvoicePojo {
    Integer SNo;
    String barcode;
    Integer quantity;
    Double sellingPrice;
    Double billAmount;

    public OrderInvoicePojo() {

    }

    public OrderInvoicePojo(Integer SNo, String barcode, Integer quantity, Double sellingPrice, Double billAmount) {
        this.SNo = SNo;
        this.barcode = barcode;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.billAmount = billAmount;
    }
}
