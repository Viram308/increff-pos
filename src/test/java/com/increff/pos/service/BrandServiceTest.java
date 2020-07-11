package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {
	@Autowired
	private BrandService service;

	// test brand service
	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		// Add one time
		service.add(b);
		// Throw exception while entering second time
		service.add(b);
	}

	@Test
	public void testDelete() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		// Add data
		service.add(b);
		BrandMasterPojo brandMasterPojo = service.getByBrandCategory(b.getBrand(), b.getCategory());
		assertEquals(brandMasterPojo.getId(), b.getId());
		// Delete should be successful and should not throw exception as data exists
		service.delete(brandMasterPojo.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetId() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// select data for given brand and category
		BrandMasterPojo brandMasterPojo = service.getByBrandCategory(b.getBrand(), b.getCategory());
		assertEquals(brandMasterPojo.getId(), b.getId());
		service.delete(brandMasterPojo.getId());
		// After delete throw exception while getting data
		service.getByBrandCategory(b.getBrand(), b.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetIdBlank() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// select data for given brand and category
		service.getByBrandCategory("", "");
	}

	@Test
	public void testGet() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		BrandMasterPojo p = service.get(b.getId());
		// check for inserted data
		assertEquals("viram", p.getBrand());
		assertEquals("shah", p.getCategory());
	}

	@Test
	public void testGetAll() throws ApiException {
		// test select all
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		BrandMasterPojo p = service.get(b.getId());
		p.setBrand("increff");
		p.setCategory("pos");
		service.update(b.getId(), p);
		BrandMasterPojo m = service.get(p.getId());
		// test updated data
		assertEquals("increff", m.getBrand());
		assertEquals("pos", m.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// test getCheck function
		BrandMasterPojo p = service.getCheck(b.getId());
		service.delete(p.getId());
		// After delete throw exception while getting data
		service.getCheck(p.getId());

	}

	@Test
	public void testNormalize() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.normalize(b);
		// test for normalized data
		assertEquals("viram", b.getBrand());
		assertEquals("shah", b.getCategory());

	}

	@Test
	public void testSearchData() throws ApiException {
		BrandMasterPojo brandMasterPojo1=getBrandMasterPojoTest();
		BrandMasterPojo brandMasterPojo2=getBrandMasterPojoTest();
		brandMasterPojo2.setBrand("Nestle             ");
		BrandMasterPojo brandMasterPojo3=getBrandMasterPojoTest();
		brandMasterPojo3.setCategory("POS");
		service.add(brandMasterPojo1);
		service.add(brandMasterPojo2);
		service.add(brandMasterPojo3);
		BrandMasterPojo brandMasterPojo=new BrandMasterPojo();
		brandMasterPojo.setBrand("   Vir        ");
		brandMasterPojo.setCategory("");
		List<BrandMasterPojo> list=service.searchBrandData(brandMasterPojo);
		assertEquals(2, list.size());
		brandMasterPojo.setCategory("p");
		list=service.searchBrandData(brandMasterPojo);
		assertEquals(1, list.size());
	}
	
//	@Test(expected = ApiException.class)
//	public void testCheckData() throws ApiException {
//		BrandMasterPojo b = getBrandMasterPojoTest();
//		service.checkData(b);
//		// throw exception
//		b.setBrand("");
//		service.checkData(b);
//	}

	private BrandMasterPojo getBrandMasterPojoTest() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		// create data
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		return b;
	}
}
