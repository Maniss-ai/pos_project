package com.increff.employee.dto;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.model.data.AboutAppData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class AboutAppDtoTest extends AbstractUnitTest {
    @Autowired
    AboutAppDto aboutAppDto;

    @Test
    public void testGetDetails() {
        AboutAppData aboutAppData = aboutAppDto.getDetails();

        assertEquals("POS Application", aboutAppData.getName());
        assertEquals("1.0", aboutAppData.getVersion());
    }
}
