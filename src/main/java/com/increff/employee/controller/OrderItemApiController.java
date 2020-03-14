package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemApiController {

	@Autowired
	private OrderItemService iService;
	// CRUD operation for order item

	@ApiOperation(value = "Deletes a OrderItem")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		iService.delete(id);
	}

	@ApiOperation(value = "Gets a Order Item")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo p = iService.get(id);
		String barcode = p.getProductMasterPojo().getBarcode();
		return convert(p, barcode);
	}

	@ApiOperation(value = "Gets list of all Order Items")
	@RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
	public List<OrderItemData> getAll() throws ApiException {
		List<OrderItemPojo> list = iService.getAll();
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			String barcode = p.getProductMasterPojo().getBarcode();
			list2.add(convert(p, barcode));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a OrderItem")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		OrderItemPojo p = convert(f);
		iService.update(id, p);
	}

	// Converts OrderItemPojo to OrderItemData
	private OrderItemData convert(OrderItemPojo p, String barcode) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setOrderId(p.getOrderPojo().getId());
		d.setBarcode(barcode);
		d.setQuantity(p.getQuantity());
		d.setMrp(p.getSellingPrice());
		return d;
	}

	// Converts OrderItemForm to OrderItemPojo
	private OrderItemPojo convert(OrderItemForm f) {
		OrderItemPojo i = new OrderItemPojo();
		i.setQuantity(f.getQuantity());
		return i;
	}

}
