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

public class BrandDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto brandDto;

	@Test
	public void testAddBrand() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());
	}

	@Test
	public void testSearchBrand() throws ApiException {
		BrandForm brandForm1 = getBrandForm("nestle", "dairy");
		brandDto.addBrand(brandForm1);
		BrandForm brandForm2 = getBrandForm("britania", "dairy");
		brandDto.addBrand(brandForm2);
		BrandForm brandForm3 = getBrandForm("       nest     ", "");
		List<BrandData> brandDatas = brandDto.searchBrandData(brandForm3);
		assertEquals(1, brandDatas.size());
	}

	@Test
	public void testGetBrand() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		BrandData brandData = brandDto.getBrandData(brandMasterPojo.getId());
		assertEquals("nestle", brandData.brand);
		assertEquals("dairy", brandData.category);
	}

	@Test
	public void testUpdateBrand() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		BrandForm brandFormUpdate = getBrandForm("nestle", "FOOd    ");
		brandDto.updateBrand(brandMasterPojo.getId(), brandFormUpdate);
		BrandMasterPojo brandMasterPojoUpdate = brandDto.getByBrandCategory(brandFormUpdate);
		assertEquals("nestle", brandMasterPojoUpdate.getBrand());
		assertEquals("food", brandMasterPojoUpdate.getCategory());
	}

	@Test
	public void testGetByBrandCategory() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		BrandForm brandForm1 = getBrandForm("     nestLE        ", "DairY ");
		brandDto.validateData(brandForm1);
		// throw exception
		BrandForm brandForm2 = getBrandForm("    ", "");
		brandDto.validateData(brandForm2);
	}

	@Test
	public void testGetAll() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		List<BrandData> brandDatas = brandDto.getAllBrands();
		assertEquals(1, brandDatas.size());
	}
	
	private BrandForm getBrandForm(String brand, String category) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = brand;
		brandForm.category = category;
		return brandForm;
	}

}
