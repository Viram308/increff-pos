package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.StringUtil;

public class ProductServiceTest extends AbstractUnitTest {
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService bService;

	// test product service
	@Test
	public void testAdd() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		// Add one time
		BrandMasterPojo brandMasterPojo = bService.get(p.getBrand_category_id());
		service.add(p, brandMasterPojo);
		// create new data with same barcode
		ProductMasterPojo p1 = new ProductMasterPojo();
		p1.setBrand_category_id(p.getBrand_category_id());
		p1.setMrp(p.getMrp());
		p1.setName(p.getName());
		p1.setBarcode(p.getBarcode());
		service.add(p1, brandMasterPojo);
		// get barcode for id of first object
		String b1 = service.get(p.getId()).getBarcode();
		// get barcode for very next object
		// and p.getId()+1
		String b2 = service.get(p.getId() + 1).getBarcode();
		// test for different barcode
		assertNotEquals(b1, b2);
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcode() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = bService.get(p.getBrand_category_id());
		service.add(p, brandMasterPojo);
		service.getByBarcode(p.getBarcode());
		// throw exception while getting data for unavailable barcode
		service.getByBarcode("barcode");
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcodeBlank() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = bService.get(p.getBrand_category_id());
		service.add(p, brandMasterPojo);
		service.getByBarcode("");
	}

	@Test
	public void testGet() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = bService.get(p.getBrand_category_id());
		service.add(p, brandMasterPojo);
		ProductMasterPojo pr = service.get(p.getId());
		// test added data
		assertEquals("product", pr.getName());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = bService.get(p.getBrand_category_id());
		service.add(p, brandMasterPojo);
		ProductMasterPojo pr = service.get(p.getId());
		// update data
		pr.setName(" Check ");
		service.update(pr.getId(), pr, brandMasterPojo);
		ProductMasterPojo pm = service.get(pr.getId());
		// test updated data with normalization
		assertEquals("check", pm.getName());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = bService.get(p.getBrand_category_id());
		service.add(p, brandMasterPojo);

		ProductMasterPojo pr = service.getCheck(p.getId());
		// After delete throw exception while getting data
		service.getCheck(pr.getId() + 1);

	}

	@Test
	public void searchDataProductMasterPojo() throws ApiException {
		ProductMasterPojo productMasterPojo1 = getProductMasterPojoTest();
		ProductMasterPojo productMasterPojo2 = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo1 = bService.get(productMasterPojo1.getBrand_category_id());
		BrandMasterPojo brandMasterPojo2 = bService.get(productMasterPojo2.getBrand_category_id());
		productMasterPojo2.setName("munch");
		service.add(productMasterPojo1, brandMasterPojo1);
		service.add(productMasterPojo2, brandMasterPojo2);
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = "";
		productSearchForm.name = " M       ";
		List<ProductMasterPojo> productMasterPojos = service.searchData(productSearchForm);
		assertEquals(1, productMasterPojos.size());
	}

	private ProductMasterPojo getProductMasterPojoTest() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		// create data
		String barcode = StringUtil.getAlphaNumericString();
		b.setBrand(StringUtil.getAlphaNumericString());
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		return p;
	}
}
