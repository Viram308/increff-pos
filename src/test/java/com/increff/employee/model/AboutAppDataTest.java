package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class AboutAppDataTest extends AbstractUnitTest {
	// test getters and setters for about app data
	@Test
	public void testAboutAppData() {
		AboutAppData a = new AboutAppData();
		String name = "PoS Application";
		String version = "1.0";
		a.setName(name);
		a.setVersion(version);
		assertEquals(name, a.getName());
		assertEquals(version, a.getVersion());
	}

}
