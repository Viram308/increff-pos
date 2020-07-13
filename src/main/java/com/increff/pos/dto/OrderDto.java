package com.increff.pos.dto;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.model.BillData;
import com.increff.pos.model.InfoData;
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
	private OrderItemDto orderItemDto;
	@Autowired
	private InfoData info;

	@Transactional(rollbackFor = ApiException.class)
	public List<BillData> createOrder(OrderItemForm[] orderItemForms) throws ApiException {
		// Check entered inventory with available inventory
		List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		OrderPojo orderPojo = addOrder(orderItems);
		// Convert input to required format
		List<OrderItemPojo> list = orderItems.stream().map(o -> ConverterUtil.convertOrderItemFormToOrderItemPojo(o,
				orderPojo, productService.getByBarcode(o.barcode))).collect(Collectors.toList());
		// Decrease inventory according to the entered quantity
		orderService.updateInventory(list);
		// Add each order item
		orderItemService.add(list);
		// Convert OrderItemPojo to BillData
		return orderService.getBillDataObject(list);
	}

	public OrderPojo addOrder(List<OrderItemForm> orderItems) throws ApiException {
		orderService.checkInventory(orderItems);
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(ConverterUtil.getDateTime());
		orderPojo.setOrderCreater(info.getEmail());
		// Add order if inventory is available
		orderService.add(orderPojo);
		return orderPojo;
	}

	@Transactional(rollbackFor = ApiException.class)
	public List<BillData> changeOrder(int id, OrderItemForm[] orderItemForms) throws ApiException {
		// Get order items according to orderId
		List<OrderItemData> orderItemDataList = orderItemDto.get(id);
		// add previous inventory
		addInInventory(orderItemDataList);
		// Check entered inventory with available inventory
		List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		// update order date and time
		OrderPojo orderPojo = updateOrder(id, orderItems);
		// Convert input to required format
		List<OrderItemPojo> list = orderItems.stream().map(o -> ConverterUtil.convertOrderItemFormToOrderItemPojo(o,
				orderPojo, productService.getByBarcode(o.barcode))).collect(Collectors.toList());
		// Decrease inventory according to the entered quantity
		orderService.updateInventory(list);
		// Delete previous order items
		orderItemService.deleteByOrderId(id);
		// Add each order item
		orderItemService.add(list);
		// Convert OrderItemPojo to BillData
		return orderService.getBillDataObject(list);
	}

	public OrderPojo updateOrder(int id, List<OrderItemForm> orderItems) throws ApiException {
		orderService.checkInventory(orderItems);
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(ConverterUtil.getDateTime());
		orderPojo.setOrderCreater(info.getEmail());
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
		return ConverterUtil.convertOrderPojotoOrderData(orderPojo, orderItemService.getByOrderId(orderPojo.getId()));
	}

	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		return list.stream()
				.map(o -> ConverterUtil.convertOrderPojotoOrderData(o, orderItemService.getByOrderId(o.getId())))
				.collect(Collectors.toList());
	}

	public List<OrderData> searchOrder(OrderSearchForm form) throws ApiException, ParseException {
		List<OrderPojo> orderPojo = orderService.searchOrder(form);
		if (!form.startdate.isBlank() && !form.enddate.isBlank()) {
			orderPojo = orderService.getList(orderPojo, form.startdate, form.enddate);
		}
		if (form.orderId == 0) {
			return orderPojo.stream()
					.map(o -> ConverterUtil.convertOrderPojotoOrderData(o, orderItemService.getByOrderId(o.getId())))
					.collect(Collectors.toList());
		}
		orderPojo = orderPojo.stream().filter(o -> (form.orderId == o.getId())).collect(Collectors.toList());
		return orderPojo.stream()
				.map(o -> ConverterUtil.convertOrderPojotoOrderData(o, orderItemService.getByOrderId(o.getId())))
				.collect(Collectors.toList());
	}

	public void checkSearchData(OrderSearchForm form) throws ApiException {
		if (form.startdate.isBlank() || form.enddate.isBlank()) {
			throw new ApiException("Please enter start and end dates !!");
		}
	}

}
