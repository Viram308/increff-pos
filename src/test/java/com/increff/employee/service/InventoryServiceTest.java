package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class InventoryServiceTest extends AbstractUnitTest {
	@Autowired
	private InventoryService service;

	@Test
	public void testAdd() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int id = 1;
		int quantity = 10;
		i.setProductId(id);
		i.setQuantity(quantity);
		service.add(i);
		service.add(i);
	}

	@Test
	public void testDelete() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int id = 1;
		int quantity = 10;
		i.setProductId(id);
		i.setQuantity(quantity);
		service.add(i);
		service.delete(i.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetByProductId() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int id = 1;
		int quantity = 10;
		i.setProductId(id);
		i.setQuantity(quantity);
		service.add(i);
		InventoryPojo p = service.getByProductId(i.getProductId());
		service.delete(p.getId());
		service.getByProductId(p.getProductId());
	}

	@Test
	public void testGet() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int id = 1;
		int quantity = 10;
		i.setProductId(id);
		i.setQuantity(quantity);
		service.add(i);
		InventoryPojo p = service.get(i.getId());
		assertEquals(quantity, p.getQuantity());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int id = 1;
		int quantity = 10;
		i.setProductId(id);
		i.setQuantity(quantity);
		service.add(i);
		InventoryPojo p = service.get(i.getId());
		int newQuantity = 20;
		p.setQuantity(newQuantity);
		service.update(p.getId(), p);
		InventoryPojo pi = service.get(p.getId());
		assertEquals(newQuantity, pi.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int id = 1;
		int quantity = 10;
		i.setProductId(id);
		i.setQuantity(quantity);
		service.add(i);
		InventoryPojo p = service.getCheck(i.getId());
		service.delete(p.getId());
		service.getCheck(p.getId());
	}
}
