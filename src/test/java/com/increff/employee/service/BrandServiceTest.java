package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {
	@Autowired
	private BrandService service;

	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		service.add(b);
		service.add(b);
	}

	@Test
	public void testDelete() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		service.add(b);
		int id = service.getId(b.getBrand(), b.getCategory());
		service.delete(id);
	}

	@Test(expected = ApiException.class)
	public void testGetId() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		service.add(b);
		int id = service.getId(b.getBrand(), b.getCategory());
		assertEquals(id, b.getId());
		service.delete(id);
		service.getId(b.getBrand(), b.getCategory());
	}

	@Test
	public void testGet() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		service.add(b);
		BrandMasterPojo p = service.get(b.getId());
		assertEquals("viram", p.getBrand());
		assertEquals("shah", p.getCategory());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		service.add(b);
		BrandMasterPojo p = service.get(b.getId());
		p.setBrand("increff");
		p.setCategory("pos");
		service.update(p.getId(), p);
		BrandMasterPojo m = service.get(p.getId());
		assertEquals("increff", m.getBrand());
		assertEquals("pos", m.getCategory());
	}
	
	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		service.add(b);
		BrandMasterPojo p = service.getCheck(b.getId());
		service.delete(p.getId());
		service.getCheck(p.getId());
		
	}

	@Test
	public void testNormalize() {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setBrand(" Viram ");
		b.setCategory("sHaH");
		BrandService.normalize(b);
		assertEquals("viram", b.getBrand());
		assertEquals("shah", b.getCategory());

	}
}
