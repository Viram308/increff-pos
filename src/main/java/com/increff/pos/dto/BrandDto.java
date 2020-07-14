package com.increff.pos.dto;

import java.util.List;
import java.util.stream.Collectors;

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

	public BrandMasterPojo addBrand(BrandForm form) throws ApiException {
		validateData(form);
		BrandMasterPojo brandPojo = ConverterUtil.convertBrandFormtoBrandMasterPojo(form);
		return brandService.add(brandPojo);
	}

	public List<BrandData> searchBrandData(BrandForm form) throws ApiException {
		List<BrandMasterPojo> list = brandService.searchBrandData(form);
		// map BrandMasterPojo to BrandData
		return list.stream().map(o -> ConverterUtil.convertBrandMasterPojotoBrandData(o)).collect(Collectors.toList());
	}

	public BrandData getBrandData(int id) {
		BrandMasterPojo brandPojo = brandService.get(id);
		return ConverterUtil.convertBrandMasterPojotoBrandData(brandPojo);
	}

	public BrandMasterPojo updateBrand(int id, BrandForm form) throws ApiException {
		validateData(form);
		BrandMasterPojo brandPojo = ConverterUtil.convertBrandFormtoBrandMasterPojo(form);
		return brandService.update(id, brandPojo);
	}

	public BrandMasterPojo getByBrandCategory(BrandForm brandForm) throws ApiException {
		return brandService.getByBrandCategory(brandForm);
	}

	public List<BrandData> getAllBrands() {
		List<BrandMasterPojo> list = brandService.getAll();
		// map BrandMasterPojo to BrandData
		return list.stream().map(o -> ConverterUtil.convertBrandMasterPojotoBrandData(o)).collect(Collectors.toList());
	}

	public void validateData(BrandForm b) throws ApiException {
		if (b.brand.isBlank() || b.category.isBlank()) {
			throw new ApiException("Please enter brand and category !!");
		}
	}

}
