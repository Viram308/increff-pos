package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
	private InventoryService service;
	@Autowired
	private BrandService bService;

	@Autowired
	private ProductService pService;

	// test Inventory Service
	@Test
	public void testAdd() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		// Add
		service.add(i);
		InventoryPojo inventoryPojo = service.get(i.getId());
		assertEquals(i.getQuantity(), inventoryPojo.getQuantity());
	}

	@Test
	public void testDelete() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		// Delete should be successful and should not throw exception as data exists
		service.delete(i.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetByProductId() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		ProductMasterPojo productMasterPojo = pService.get(i.getProductid());
		service.add(i);
		// select data for given productid

		InventoryPojo ip = service.getByProductId(productMasterPojo);
		assertEquals(i.getId(), ip.getId());
		service.delete(ip.getId());
		// Throw exception after deletion
		service.getByProductId(productMasterPojo);
	}

	@Test
	public void testGet() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		InventoryPojo ip = service.get(i.getId());
		// test for same quantity
		assertEquals(i.getQuantity(), ip.getQuantity());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		InventoryPojo ip = service.get(i.getId());
		int newQuantity = 20;
		ip.setQuantity(newQuantity);
		// update data
		service.update(ip.getId(), ip);
		InventoryPojo pi = service.get(ip.getId());
		// test for updated data
		assertEquals(newQuantity, pi.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		// select data for given id
		InventoryPojo ip = service.getCheck(i.getId());
		assertEquals(i.getId(), ip.getId());
		service.delete(ip.getId());
		// Throw exception after deletion
		service.getCheck(ip.getId());
	}

	@Test
	public void testSearchData() throws ApiException {
		InventoryPojo inventoryPojo1 = getInventoryPojoTest();
		service.add(inventoryPojo1);
		List<Integer> productIds=new ArrayList<Integer>();
		productIds.add(inventoryPojo1.getProductid());
		List<InventoryPojo> inventoryPojos=service.searchData(productIds);
		assertEquals(1, inventoryPojos.size());
		productIds.clear();
		inventoryPojos=service.searchData(productIds);
		assertEquals(0, inventoryPojos.size());
	}
	
	private InventoryPojo getInventoryPojoTest() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		// create data
		String barcode = StringUtil.getAlphaNumericString();
		BrandMasterPojo b = new BrandMasterPojo();
		ProductMasterPojo p = new ProductMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		pService.add(p, b);
		int quantity = 10;
		i.setProductid(p.getId());
		i.setQuantity(quantity);
		return i;
	}
}
