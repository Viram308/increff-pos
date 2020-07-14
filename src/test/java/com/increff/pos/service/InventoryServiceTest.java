package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.StringUtil;

public class InventoryServiceTest extends AbstractUnitTest {
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;

	// test Inventory Service
	@Test
	public void testAdd() throws ApiException {
		InventoryPojo inventoryPojo = getInventoryPojoTest();
		// Add
		inventoryService.add(inventoryPojo);
		InventoryPojo inventoryPojoFinal = inventoryService.get(inventoryPojo.getId());
		assertEquals(inventoryPojo.getQuantity(), inventoryPojoFinal.getQuantity());
	}

	@Test
	public void testGetByProductId() throws ApiException {
		InventoryPojo inventoryPojo = getInventoryPojoTest();
		ProductMasterPojo productMasterPojo = productService.get(inventoryPojo.getProductId());
		inventoryService.add(inventoryPojo);
		// select data for given product id
		InventoryPojo ip = inventoryService.getByProductId(productMasterPojo);
		assertEquals(inventoryPojo.getId(), ip.getId());
		assertEquals(inventoryPojo.getQuantity(), ip.getQuantity());
	}

	@Test
	public void testGet() throws ApiException {
		InventoryPojo inventoryPojo = getInventoryPojoTest();
		inventoryService.add(inventoryPojo);
		InventoryPojo ip = inventoryService.get(inventoryPojo.getId());
		// test for same quantity
		assertEquals(inventoryPojo.getQuantity(), ip.getQuantity());
	}

	@Test
	public void testGetAll() throws ApiException {
		InventoryPojo inventoryPojo = getInventoryPojoTest();
		inventoryService.add(inventoryPojo);
		// test get all function
		List<InventoryPojo> inventoryPojos = inventoryService.getAll();
		assertEquals(1, inventoryPojos.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		InventoryPojo inventoryPojo = getInventoryPojoTest();
		inventoryService.add(inventoryPojo);
		InventoryPojo ip = inventoryService.get(inventoryPojo.getId());
		int newQuantity = 20;
		ip.setQuantity(newQuantity);
		// update data
		inventoryService.update(ip.getId(), ip);
		InventoryPojo inventoryPojoFinal = inventoryService.get(ip.getId());
		// test for updated data
		assertEquals(newQuantity, inventoryPojoFinal.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		InventoryPojo inventoryPojo = getInventoryPojoTest();
		inventoryService.add(inventoryPojo);
		// select data for given id
		InventoryPojo ip = inventoryService.getCheck(inventoryPojo.getId());
		assertEquals(inventoryPojo.getId(), ip.getId());
		// Throw exception
		inventoryService.getCheck(ip.getId() + 1);
	}

	private InventoryPojo getInventoryPojoTest() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		// create data
		String barcode = StringUtil.getAlphaNumericString();
		BrandMasterPojo b = new BrandMasterPojo();
		ProductMasterPojo p = new ProductMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		brandService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		productService.add(p, b);
		int quantity = 10;
		i.setProductId(p.getId());
		i.setQuantity(quantity);
		return i;
	}
}
