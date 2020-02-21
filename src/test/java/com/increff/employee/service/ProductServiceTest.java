package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

public class ProductServiceTest extends AbstractUnitTest {
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService bService;

	@Test
	public void testAdd() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		service.add(p);
	}

	@Test
	public void testDelete() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		service.delete(p.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcode() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		ProductMasterPojo pr = service.getByBarcode(p.getBarcode());
		service.delete(pr.getId());
		service.getByBarcode(pr.getBarcode());
	}

	@Test
	public void testGet() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		ProductMasterPojo pr = service.get(p.getId());
		assertEquals("product", pr.getName());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		ProductMasterPojo pr = service.get(p.getId());
		pr.setName(" Check ");
		service.update(pr.getId(), pr);
		ProductMasterPojo pm = service.get(pr.getId());
		assertEquals("check", pm.getName());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);

		ProductMasterPojo pr = service.getCheck(p.getId());
		service.delete(pr.getId());
		service.getCheck(pr.getId());

	}

	@Test
	public void testNormalize() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		ProductService.normalize(p);
		assertEquals("product", p.getName());
	}

	private ProductMasterPojo getProductMasterPojoTest() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();

		String barcode = StringUtil.getAlphaNumericString();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(b);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		return p;
	}
}
