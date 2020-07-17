package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtil;

public class BrandServiceTest extends AbstractUnitTest {
	@Autowired
	private BrandService brandService;

	// test brand service
	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		// Add one time
		brandMasterPojo = brandService.add(brandMasterPojo);
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());
		// Throw exception while entering second time
		brandService.add(brandMasterPojo);
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandCategory() throws ApiException {
		BrandMasterPojo brandMasterPojo1 = TestUtil.getBrandMasterPojo();
		brandService.add(brandMasterPojo1);
		// select data for given brand and category
		BrandForm brandForm = TestUtil.getBrandForm();
		BrandMasterPojo brandMasterPojo = brandService.getByBrandCategory(brandForm);
		assertEquals(brandMasterPojo.getId(), brandMasterPojo1.getId());
		brandForm.brand = "a";
		brandForm.category = "b";
		brandMasterPojo = brandService.getByBrandCategory(brandForm);
	}

	@Test
	public void testGet() throws ApiException {
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		brandService.add(brandMasterPojo);
		BrandMasterPojo p = brandService.get(brandMasterPojo.getId());
		// check for inserted data
		assertEquals("nestle", p.getBrand());
		assertEquals("dairy", p.getCategory());
	}

	@Test
	public void testGetAll() throws ApiException {
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		brandService.add(brandMasterPojo);
		// test select all
		List<BrandMasterPojo> brandMasterPojos = brandService.getAll();
		assertEquals(1, brandMasterPojos.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		brandMasterPojo = brandService.add(brandMasterPojo);
		BrandMasterPojo brandMasterPojoUpdate = new BrandMasterPojo();
		brandMasterPojoUpdate.setBrand("increff");
		brandMasterPojoUpdate.setCategory("pos");
		brandMasterPojoUpdate = brandService.update(brandMasterPojo.getId(), brandMasterPojoUpdate);
		BrandMasterPojo brandMasterPojoResult = brandService.get(brandMasterPojoUpdate.getId());
		// test updated data
		assertEquals("increff", brandMasterPojoResult.getBrand());
		assertEquals("pos", brandMasterPojoResult.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		brandService.add(brandMasterPojo);
		// test getCheck function
		BrandMasterPojo brandMasterPojoResult = brandService.getCheck(brandMasterPojo.getId());
		// throw exception while getting data of id+1
		brandService.getCheck(brandMasterPojoResult.getId() + 1);
	}

	@Test
	public void testSearchData() throws ApiException {
		// create data
		BrandMasterPojo brandMasterPojo1 = TestUtil.getBrandMasterPojo();
		BrandMasterPojo brandMasterPojo2 = TestUtil.getBrandMasterPojo();
		brandMasterPojo2.setBrand("Britania             ");
		BrandMasterPojo brandMasterPojo3 = TestUtil.getBrandMasterPojo();
		brandMasterPojo3.setCategory("POS");
		// add
		brandService.add(brandMasterPojo1);
		brandService.add(brandMasterPojo2);
		brandService.add(brandMasterPojo3);
		BrandForm brandForm = TestUtil.getBrandForm();
		// search
		List<BrandMasterPojo> list = brandService.searchBrandData(brandForm);
		// test list size
		assertEquals(1, list.size());
		brandForm.category = "";
		list = brandService.searchBrandData(brandForm);
		assertEquals(2, list.size());
	}

}
