package com.increff.employee.service;

import org.junit.Test;

public class ApiExceptionTest {

	@Test(expected = ApiException.class)
	public void testApiExcecption() throws ApiException {
		throw new ApiException("Testing Exception");
	}
}
