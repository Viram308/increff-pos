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

	@Test
	public void testAdd() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		service.add(i);
	}

	@Test
	public void testDelete() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		service.delete(i.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetByProductId() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		InventoryPojo ip = service.getByProductId(i.getProductMasterPojo().getId());
		service.delete(ip.getId());
		service.getByProductId(ip.getProductMasterPojo().getId());
	}

	@Test
	public void testGet() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		InventoryPojo ip = service.get(i.getId());
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
		service.update(ip.getId(), ip);
		InventoryPojo pi = service.get(ip.getId());
		assertEquals(newQuantity, pi.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		InventoryPojo i = getInventoryPojoTest();
		service.add(i);
		InventoryPojo ip = service.getCheck(i.getId());
		service.delete(ip.getId());
		service.getCheck(ip.getId());
	}

	private InventoryPojo getInventoryPojoTest() throws ApiException {
		InventoryPojo i = new InventoryPojo();

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
