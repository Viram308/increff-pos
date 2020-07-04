package com.increff.pos.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConverterUtil;

@Component
public class BrandDto {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ConverterUtil converterUtil;

	public void addBrand(BrandForm form) throws ApiException {
		checkData(form);
		BrandMasterPojo brandPojo = converterUtil.convertBrandFormtoBrandMasterPojo(form);
		brandService.add(brandPojo);
	}

	public List<BrandData> searchBrand(BrandForm form) throws ApiException {
		checkSearchData(form);
		BrandMasterPojo brandPojo = converterUtil.convertBrandFormtoBrandMasterPojo(form);
		List<BrandMasterPojo> list = brandService.searchData(brandPojo);
		return converterUtil.getBrandDataList(list);
	}

	public BrandData getBrandData(int id) {
		BrandMasterPojo brandPojo = brandService.get(id);
		return converterUtil.convertBrandMasterPojotoBrandData(brandPojo);
	}

	public void updateBrand(int id, BrandForm form) throws ApiException {
		checkData(form);
		BrandMasterPojo brandPojo = converterUtil.convertBrandFormtoBrandMasterPojo(form);
		brandService.update(id, brandPojo);
	}

	public List<BrandData> getAllBrands() {
		List<BrandMasterPojo> list = brandService.getAll();
		return converterUtil.getBrandDataList(list);
	}

	public void checkData(BrandForm b) throws ApiException {
		if (b.getBrand().isBlank() || b.getCategory().isBlank()) {
			throw new ApiException("Please enter brand and category !!");
		}
	}
	
	public void checkSearchData(BrandForm b) throws ApiException {
		if (b.getBrand().isBlank() && b.getCategory().isBlank()) {
			throw new ApiException("Please enter brand or category !!");
		}
	}
}
