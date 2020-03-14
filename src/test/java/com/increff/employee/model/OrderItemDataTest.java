package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

public class OrderItemDataTest extends AbstractUnitTest {
	// test getters and setters for order item data
	@Test
	public void testOrderItemData() {
		OrderItemData o = new OrderItemData();
		int id = 1;
		int orderId = 2;
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 40;
		double mrp = 10.50;
		o.setId(id);
		o.setOrderId(orderId);
		o.setBarcode(barcode);
		o.setQuantity(quantity);
		o.setMrp(mrp);
		assertEquals(id, o.getId());
		assertEquals(orderId, o.getOrderId());
		assertEquals(barcode, o.getBarcode());
		assertEquals(quantity, o.getQuantity());
		assertEquals(mrp, o.getMrp(), 0.01);

	}

}
