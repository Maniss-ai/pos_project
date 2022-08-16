package com.increff.employee.generatepdf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class OrderInvoice {
    Integer id;
    List<OrderInvoicePojo> orderInvoicePojoList;
    String time;
    Integer order_id;
    Double total_amount;

    public OrderInvoice() {

    }

    public OrderInvoice(Integer id, List<OrderInvoicePojo> orderInvoicePojoList, String time, Integer order_id, Double total_amount) {
        this.id = id;
        this.orderInvoicePojoList = orderInvoicePojoList;
        this.time = time;
        this.order_id = order_id;
        this.total_amount = total_amount;
    }

    @XmlAttribute
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    @XmlElement
    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }
}
