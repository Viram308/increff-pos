package com.increff.employee.controller;

import java.util.List;

import javax.transaction.Transactional;

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
import com.increff.employee.util.ConverterUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/orderitem")
public class OrderItemApiController {

	@Autowired
	private OrderItemService iService;
	// CRUD operation for order item

	@ApiOperation(value = "Deletes a OrderItem")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		iService.delete(id);
	}

	@ApiOperation(value = "Gets a Order Item")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo p = iService.get(id);
		return ConverterUtil.convertOrderItemPojotoOrderItemData(p, p.getProductMasterPojo().getBarcode());
	}

	@ApiOperation(value = "Gets list of all Order Items")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<OrderItemData> getAll() throws ApiException {
		List<OrderItemPojo> list = iService.getAll();
		return ConverterUtil.getOrderItemDataList(list);
	}

	@Transactional(rollbackOn = ApiException.class)
	@ApiOperation(value = "Updates a OrderItem")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		iService.checkEnteredQuantity(f);
		iService.checkInventory(id, f);
		OrderItemPojo p = ConverterUtil.convertOrderItemFormtoOrderItemPojo(f);
		iService.update(id, p);

	}

}
