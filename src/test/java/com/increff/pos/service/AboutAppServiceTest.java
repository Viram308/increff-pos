package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.AboutAppData;
import com.increff.pos.spring.AbstractUnitTest;

public class AboutAppServiceTest extends AbstractUnitTest {

	@Autowired
	private AboutAppService service;

	// test AboutApp
	@Test
	public void testAboutAppService() {
		AboutAppData d = service.getNameandVersion();
		// test application name
		assertEquals("PoS Application", d.name);
		// test application version
		assertEquals("1.0", d.version);
	}

}
