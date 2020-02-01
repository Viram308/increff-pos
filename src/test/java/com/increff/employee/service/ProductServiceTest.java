package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

public class ProductServiceTest extends AbstractUnitTest {
	@Autowired
	private ProductService service;

	@Test
	public void testAdd() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		service.add(p);
		service.add(p);
	}

	@Test
	public void testDelete() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		service.add(p);
		service.delete(p.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcode() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		service.add(p);
		ProductMasterPojo pr = service.getByBarcode(p.getBarcode());
		service.delete(pr.getId());
		service.getByBarcode(pr.getBarcode());
	}

	@Test
	public void testGet() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		service.add(p);
		ProductMasterPojo pr = service.get(p.getId());
		assertEquals("product", pr.getName());
		assertEquals(StringUtil.toLowerCase(barcode), p.getBarcode());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		service.add(p);
		ProductMasterPojo pr = service.get(p.getId());
		pr.setName(" Check ");
		service.update(pr.getId(), pr);
		ProductMasterPojo pm = service.get(pr.getId());
		assertEquals("check", pm.getName());
	}
	
	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		service.add(p);

		ProductMasterPojo pr = service.getCheck(p.getId());
		service.delete(pr.getId());
		service.getCheck(pr.getId());
		
	}

	@Test
	public void testNormalize() {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		int id = 1;
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(id);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		ProductService.normalize(p);
		assertEquals("product", p.getName());
		assertEquals(StringUtil.toLowerCase(barcode), p.getBarcode());

	}
}
