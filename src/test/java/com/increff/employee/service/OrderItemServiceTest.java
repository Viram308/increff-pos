package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class OrderItemServiceTest extends AbstractUnitTest {
	@Autowired
	private OrderItemService service;

	@Test
	public void testAdd() {
		OrderItemPojo o = new OrderItemPojo();
		int orderid = 1;
		int productId = 2;
		int quantity = 10;
		double sellingPrice = 10.25;
		o.setOrderid(orderid);
		o.setProductId(productId);
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		service.add(o);
	}

	@Test
	public void testDelete() throws ApiException {
		OrderItemPojo o = new OrderItemPojo();
		int orderid = 1;
		int productId = 2;
		int quantity = 10;
		double sellingPrice = 10.25;
		o.setOrderid(orderid);
		o.setProductId(productId);
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		service.add(o);
		service.delete(o.getId());
	}

	@Test
	public void testGet() throws ApiException {
		OrderItemPojo o = new OrderItemPojo();
		int orderid = 1;
		int productId = 2;
		int quantity = 10;
		double sellingPrice = 10.25;
		o.setOrderid(orderid);
		o.setProductId(productId);
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		service.add(o);
		OrderItemPojo p = service.get(o.getId());
		assertEquals(1, p.getOrderid());
		assertEquals(2, p.getProductId());
		assertEquals(10, p.getQuantity());
		assertEquals(10.25, p.getSellingPrice(), 0.01);
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		OrderItemPojo o = new OrderItemPojo();
		int orderid = 1;
		int productId = 2;
		int quantity = 10;
		double sellingPrice = 10.25;
		o.setOrderid(orderid);
		o.setProductId(productId);
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		service.add(o);
		OrderItemPojo p = service.get(o.getId());
		int newQuantity = 20;
		p.setQuantity(newQuantity);
		service.update(p.getId(), p);
		OrderItemPojo pi = service.get(p.getId());
		assertEquals(newQuantity, pi.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		OrderItemPojo o = new OrderItemPojo();
		int orderid = 1;
		int productId = 2;
		int quantity = 10;
		double sellingPrice = 10.25;
		o.setOrderid(orderid);
		o.setProductId(productId);
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		service.add(o);
		OrderItemPojo p = service.getCheck(o.getId());
		service.delete(p.getId());
		service.getCheck(p.getId());

	}

}
