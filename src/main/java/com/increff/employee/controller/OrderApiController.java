package com.increff.employee.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderService oService;

	@Autowired
	private OrderItemService iService;

	@Autowired
	private ProductService pService;

	@ApiOperation(value = "Adds Order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm[] orderItems) throws ApiException {
		OrderPojo o = new OrderPojo();
		o.setDatetime(getDateTime());
		oService.add(o);
		List<OrderItemPojo> list = getOrderItemObject(orderItems);
		iService.add(list);
	}

//	@ApiOperation(value = "Deletes a Brand")
//	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
//	public void delete(@PathVariable int id) {
//		oService.delete(id);
//	}
//
//	@ApiOperation(value = "Gets a Brand")
//	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
//	public BrandData get(@PathVariable int id) throws ApiException {
//		BrandMasterPojo p = oService.get(id);
//		return convert(p);
//	}
//
//	@ApiOperation(value = "Gets list of all Brands")
//	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
//	public List<BrandData> getAll() {
//		List<BrandMasterPojo> list = oService.getAll();
//		List<BrandData> list2 = new ArrayList<BrandData>();
//		for (BrandMasterPojo p : list) {
//			list2.add(convert(p));
//		}
//		return list2;
//	}
//
//	@ApiOperation(value = "Updates a Brand")
//	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
//	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
//		BrandMasterPojo p = convert(f);
//		oService.update(id, p);
//	}
//
//	private static BrandData convert(BrandMasterPojo p) {
//		BrandData d = new BrandData();
//		d.setCategory(p.getCategory());
//		d.setBrand(p.getBrand());
//		d.setId(p.getId());
//		return d;
//	}
//
//	private static BrandMasterPojo convert(BrandForm f) {
//		BrandMasterPojo b = new BrandMasterPojo();
//		b.setCategory(f.getCategory());
//		b.setBrand(f.getBrand());
//		return b;
//	}
	private List<OrderItemPojo> getOrderItemObject(OrderItemForm[] orderItems) throws ApiException {
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		int orderId = oService.getMax();
		for (OrderItemForm o : orderItems) {
			ProductMasterPojo p = pService.getId(o.getBarcode());
			OrderItemPojo item = new OrderItemPojo();
			item.setOrderid(orderId);
			item.setProductId(p.getId());
			item.setQuantity(o.getQuantity());
			item.setSellingPrice(o.getMrp());
			list.add(item);
		}
		return list;
	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}
}
