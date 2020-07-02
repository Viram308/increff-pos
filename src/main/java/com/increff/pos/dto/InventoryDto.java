package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;

@Component
public class InventoryDto {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductService pService;

	// Converts InventoryPojo to InventoryData
	public InventoryData convertInventoryPojotoInventoryData(InventoryPojo inventoryPojo) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return modelMapper.map(inventoryPojo, InventoryData.class);
	}

	// Converts InventoryForm to InventoryPojo
	public InventoryPojo convertInventoryFormtoInventoryPojo(InventoryForm form) throws ApiException {
		InventoryPojo inventoryPojo = modelMapper.map(form, InventoryPojo.class);
		inventoryPojo.setProductMasterPojo(pService.getByBarcode(form.getBarcode()));
		checkData(inventoryPojo);
		return inventoryPojo;
	}

	// Converts list of InventoryPojo to list of InventoryData
	public List<InventoryData> getInventoryDataList(List<InventoryPojo> list) {
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo inventoryPojo : list) {
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			InventoryData inventoryData = modelMapper.map(inventoryPojo, InventoryData.class);
			list2.add(inventoryData);
		}
		return list2;
	}

	public void checkData(InventoryPojo i) throws ApiException {
		if (i.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero for product : "
					+ i.getProductMasterPojo().getBarcode() + " !!");
		}
	}

}
