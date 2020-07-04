package com.increff.pos.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BillData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConverterUtil;

@Component
public class OrderDto {
	@Autowired
	private OrderService orderService;
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

	public void delete(int id) {
		orderService.delete(id);
	}

	public OrderData get(int id) throws ApiException {
		OrderPojo orderPojo = orderService.get(id);
		return converterUtil.convertOrderPojotoOrderData(orderPojo);
	}

	public List<OrderData> getAll() {
		List<OrderPojo> list = orderService.getAll();
		return converterUtil.getOrderDataList(list);
	}
}
