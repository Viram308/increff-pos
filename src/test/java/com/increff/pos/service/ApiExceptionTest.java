package com.increff.pos.service;

import org.junit.Test;

public class ApiExceptionTest {
	// test ApiException
	@Test(expected = ApiException.class)
	public void testApiExcecption() throws ApiException {
		throw new ApiException("Testing Exception");
	}
}
