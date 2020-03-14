package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class OrderServiceTest extends AbstractUnitTest {
	@Autowired
	private OrderService service;

	// test order service
	@Test
	public void testAdd() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		// test add
		service.add(op);
	}

	@Test
	public void testDelete() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		service.add(op);
		// test delete
		service.delete(op.getId());
	}

	@Test
	public void testGet() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		service.add(op);
		OrderPojo o = service.get(op.getId());
		// test added data
		assertEquals(op.getDatetime(), o.getDatetime());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {

		OrderPojo op = getOrderPojoTest();
		service.add(op);
		OrderPojo o = service.getCheck(op.getId());
		service.delete(o.getId());
		// After delete throw exception while getting data
		service.getCheck(o.getId());
	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	private OrderPojo getOrderPojoTest() throws ApiException {
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		// create data
		op.setDatetime(datetime);
		return op;
	}
}
