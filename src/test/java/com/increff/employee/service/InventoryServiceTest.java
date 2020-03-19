package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

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
		// Add one time
		service.add(i);
		// test for double quantity
		service.add(i);
		assertEquals(20, i.getQuantity());
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
		service.add(i);
		// select data for given productid
		InventoryPojo ip = service.getByProductId(i.getProductMasterPojo());
		assertEquals(i.getId(), ip.getId());
		service.delete(ip.getId());
		// Throw exception after deletion
		service.getByProductId(ip.getProductMasterPojo());
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
	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.checkData(i);
		// throw exception
		i.setQuantity(0);
		service.checkData(i);
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
		p.setBrand_category(b);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		pService.add(p);
		int quantity = 10;
		i.setProductMasterPojo(p);
		i.setQuantity(quantity);
		return i;
	}
}
