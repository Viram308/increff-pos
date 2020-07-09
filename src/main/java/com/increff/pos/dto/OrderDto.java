package com.increff.pos.dto;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

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
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class OrderDto {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ConverterUtil converterUtil;
	@Autowired
	private OrderItemDto orderItemDto;

	@Transactional(rollbackOn = ApiException.class)
	public List<BillData> createOrder(OrderItemForm[] orderItemForms) throws ApiException {
		// Check entered inventory with available inventory
		List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		OrderPojo orderPojo = addOrder();
		// Convert input to required format
		List<OrderItemPojo> list = orderService.getOrderItemObject(orderItems, orderPojo);
		// Decrease inventory according to the entered quantity
		orderService.updateInventory(list);
		// Add each order item
		orderItemService.add(list);
		// Convert OrderItemPojo to BillData
		return orderService.getBillDataObject(list);
	}

	public OrderPojo addOrder() throws ApiException {
//		orderService.checkInventory(orderItems);
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(converterUtil.getDateTime());
		// Add order if inventory is available
		orderService.add(orderPojo);
		return orderPojo;
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<BillData> changeOrder(int id, OrderItemForm[] orderItemForms) throws ApiException {
		// Get order items according to orderId
		List<OrderItemData> orderItemDataList = orderItemDto.get(id);
		// add previous inventory
		addInInventory(orderItemDataList);
		// Check entered inventory with available inventory
		List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		// update order date and time
		OrderPojo orderPojo = updateOrder(id);
		// Convert input to required format
		List<OrderItemPojo> list = orderService.getOrderItemObject(orderItems, orderPojo);
		// Decrease inventory according to the entered quantity
		orderService.updateInventory(list);
		// Delete previous order items
		orderItemService.deleteByOrderId(id);
		// Add each order item
		orderItemService.add(list);
		// Convert OrderItemPojo to BillData
		return orderService.getBillDataObject(list);
	}

	public OrderPojo updateOrder(int id) throws ApiException {
		// orderService.checkInventory(orderItems);
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(converterUtil.getDateTime());
		// Add order if inventory is available
		orderService.update(id, orderPojo);
		orderPojo.setId(id);
		return orderPojo;
	}

	public void addInInventory(List<OrderItemData> orderItemDataList) throws ApiException {
		for (OrderItemData orderItemData : orderItemDataList) {
			ProductMasterPojo productMasterPojo = productService.getByBarcode(orderItemData.barcode);
			InventoryPojo inventoryPojo = inventoryService.getByProductId(productMasterPojo);
			InventoryPojo inventoryPojoFinal = new InventoryPojo();
			inventoryPojoFinal.setQuantity(orderItemData.quantity + inventoryPojo.getQuantity());
			inventoryService.update(inventoryPojo.getId(), inventoryPojoFinal);
		}
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
		List<OrderPojo> orderList = orderService.getList(orderPojo, form.startdate, form.enddate);
		if (orderList.isEmpty()) {
			throw new ApiException("There are no orders for given dates !!");
		}
		return converterUtil.getOrderDataList(orderList);
	}

	public void checkSearchData(OrderSearchForm form) throws ApiException {
		if (form.startdate.isBlank() || form.enddate.isBlank()) {
			throw new ApiException("Please enter start and end dates !!");
		}
	}

}
