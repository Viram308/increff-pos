package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class LoginFormTest extends AbstractUnitTest {
	// test getters and setters for login form
	@Test
	public void testLoginForm() {
		LoginForm l = new LoginForm();
		String email = "shahviram308@gmail.com";
		String password = "password";
		l.setEmail(email);
		l.setPassword(password);
		assertEquals(email, l.getEmail());
		assertEquals(password, l.getPassword());

	}

}
