package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

public class ProductDataTest extends AbstractUnitTest {

	@Test
	public void testProductData() {
		ProductData p = new ProductData();
		int id = 1;
		String barcode = StringUtil.getAlphaNumericString();
		String brand = "increff";
		String category = "pos";
		String name = "viram";
		double mrp = 10.50;
		p.setId(id);
		p.setBarcode(barcode);
		p.setBrand(brand);
		p.setCategory(category);
		p.setName(name);
		p.setMrp(mrp);
		assertEquals(id, p.getId());
		assertEquals(barcode, p.getBarcode());
		assertEquals(brand, p.getBrand());
		assertEquals(category, p.getCategory());
		assertEquals(name, p.getName());
		assertEquals(mrp, p.getMrp(), 0.01);

	}

}
