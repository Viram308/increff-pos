package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
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

	

	@Test(expected = ApiException.class)
	public void testGetByBrandCategory() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// select data for given brand and category
		BrandForm brandForm = getBrandForm();
		BrandMasterPojo brandMasterPojo = service.getByBrandCategory(brandForm);
		assertEquals(brandMasterPojo.getId(), b.getId());
		brandForm.brand="a";
		brandForm.category="b";		
		brandMasterPojo = service.getByBrandCategory(brandForm);
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
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// test select all
		List<BrandMasterPojo> brandMasterPojos = service.getAll();
		assertEquals(1, brandMasterPojos.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		b=service.add(b);
		BrandMasterPojo p = new BrandMasterPojo();
		p.setBrand("increff");
		p.setCategory("pos");
		p=service.update(b.getId(), p);
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
		// throw exception while getting data of id+1
		service.getCheck(p.getId()+1);
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
		BrandForm brandForm = getBrandForm();
		List<BrandMasterPojo> list=service.searchBrandData(brandForm);
		assertEquals(1, list.size());
		brandForm.category="";
		list=service.searchBrandData(brandForm);
		assertEquals(2, list.size());
	}

	private BrandForm getBrandForm() {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = "viram";
		brandForm.category = "shah";
		return brandForm;
	}
	
	private BrandMasterPojo getBrandMasterPojoTest() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		// create data
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		return b;
	}
}
