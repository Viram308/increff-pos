package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class UserDataTest extends AbstractUnitTest {
	// test getters and setters for user data
	@Test
	public void testUserData() {
		UserData u = new UserData();
		int id = 1;
		String email = "shahviram308@gmail.com";
		String password = "password";
		String role = "admin";
		u.setId(id);
		u.setEmail(email);
		u.setPassword(password);
		u.setRole(role);
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(password, u.getPassword());
		assertEquals(role, u.getRole());
	}

}
