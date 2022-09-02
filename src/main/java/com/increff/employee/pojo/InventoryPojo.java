package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class InventoryPojo {
    @Id
    private Integer id;
    private Integer inventory;
}
