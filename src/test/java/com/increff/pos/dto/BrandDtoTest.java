package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestDataUtil;

public class BrandDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto brandDto;

	@Test
	public void testAddBrand() throws ApiException {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		// add
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		// test added data
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());
	}

	@Test
	public void testSearchBrand() throws ApiException {
		// add
		BrandForm brandForm1 = TestDataUtil.getBrandFormDto("nestle", "dairy");
		brandDto.addBrand(brandForm1);
		BrandForm brandForm2 = TestDataUtil.getBrandFormDto("britania", "dairy");
		brandDto.addBrand(brandForm2);
		// search
		BrandForm brandForm3 = TestDataUtil.getBrandFormDto("       nest     ", "");
		List<BrandData> brandDatas = brandDto.searchBrandData(brandForm3);
		// test
		assertEquals(1, brandDatas.size());
	}

	@Test
	public void testGetBrand() throws ApiException {
		// add
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		// get data
		BrandData brandData = brandDto.getBrandData(brandMasterPojo.getId());
		assertEquals("nestle", brandData.brand);
		assertEquals("dairy", brandData.category);
	}

	@Test
	public void testUpdateBrand() throws ApiException {
		// add
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		// create update form
		BrandForm brandFormUpdate = TestDataUtil.getBrandFormDto("nestle", "FOOd    ");
		brandDto.updateBrand(brandMasterPojo.getId(), brandFormUpdate);
		BrandMasterPojo brandMasterPojoUpdate = brandDto.getByBrandCategory(brandFormUpdate);
		// test update
		assertEquals("nestle", brandMasterPojoUpdate.getBrand());
		assertEquals("food", brandMasterPojoUpdate.getCategory());
	}

	@Test
	public void testGetByBrandCategory() throws ApiException {
		// add
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		// get data
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		// test
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testValidateData() throws ApiException {
		BrandForm brandForm1 = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		// validate
		brandDto.validateData(brandForm1);
		// throw exception
		BrandForm brandForm2 = TestDataUtil.getBrandFormDto("    ", "");
		brandDto.validateData(brandForm2);
	}

	@Test
	public void testGetAll() throws ApiException {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		// get all data
		List<BrandData> brandDatas = brandDto.getAllBrands();
		assertEquals(1, brandDatas.size());
	}

}
