package com.increff.employee.model.form;

import com.increff.employee.pojo.InventoryPojo;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    private String time;
    private Double billAmount;

    @XmlRootElement
    public static class OrderInvoice {
        Integer id;
        List<InventoryPojo.OrderInvoicePojo> orderInvoicePojoList;
        String time;
        Integer orderId;
        Double totalAmount;

        public OrderInvoice() {

        }

        public OrderInvoice(Integer id, List<InventoryPojo.OrderInvoicePojo> orderInvoicePojoList, String time, Integer orderId, Double totalAmount) {
            this.id = id;
            this.orderInvoicePojoList = orderInvoicePojoList;
            this.time = time;
            this.orderId = orderId;
            this.totalAmount = totalAmount;
        }

        @XmlAttribute
        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }

        @XmlElement
        public List<InventoryPojo.OrderInvoicePojo> getOrderInvoicePojoList() {
            return orderInvoicePojoList;
        }

        public void setOrderInvoicePojoList(List<InventoryPojo.OrderInvoicePojo> orderInvoicePojoList) {
            this.orderInvoicePojoList = orderInvoicePojoList;
        }
        @XmlElement
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @XmlElement
        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        @XmlElement
        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
}
