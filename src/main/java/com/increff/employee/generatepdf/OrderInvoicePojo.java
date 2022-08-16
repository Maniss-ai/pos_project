package com.increff.employee.generatepdf;

public class OrderInvoicePojo {
    Integer SNo;
    String barcode;
    Integer quantity;
    Double selling_price;
    Double bill_amount;

    public OrderInvoicePojo() {

    }

    public OrderInvoicePojo(Integer SNo, String barcode, Integer quantity, Double selling_price, Double bill_amount) {
        this.SNo = SNo;
        this.barcode = barcode;
        this.quantity = quantity;
        this.selling_price = selling_price;
        this.bill_amount = bill_amount;
    }

    public Integer getSNo() {
        return SNo;
    }

    public void setSNo(Integer SNo) {
        this.SNo = SNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(Double selling_price) {
        this.selling_price = selling_price;
    }

    public Double getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(Double bill_amount) {
        this.bill_amount = bill_amount;
    }
}
