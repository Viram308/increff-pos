package com.increff.pos.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class PasswordUtilTest extends AbstractUnitTest{

	@Test
	public void testGetHash() throws Exception {
		String password1 = "password1";
		String password2="password2";
		String hashedPassword1=PasswordUtil.getHash(password1);
		String hashedPassword2=PasswordUtil.getHash(password2);
		// different password different hash
		assertNotEquals(hashedPassword1, hashedPassword2);
		hashedPassword2=PasswordUtil.getHash(password1);
		// same password same hash
		assertEquals(hashedPassword1, hashedPassword2);
	}
	
}
