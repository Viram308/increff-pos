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
import com.increff.pos.spring.AbstractUnitTest;

public class NormalizeUtilTest extends AbstractUnitTest {

	// tests for normalization

	@Test
	public void testNormalizeBrandMasterPojo() throws ApiException {
		BrandMasterPojo brandMasterPojo = TestDataUtil.getBrandMasterPojo();
		NormalizeUtil.normalizeBrandMasterPojo(brandMasterPojo);
		// test for normalized data
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());

	}

	@Test
	public void testNormalizeBrandForm() {
		BrandForm brandForm = TestDataUtil.getBrandForm();
		NormalizeUtil.normalizeBrandForm(brandForm);
		// test for normalized data
		assertEquals("nestle", brandForm.brand);
		assertEquals("dairy", brandForm.category);
	}

	@Test
	public void testNormalizeProductMasterPojo() throws ApiException {
		ProductMasterPojo productMasterPojo = TestDataUtil.getProductMasterPojo();
		NormalizeUtil.normalizeProductMasterPojo(productMasterPojo);
		// test normalized data
		assertEquals("munch", productMasterPojo.getName());
	}

	@Test
	public void testNormalizeProductSearchForm() {
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchForm();
		NormalizeUtil.normalizeProductSearchForm(productSearchForm);
		// test normalized data
		assertEquals("munch", productSearchForm.name);
	}

	@Test
	public void testNormalizeUserForm() {
		UserForm userForm = TestDataUtil.getUserForm();
		NormalizeUtil.normalizeUserForm(userForm);
		// test normalized data
		assertEquals("shahviram308@gmail.com", userForm.getEmail());
		assertEquals("admin", userForm.getRole());
	}

	@Test
	public void testNormalizeUserPojo() {
		UserPojo userPojo = TestDataUtil.getUserPojo();
		NormalizeUtil.normalizeUserPojo(userPojo);
		// test normalized data
		assertEquals("shahviram308@gmail.com", userPojo.getEmail());
		assertEquals("standard", userPojo.getRole());
	}

}
