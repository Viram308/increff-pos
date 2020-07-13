package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/admin/orderitem")
public class OrderItemApiController {

	@Autowired
	private OrderItemDto orderItemDto;
	// CRUD operation for order item

	@ApiOperation(value = "Gets Order Items for order")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public List<OrderItemData> get(@PathVariable int id) throws ApiException {
		return orderItemDto.get(id);
	}
	

	@ApiOperation(value = "Search Order Items")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public List<OrderItemData> search(@RequestBody OrderItemData orderItemData) throws ApiException {
		return orderItemDto.searchOrderItem(orderItemData);
	}

}
