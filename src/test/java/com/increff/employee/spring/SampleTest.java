package com.increff.employee.spring;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

public class SampleTest {
	// test Sample files
	@Test
	public void testFiles() {
		// test different files exists or not
		InputStream is = null;
		is = SampleTest.class.getResourceAsStream("/com/increff/employee/brand.tsv");
		assertNotNull(is);
		is = SampleTest.class.getResourceAsStream("/com/increff/employee/inventory.tsv");
		assertNotNull(is);
		is = SampleTest.class.getResourceAsStream("/com/increff/employee/product.tsv");
		assertNotNull(is);
	}

}
