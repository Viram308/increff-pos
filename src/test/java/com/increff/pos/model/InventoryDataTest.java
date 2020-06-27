package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.StringUtil;

public class InventoryDataTest extends AbstractUnitTest {
	// test getters and setters for inventory data
	@Test
	public void testInventoryData() {
		InventoryData i = new InventoryData();
		int id = 1;
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 40;
		i.setId(id);
		i.setBarcode(barcode);
		i.setQuantity(quantity);
		assertEquals(id, i.getId());
		assertEquals(barcode, i.getBarcode());
		assertEquals(quantity, i.getQuantity());

	}

}
