package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;

@Component
public class ReportDto {
	@Autowired
	private ModelMapper modelMapper;

	public List<BrandData> convertToBrandData(List<BrandMasterPojo> list) {
		// Converts list of BrandMasterPojo to list of BrandData
		List<BrandData> list2 = new ArrayList<BrandData>();
		int i = 0;
		for (BrandMasterPojo brandMasterPojo : list) {
			i++;
			BrandData brandData = modelMapper.map(brandMasterPojo, BrandData.class);
			brandData.setId(i);
			list2.add(brandData);
		}
		return list2;
	}

	public List<InventoryReportData> convertToInventoryReportData(List<InventoryPojo> ip) {
		List<InventoryReportData> list2 = new ArrayList<InventoryReportData>();
		int i = 0;
		// Converts InventoryPojo to InventoryReportData
		for (InventoryPojo inventoryPojo : ip) {
			i++;
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			InventoryReportData inventoryReportData = modelMapper.map(inventoryPojo, InventoryReportData.class);
			inventoryReportData.setId(i);
			list2.add(inventoryReportData);
		}
		return list2;
	}
}
