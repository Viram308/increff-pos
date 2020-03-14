package com.increff.employee.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserPrincipalTest {
	// test UserPrincipal
	@Test
	public void testUserPrincipal() {
		UserPrincipal u = new UserPrincipal();
		int id = 1;
		String email = "shahviram308@gmail.com";
		u.setId(id);
		u.setEmail(email);
		// id and email should be same as object's id and email
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
	}
}
