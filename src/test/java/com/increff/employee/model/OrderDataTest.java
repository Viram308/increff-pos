package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class OrderDataTest extends AbstractUnitTest {

	@Test
	public void testOrderData() {
		OrderData o = new OrderData();
		int id = 1;
		String datetime = getDateTime();
		o.setId(id);
		o.setDatetime(datetime);
		assertEquals(id, o.getId());
		assertEquals(datetime, o.getDatetime());

	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

}