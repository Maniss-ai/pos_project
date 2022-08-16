package com.increff.employee.dto;

import com.increff.employee.model.data.AboutAppData;
import com.increff.employee.service.AboutAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AboutAppDto {
    @Autowired
    private AboutAppService service;

    public AboutAppData getDetails() {
        AboutAppData d = new AboutAppData();
        d.setName(service.getName());
        d.setVersion(service.getVersion());
        return d;
    }
}
