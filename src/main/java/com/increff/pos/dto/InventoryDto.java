package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class InventoryDto {

	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ConverterUtil converterUtil;

	@Transactional
	public void addInventory(InventoryForm form) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.getByBarcode(form.getBarcode());
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productMasterPojo);
		inventoryPojo.setQuantity(form.getQuantity() + inventoryPojo.getQuantity());
		inventoryService.update(inventoryPojo.getId(), inventoryPojo);
	}

	public List<InventoryData> searchInventory(InventorySearchForm form) throws ApiException {
		checkSearchData(form);
		ProductMasterPojo productMasterPojo = new ProductMasterPojo();
		productMasterPojo.setBarcode(form.getBarcode());
		productMasterPojo.setName(form.getName());
		List<ProductMasterPojo> productMasterPojoList = productService.searchData(productMasterPojo);
		List<Integer> productIds = getProductIdList(productMasterPojoList);
		List<InventoryPojo> list = inventoryService.searchData(productIds);
		return converterUtil.getInventoryDataList(list);
	}

	public InventoryData getInventoryData(int id) throws ApiException {
		InventoryPojo inventoryPojo = inventoryService.get(id);
		ProductMasterPojo productMasterPojo = productService.get(inventoryPojo.getProductid());
		return converterUtil.convertInventoryPojotoInventoryData(inventoryPojo, productMasterPojo);
	}

	public void updateInventory(int id, InventoryForm form) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.getByBarcode(form.getBarcode());
		InventoryPojo inventoryPojo = converterUtil.convertInventoryFormtoInventoryPojo(form, productMasterPojo);
		checkData(inventoryPojo, productMasterPojo);
		inventoryService.update(id, inventoryPojo);
	}

	public List<InventoryData> getAllInventory() {
		List<InventoryPojo> list = inventoryService.getAll();
		return converterUtil.getInventoryDataList(list);
	}

	public List<Integer> getProductIdList(List<ProductMasterPojo> productMasterPojoList) {
		List<Integer> productIdList = new ArrayList<Integer>();
		for (ProductMasterPojo productMasterPojo : productMasterPojoList) {
			productIdList.add(productMasterPojo.getId());
		}
		return productIdList;
	}

	public void checkData(InventoryPojo inventoryPojo, ProductMasterPojo productMasterPojo) throws ApiException {
		if (inventoryPojo.getQuantity() <= 0) {
			throw new ApiException(
					"Quantity can not be negative or zero for product : " + productMasterPojo.getBarcode() + " !!");
		}
	}

	public void checkSearchData(InventorySearchForm inventorySearchForm) throws ApiException {
		if (inventorySearchForm.getBarcode().isBlank() && inventorySearchForm.getName().isBlank()) {
			throw new ApiException("Please enter name or barcode !!");
		}
	}

}
