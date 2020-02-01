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

	@Test
	public void testAdd() throws ApiException {
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		service.add(op);
	}

	@Test
	public void testDelete() throws ApiException {
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		service.add(op);
		service.delete(service.getMax());
	}

	@Test
	public void testGet() throws ApiException {
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		service.add(op);
		OrderPojo o = service.get(op.getId());
		assertEquals(datetime, o.getDatetime());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {

		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		service.add(op);
		OrderPojo o = service.getCheck(op.getId());
		service.delete(o.getId());
		service.getCheck(o.getId());
	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}
}
