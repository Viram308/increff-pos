package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class BillDataTest extends AbstractUnitTest {
	// test getters and setters for bill data

	@Test
	public void testBillData() {
		BillData b = new BillData();
		String name = "increff";
		int id = 1;
		double mrp = 10.50;
		int quantity = 40;
		b.setId(id);
		b.setMrp(mrp);
		b.setName(name);
		b.setQuantity(quantity);
		assertEquals(id, b.getId());
		assertEquals(name, b.getName());
		assertEquals(quantity, b.getQuantity());
		assertEquals(mrp, b.getMrp(), 0.01);

	}

}
