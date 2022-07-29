package com.increff.employee.generatepdf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class OrderInvoice {
    int id;
    List<OrderInvoicePojo> orderInvoicePojoList;
    String time;
    int order_id;
    int total_amount;

    public OrderInvoice() {

    }

    public OrderInvoice(int id, List<OrderInvoicePojo> orderInvoicePojoList, String time, int order_id, int total_amount) {
        this.id = id;
        this.orderInvoicePojoList = orderInvoicePojoList;
        this.time = time;
        this.order_id = order_id;
        this.total_amount = total_amount;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public List<OrderInvoicePojo> getOrderInvoicePojoList() {
        return orderInvoicePojoList;
    }

    public void setOrderInvoicePojoList(List<OrderInvoicePojo> orderInvoicePojoList) {
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
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    @XmlElement
    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }
}
