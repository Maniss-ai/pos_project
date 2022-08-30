package com.increff.employee.model.form;

import com.increff.employee.dto.OrderInvoicePojo;
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
}
