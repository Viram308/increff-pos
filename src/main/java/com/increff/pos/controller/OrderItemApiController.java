package com.increff.pos.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/orderitem")
public class OrderItemApiController {

	@Autowired
	private OrderItemService service;

	@Autowired
	private OrderItemDto orderItemDto;
	// CRUD operation for order item

	@ApiOperation(value = "Deletes a OrderItem")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Order Item")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo orderItemPojo = service.get(id);
		return orderItemDto.convertOrderItemPojotoOrderItemData(orderItemPojo);
	}

	@ApiOperation(value = "Gets list of all Order Items")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<OrderItemData> getAll() throws ApiException {
		List<OrderItemPojo> list = service.getAll();
		return orderItemDto.getOrderItemDataList(list);
	}

	@Transactional(rollbackOn = ApiException.class)
	@ApiOperation(value = "Updates a OrderItem")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		service.checkEnteredQuantity(f);
		service.checkInventory(id, f);
		OrderItemPojo p = orderItemDto.convertOrderItemFormtoOrderItemPojo(f);
		service.update(id, p);

	}

}
