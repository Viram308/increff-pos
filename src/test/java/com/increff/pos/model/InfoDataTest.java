package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InfoDataTest {

	@Test
	public void testInfoData() {
		InfoData infoData = new InfoData();
		String email = "shahviram308@gmail.com";
		String message = "no message";
		String role = "admin";
		infoData.setEmail(email);
		infoData.setMessage(message);
		infoData.setRole(role);
		assertEquals(email, infoData.getEmail());
		assertEquals(message, infoData.getMessage());
		assertEquals(role, infoData.getRole());
	}
}
