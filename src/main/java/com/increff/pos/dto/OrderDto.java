package com.increff.pos.dto;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BillData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.OrderSearchForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class OrderDto {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ConverterUtil converterUtil;

	public List<OrderItemForm> groupItemsByBarcode(OrderItemForm[] orderItemForms) {
		return orderService.groupItemsByBarcode(orderItemForms);
	}

	public OrderPojo addOrder(List<OrderItemForm> orderItems) throws ApiException {
		orderService.checkInventory(orderItems);
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(converterUtil.getDateTime());
		// Add order if inventory is available
		orderService.add(orderPojo);
		return orderPojo;
	}

	public OrderPojo updateOrder(int id,List<OrderItemForm> orderItems) throws ApiException {
		orderService.checkInventory(orderItems);
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(converterUtil.getDateTime());
		// Add order if inventory is available
		orderService.update(id,orderPojo);
		orderPojo.setId(id);
		return orderPojo;
	}

	public void addInInventory(List<OrderItemData> orderItemDataList) throws ApiException {
		for (OrderItemData orderItemData : orderItemDataList) {
			ProductMasterPojo productMasterPojo = productService.getByBarcode(orderItemData.getBarcode());
			InventoryPojo inventoryPojo = inventoryService.getByProductId(productMasterPojo);
			InventoryPojo inventoryPojoFinal = new InventoryPojo();
			inventoryPojoFinal.setQuantity(orderItemData.getQuantity() + inventoryPojo.getQuantity());
			inventoryService.update(inventoryPojo.getId(), inventoryPojoFinal);
		}
	}

	public List<OrderItemPojo> getOrderItemObject(List<OrderItemForm> orderItems, OrderPojo orderPojo)
			throws ApiException {
		return orderService.getOrderItemObject(orderItems, orderPojo);
	}

	public void updateInventory(List<OrderItemPojo> list) throws ApiException {
		orderService.updateInventory(list);
	}

	public List<BillData> getBillDataObject(List<OrderItemPojo> list) throws ApiException {
		return orderService.getBillDataObject(list);
	}

	public OrderData get(int id) throws ApiException {
		OrderPojo orderPojo = orderService.get(id);
		return converterUtil.convertOrderPojotoOrderData(orderPojo);
	}

	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		return converterUtil.getOrderDataList(list);
	}

	public List<OrderData> searchOrder(OrderSearchForm form) throws ParseException, ApiException {
		checkSearchData(form);
		List<OrderPojo> orderPojo = orderService.getAll();
		// Get list of order ids
		List<OrderPojo> orderList = orderService.getList(orderPojo, form.getStartdate(), form.getEnddate());
		if (orderList.isEmpty()) {
			throw new ApiException("There are no orders for given dates !!");
		}
		return converterUtil.getOrderDataList(orderList);
	}

	public void checkSearchData(OrderSearchForm form) throws ApiException {
		if (form.getStartdate().isBlank() && form.getEnddate().isBlank()) {
			throw new ApiException("Please enter start and end dates !!");
		}
	}

}
