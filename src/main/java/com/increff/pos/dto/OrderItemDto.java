package com.increff.pos.dto;

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

	public void delete(int id) {
		orderItemService.delete(id);
	}

	public OrderItemData get(int id) throws ApiException {
		OrderItemPojo orderItemPojo = orderItemService.get(id);
		ProductMasterPojo productMasterPojo = productService.get(orderItemPojo.getProductId());
		return converterUtil.convertOrderItemPojotoOrderItemData(orderItemPojo, productMasterPojo.getBarcode());
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

	public void checkEnteredQuantity(OrderItemForm f) throws ApiException {
		if (f.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero !!");
		}
	}
}
