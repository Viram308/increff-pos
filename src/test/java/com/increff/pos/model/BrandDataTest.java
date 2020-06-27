package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class BrandDataTest extends AbstractUnitTest {
	// test getters and setters for brand data
	@Test
	public void testBrandData() {
		BrandData b = new BrandData();
		int id = 1;
		String brand = "increff";
		String category = "pos";
		b.setId(id);
		b.setBrand(brand);
		b.setCategory(category);
		assertEquals(id, b.getId());
		assertEquals(brand, b.getBrand());
		assertEquals(category, b.getCategory());

	}

}
