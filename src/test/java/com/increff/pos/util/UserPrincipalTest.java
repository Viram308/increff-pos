package com.increff.pos.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.model.UserPrincipal;
import com.increff.pos.spring.AbstractUnitTest;

public class UserPrincipalTest extends AbstractUnitTest {
	// test UserPrincipal
	@Test
	public void testUserPrincipal() {
		UserPrincipal u = new UserPrincipal();
		int id = 1;
		String email = "shahviram308@gmail.com";
		String role = "admin";

		u.setId(id);
		u.setEmail(email);
		u.setRole(role);
		// id,email and role should be same as object's id and email
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(role, u.getRole());
	}
}
