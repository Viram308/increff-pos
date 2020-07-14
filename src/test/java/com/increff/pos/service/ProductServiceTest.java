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
	private ProductService productService;
	@Autowired
	private BrandService brandService;

	// test product service
	@Test
	public void testAdd() throws ApiException {
		ProductMasterPojo productMasterPojo = getProductMasterPojoTest();
		// Add one time
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		productService.add(productMasterPojo, brandMasterPojo);
		// create new data with same barcode
		ProductMasterPojo productMasterPojoFinal = new ProductMasterPojo();
		productMasterPojoFinal.setBrand_category_id(productMasterPojo.getBrand_category_id());
		productMasterPojoFinal.setMrp(productMasterPojo.getMrp());
		productMasterPojoFinal.setName(productMasterPojo.getName());
		productMasterPojoFinal.setBarcode(productMasterPojo.getBarcode());
		productService.add(productMasterPojoFinal, brandMasterPojo);
		// get barcode for id of first object
		String b1 = productService.get(productMasterPojo.getId()).getBarcode();
		// get barcode for very next object
		// and p.getId()+1
		String b2 = productService.get(productMasterPojo.getId() + 1).getBarcode();
		// test for different barcode
		assertNotEquals(b1, b2);
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcode() throws ApiException {
		ProductMasterPojo productMasterPojo = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		productService.add(productMasterPojo, brandMasterPojo);
		ProductMasterPojo productMasterPojo2 = productService.getByBarcode(productMasterPojo.getBarcode());
		assertEquals(productMasterPojo.getName(), productMasterPojo2.getName());
		assertEquals(productMasterPojo.getBrand_category_id(), productMasterPojo2.getBrand_category_id());
		assertEquals(productMasterPojo.getMrp(), productMasterPojo2.getMrp(), 0.01);

		// throw exception while getting data for unavailable barcode
		productService.getByBarcode("barcode");
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcodeBlank() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = brandService.get(p.getBrand_category_id());
		productService.add(p, brandMasterPojo);
		// throw exception
		productService.getByBarcode("");
	}

	@Test
	public void testGet() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = brandService.get(p.getBrand_category_id());
		productService.add(p, brandMasterPojo);
		ProductMasterPojo pr = productService.get(p.getId());
		// test added data
		assertEquals("product", pr.getName());
	}

	@Test
	public void testGetAll() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = brandService.get(p.getBrand_category_id());
		productService.add(p, brandMasterPojo);
		List<ProductMasterPojo> productMasterPojos=productService.getAll();
		assertEquals(1, productMasterPojos.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		ProductMasterPojo productMasterPojo = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		productService.add(productMasterPojo, brandMasterPojo);
		ProductMasterPojo productMasterPojoFinal = productService.get(productMasterPojo.getId());
		// update data
		productMasterPojoFinal.setName(" Check ");
		productService.update(productMasterPojoFinal.getId(), productMasterPojoFinal, brandMasterPojo);
		ProductMasterPojo pm = productService.get(productMasterPojoFinal.getId());
		// test updated data with normalization
		assertEquals("check", pm.getName());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		ProductMasterPojo productMasterPojo = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		productService.add(productMasterPojo, brandMasterPojo);

		ProductMasterPojo productMasterPojo2 = productService.getCheck(productMasterPojo.getId());
		assertEquals(productMasterPojo.getName(), productMasterPojo2.getName());
		assertEquals(productMasterPojo.getBrand_category_id(), productMasterPojo2.getBrand_category_id());
		assertEquals(productMasterPojo.getMrp(), productMasterPojo2.getMrp(), 0.01);

		// throw exception while getting data for id+1
		productService.getCheck(productMasterPojo2.getId() + 1);

	}

	@Test
	public void searchDataProductMasterPojo() throws ApiException {
		// create data
		ProductMasterPojo productMasterPojo1 = getProductMasterPojoTest();
		ProductMasterPojo productMasterPojo2 = getProductMasterPojoTest();
		BrandMasterPojo brandMasterPojo1 = brandService.get(productMasterPojo1.getBrand_category_id());
		BrandMasterPojo brandMasterPojo2 = brandService.get(productMasterPojo2.getBrand_category_id());
		productMasterPojo2.setName("munch");
		// add
		productService.add(productMasterPojo1, brandMasterPojo1);
		productService.add(productMasterPojo2, brandMasterPojo2);
		// create product search form
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = "";
		productSearchForm.name = " M       ";
		// search
		List<ProductMasterPojo> productMasterPojos = productService.searchData(productSearchForm);
		// test
		assertEquals(1, productMasterPojos.size());
	}

	private ProductMasterPojo getProductMasterPojoTest() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		// create data
		String barcode = StringUtil.getAlphaNumericString();
		b.setBrand(StringUtil.getAlphaNumericString());
		b.setCategory("ShaH");
		brandService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		return p;
	}
}
