package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class InfoDataTest extends AbstractUnitTest {
	// test getters and setters for info data
	@Test
	public void testInfoData() {
		InfoData i = new InfoData();
		String email = "shahviram308@gmail.com";
		String message = "valid";
		String role = "admin";
		i.setEmail(email);
		i.setMessage(message);
		i.setRole(role);
		assertEquals(role, i.getRole());
		assertEquals(email, i.getEmail());
		assertEquals(message, i.getMessage());

	}

}
