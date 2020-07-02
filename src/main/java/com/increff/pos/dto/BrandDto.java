package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.ApiException;

@Component
public class BrandDto {
	@Autowired
	private ModelMapper modelMapper;

	// Converts BrandForm to BrandMasterPojo
	public BrandMasterPojo convertBrandFormtoBrandMasterPojo(BrandForm form) throws ApiException {
		BrandMasterPojo brandMasterPojo = modelMapper.map(form, BrandMasterPojo.class);
		checkData(brandMasterPojo);
		return brandMasterPojo;
	}

	// Converts BrandMasterPojo to BrandData
	public BrandData convertBrandMasterPojotoBrandData(BrandMasterPojo brandMasterPojo) {
		return modelMapper.map(brandMasterPojo, BrandData.class);
	}

	// Converts list of BrandMasterPojo to list of BrandData
	public List<BrandData> getBrandDataList(List<BrandMasterPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandMasterPojo brandMasterPojo : list) {
			BrandData brandData = modelMapper.map(brandMasterPojo, BrandData.class);
			list2.add(brandData);
		}
		return list2;
	}
	
	
	public void checkData(BrandMasterPojo b) throws ApiException {
		if (b.getBrand().isBlank() || b.getCategory().isBlank()) {
			throw new ApiException("Please enter brand and category !!");
		}
	}
}
