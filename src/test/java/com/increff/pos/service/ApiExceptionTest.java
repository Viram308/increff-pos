package com.increff.pos.service;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class ApiExceptionTest extends AbstractUnitTest {
	// test ApiException
	@Test(expected = ApiException.class)
	public void testApiExcecption() throws ApiException {
		throw new ApiException("Testing Api Exception");
	}
}
