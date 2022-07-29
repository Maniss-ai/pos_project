package com.increff.employee.generatepdf;

public class OrderInvoicePojo {
    int SNo;
    String barcode;
    int quantity;
    int selling_price;
    int bill_amount;

    public OrderInvoicePojo() {

    }

    public OrderInvoicePojo(int SNo, String barcode, int quantity, int selling_price, int bill_amount) {
        this.SNo = SNo;
        this.barcode = barcode;
        this.quantity = quantity;
        this.selling_price = selling_price;
        this.bill_amount = bill_amount;
    }

    public int getSNo() {
        return SNo;
    }

    public void setSNo(int SNo) {
        this.SNo = SNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(int selling_price) {
        this.selling_price = selling_price;
    }

    public int getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(int bill_amount) {
        this.bill_amount = bill_amount;
    }
}
