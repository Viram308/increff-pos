package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class InventoryReportDataTest extends AbstractUnitTest {
	// test getters and setters for inventory report data
	@Test
	public void testSalesReportData() {
		InventoryReportData i = new InventoryReportData();
		int id = 1;
		String brand = "increff";
		String category = "pos";
		int quantity = 10;
		i.setId(id);
		i.setBrand(brand);
		i.setCategory(category);
		i.setQuantity(quantity);
		assertEquals(id, i.getId());
		assertEquals(brand, i.getBrand());
		assertEquals(category, i.getCategory());
		assertEquals(quantity, i.getQuantity());
	}

}
