package com.increff.employee.model.data;

import com.increff.employee.model.form.OrderItemForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    private Integer id;
    private Integer order_id;
}
