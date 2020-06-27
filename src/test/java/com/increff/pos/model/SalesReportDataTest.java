package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class SalesReportDataTest extends AbstractUnitTest {
	// test getters and setters for sales report data
	@Test
	public void testSalesReportData() {
		SalesReportData s = new SalesReportData();
		int id = 1;
		String brand = "increff";
		String category = "pos";
		int quantity = 10;
		double revenue = 10.25;
		s.setId(id);
		s.setBrand(brand);
		s.setCategory(category);
		s.setQuantity(quantity);
		s.setRevenue(revenue);
		assertEquals(id, s.getId());
		assertEquals(brand, s.getBrand());
		assertEquals(category, s.getCategory());
		assertEquals(quantity, s.getQuantity());
		assertEquals(revenue, s.getRevenue(), 0.01);
	}

}
