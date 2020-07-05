package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class OrderItemDto {
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ConverterUtil converterUtil;

	public void add(List<OrderItemPojo> list) {
		orderItemService.add(list);
	}

	public void deleteByOrderId(int id) {
		orderItemService.deleteByOrderId(id);
	}

	public List<OrderItemData> get(int orderId) throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
		return converterUtil.getOrderItemDataList(orderItemPojoList);
	}

	public List<OrderItemData> searchOrderItem(OrderItemData orderItemData) throws ApiException {
		checkSearchData(orderItemData);
		ProductMasterPojo productMasterPojo = new ProductMasterPojo();
		productMasterPojo.setBarcode(orderItemData.getBarcode());
		productMasterPojo.setName(orderItemData.getName());
		List<ProductMasterPojo> productMasterPojoList = productService.searchData(productMasterPojo);
		List<Integer> productIds = getProductIdList(productMasterPojoList);
		List<OrderItemPojo> orderItemPojos = orderItemService.searchData(orderItemData, productIds);
		return converterUtil.getOrderItemDataList(orderItemPojos);
	}

	public void checkSearchData(OrderItemData orderItemData) throws ApiException {
		if (orderItemData.getBarcode().isBlank() && orderItemData.getName().isBlank()
				&& orderItemData.getOrderId() <= 0) {
			throw new ApiException("Please enter anyone(barcode,name,orderId) !!");
		}
	}

	public void update(int id, OrderItemForm form) throws ApiException {
		checkEnteredQuantity(form);
		orderItemService.checkInventory(id, form);
		OrderItemPojo p = converterUtil.convertOrderItemFormtoOrderItemPojo(form);
		orderItemService.update(id, p);
	}

	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = orderItemService.getAll();
		return converterUtil.getOrderItemDataList(list);
	}

	public List<Integer> getProductIdList(List<ProductMasterPojo> productMasterPojoList) {
		List<Integer> productIdList = new ArrayList<Integer>();
		for (ProductMasterPojo productMasterPojo : productMasterPojoList) {
			productIdList.add(productMasterPojo.getId());
		}
		return productIdList;
	}

	public void checkEnteredQuantity(OrderItemForm f) throws ApiException {
		if (f.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero !!");
		}
	}

}
