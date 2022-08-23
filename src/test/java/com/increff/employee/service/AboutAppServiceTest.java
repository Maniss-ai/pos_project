package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import com.increff.employee.AbstractUnitTest;
import com.increff.employee.service.AboutAppService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AboutAppServiceTest extends AbstractUnitTest {

	@Autowired
	private AboutAppService service;

	@Test
	public void testServiceApis() {
		assertEquals("POS Application", service.getName());
		assertEquals("1.0", service.getVersion());
	}

}
