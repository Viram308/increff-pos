package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.ConverterUtil;

public class BrandDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto brandDto;
	@Autowired
	private ConverterUtil converterUtil;

	@Test
	public void testAddBrand() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm.brand, brandForm.category);
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
		List<BrandData> brandDatas = brandDto.searchBrand(brandForm3);
		assertEquals(1, brandDatas.size());
	}

	@Test
	public void testGetBrand() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm.brand, brandForm.category);
		BrandData brandData = brandDto.getBrandData(brandMasterPojo.getId());
		assertEquals("nestle", brandData.brand);
		assertEquals("dairy", brandData.category);
	}

	@Test
	public void testUpdateBrand() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm.brand, brandForm.category);
		BrandForm brandFormUpdate = getBrandForm("nestle", "FOOd    ");
		brandDto.updateBrand(brandMasterPojo.getId(), brandFormUpdate);
		BrandMasterPojo brandMasterPojoUpdate = brandDto.getByBrandCategory(brandFormUpdate.brand,
				brandFormUpdate.category);
		assertEquals("nestle", brandMasterPojoUpdate.getBrand());
		assertEquals("food", brandMasterPojoUpdate.getCategory());
	}

	@Test
	public void testGetByBrandCategory() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm.brand, brandForm.category);
		assertEquals("nestle", brandMasterPojo.getBrand());
		assertEquals("dairy", brandMasterPojo.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		BrandForm brandForm1 = getBrandForm("     nestLE        ", "DairY ");
		brandDto.checkData(brandForm1);
		// throw exception
		BrandForm brandForm2 = getBrandForm("    ", "");
		brandDto.checkData(brandForm2);
	}

	@Test(expected = ApiException.class)
	public void testCheckSearchData() throws ApiException {
		BrandForm brandForm1 = getBrandForm("     nestLE        ", "DairY ");
		brandDto.checkSearchData(brandForm1);
		BrandForm brandForm2 = getBrandForm(" a   ", "");
		brandDto.checkSearchData(brandForm2);
		// throw exception
		BrandForm brandForm3 = getBrandForm("    ", "");
		brandDto.checkSearchData(brandForm3);
	}

	@Test
	public void testGetBrandIdList() throws ApiException {
		BrandForm brandForm1 = getBrandForm("nestle", "dairy");
		brandDto.addBrand(brandForm1);
		BrandForm brandForm2 = getBrandForm("britania", "dairy");
		brandDto.addBrand(brandForm2);
		BrandMasterPojo brandMasterPojo1 = converterUtil.convertBrandFormtoBrandMasterPojo(brandForm1);
		BrandMasterPojo brandMasterPojo2 = converterUtil.convertBrandFormtoBrandMasterPojo(brandForm2);
		List<BrandMasterPojo> brandMasterPojolist = new ArrayList<BrandMasterPojo>();
		brandMasterPojolist.add(brandMasterPojo1);
		brandMasterPojolist.add(brandMasterPojo2);
		List<Integer> brandIds = brandDto.getBrandIdList(brandMasterPojolist);
		assertEquals(2, brandIds.size());
	}

	private BrandForm getBrandForm(String brand, String category) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = brand;
		brandForm.category = category;
		return brandForm;
	}

}
