package com.increff.pos.spring;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

public class SampleTest {
	// test Sample files
	@Test
	public void testFiles() {
		// test different files exists or not
		InputStream is = null;
		is = SampleTest.class.getResourceAsStream("/com/increff/pos/brand.tsv");
		assertNotNull(is);
		is = SampleTest.class.getResourceAsStream("/com/increff/pos/inventory.tsv");
		assertNotNull(is);
		is = SampleTest.class.getResourceAsStream("/com/increff/pos/product.tsv");
		assertNotNull(is);
	}

}
