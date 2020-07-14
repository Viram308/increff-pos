package com.increff.pos.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.model.ProductSearchForm;
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

	public InventoryPojo addInventory(InventoryForm form) throws ApiException {
		validateData(form);
		ProductMasterPojo productMasterPojo = productService.getByBarcode(form.barcode);
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productMasterPojo);
		inventoryPojo.setQuantity(form.quantity + inventoryPojo.getQuantity());
		return inventoryService.update(inventoryPojo.getId(), inventoryPojo);
	}

	public List<InventoryData> searchInventory(InventorySearchForm form) throws ApiException {
		ProductSearchForm productSearchForm = ConverterUtil.convertInventorySearchFormtoProductSearchForm(form);
		List<ProductMasterPojo> productMasterPojoList = productService.searchData(productSearchForm);
		List<Integer> productIds = productMasterPojoList.stream().map(o -> o.getId()).collect(Collectors.toList());
		List<InventoryPojo> list = inventoryService.getAll().stream()
				.filter(o -> (productIds.contains(o.getProductId()))).collect(Collectors.toList());
		return list.stream()
				.map(o -> ConverterUtil.convertInventoryPojotoInventoryData(o, productService.get(o.getProductId())))
				.collect(Collectors.toList());
	}

	public InventoryData getInventoryData(int id) throws ApiException {
		InventoryPojo inventoryPojo = inventoryService.get(id);
		ProductMasterPojo productMasterPojo = productService.get(inventoryPojo.getProductId());
		return ConverterUtil.convertInventoryPojotoInventoryData(inventoryPojo, productMasterPojo);
	}

	public InventoryPojo updateInventory(int id, InventoryForm form) throws ApiException {
		validateData(form);
		ProductMasterPojo productMasterPojo = productService.getByBarcode(form.barcode);
		InventoryPojo inventoryPojo = ConverterUtil.convertInventoryFormtoInventoryPojo(form, productMasterPojo);
		return inventoryService.update(id, inventoryPojo);
	}

	public List<InventoryData> getAllInventory() {
		List<InventoryPojo> list = inventoryService.getAll();
		return list.stream()
				.map(o -> ConverterUtil.convertInventoryPojotoInventoryData(o, productService.get(o.getProductId())))
				.collect(Collectors.toList());
	}

	public void validateData(InventoryForm inventoryForm) throws ApiException {
		if (inventoryForm.quantity < 0) {
			throw new ApiException("Quantity can not be negative for product : " + inventoryForm.barcode + " !!");
		}
	}

}
