package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class SalesReportFormTest extends AbstractUnitTest {

	@Test
	public void testSalesReportForm() {
		SalesReportForm s = new SalesReportForm();
		String startdate = "02-02-2020";
		String enddate = "20-02-2020";
		String brand = "increff";
		String category = "pos";
		s.setStartdate(startdate);
		s.setEnddate(enddate);
		s.setBrand(brand);
		s.setCategory(category);
		assertEquals(startdate, s.getStartdate());
		assertEquals(enddate, s.getEnddate());
		assertEquals(brand, s.getBrand());
		assertEquals(category, s.getCategory());
	}

}
