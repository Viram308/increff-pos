package com.increff.pos.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;

public class NormalizeUtilTest {

	@Test
	public void testNormalizeBrandMasterPojo() throws ApiException {
		BrandMasterPojo brandMasterPojo = getBrandMasterPojoTest();
		NormalizeUtil.normalizeBrandMasterPojo(brandMasterPojo);
		// test for normalized data
		assertEquals("viram", brandMasterPojo.getBrand());
		assertEquals("shah", brandMasterPojo.getCategory());

	}

	@Test
	public void testNormalizeBrandForm() {
		BrandForm brandForm = getBrandForm();
		NormalizeUtil.normalizeBrandForm(brandForm);
		// test for normalized data
		assertEquals("viram", brandForm.brand);
		assertEquals("shah", brandForm.category);
	}

	@Test
	public void testNormalizeProductMasterPojo() throws ApiException {
		ProductMasterPojo productMasterPojo = getProductMasterPojo();
		NormalizeUtil.normalizeProductMasterPojo(productMasterPojo);
		// test normalized data
		assertEquals("munch", productMasterPojo.getName());
	}

	@Test
	public void testNormalizeProductSearchForm() {
		ProductSearchForm productSearchForm = getProductSearchForm();
		NormalizeUtil.normalizeProductSearchForm(productSearchForm);
		// test normalized data
		assertEquals("munch", productSearchForm.name);
	}

	@Test
	public void testNormalizeUserForm() {
		UserForm userForm = getUserForm();
		NormalizeUtil.normalizeUserForm(userForm);
		// test normalized data
		assertEquals("shahviram308@gmail.com", userForm.getEmail());
		assertEquals("admin", userForm.getRole());
	}

	@Test
	public void testNormalizeUserPojo() {
		UserPojo userPojo = getUserPojo();
		NormalizeUtil.normalizeUserPojo(userPojo);
		// test normalized data
		assertEquals("shahviram308@gmail.com", userPojo.getEmail());
		assertEquals("admin", userPojo.getRole());
	}

	private UserForm getUserForm() {
		UserForm userForm = new UserForm();
		userForm.setEmail(" Shahviram308@gmail.coM ");
		userForm.setPassword("admin");
		userForm.setRole(" AdmiN ");
		return userForm;
	}

	private UserPojo getUserPojo() {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		return u;
	}

	private ProductSearchForm getProductSearchForm() {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = StringUtil.getAlphaNumericString();
		productSearchForm.name = " MUNch       ";
		return productSearchForm;
	}

	private ProductMasterPojo getProductMasterPojo() {
		ProductMasterPojo productMasterPojo = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		String name = " MunCH   ";
		productMasterPojo.setBarcode(barcode);
		productMasterPojo.setName(name);
		return productMasterPojo;
	}

	private BrandForm getBrandForm() {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = "   vIram ";
		brandForm.category = " ShaH ";
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
