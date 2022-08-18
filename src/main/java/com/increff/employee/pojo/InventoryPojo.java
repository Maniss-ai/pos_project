package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class InventoryPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String barcode;
    private Integer inventory;

    public static class OrderInvoicePojo {
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

        public Double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(Double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public Double getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(Double billAmount) {
            this.billAmount = billAmount;
        }
    }
}
