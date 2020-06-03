package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.spring.AbstractUnitTest;

public class AboutAppServiceTest extends AbstractUnitTest {

	@Autowired
	private AboutAppService service;

	// test AboutApp
	@Test
	public void testAboutAppService() {
		// test application name
		assertEquals("PoS Application", service.getName());
		// test application version
		assertEquals("1.0", service.getVersion());
	}

}
