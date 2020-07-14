package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.StringUtil;

public class OrderItemServiceTest extends AbstractUnitTest {
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private InventoryService inventoryService;

	// test order item service
	@Test
	public void testAdd() throws ApiException {
		OrderItemPojo orderItemPojo = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(orderItemPojo);
		// test add data
		orderItemService.add(list);
		List<OrderItemPojo> orderItemPojos = orderItemService.getAll();
		assertEquals(1, orderItemPojos.size());
	}

	@Test(expected = ApiException.class)
	public void testDeleteByOrderId() throws ApiException {
		OrderItemPojo orderItemPojo = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(orderItemPojo);
		// test add data
		orderItemService.add(list);
		// Delete should be successful and should not throw exception as data exists
		orderItemService.deleteByOrderId(orderItemPojo.getOrderId());
		orderItemService.getByOrderId(orderItemPojo.getOrderId());
	}

	@Test
	public void testGetByOrderId() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		int i = 0;
		// test add data
		orderItemService.add(list);
		// Delete should be successful and should not throw exception as data exists
		List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(o.getOrderId());
		for (OrderItemPojo orderItemPojo : orderItemPojos) {
			assertEquals(list.get(i).getOrderId(), orderItemPojo.getOrderId());
			assertEquals(list.get(i).getProductId(), orderItemPojo.getProductId());
			assertEquals(list.get(i).getQuantity(), orderItemPojo.getQuantity());
			assertEquals(list.get(i).getSellingPrice(), orderItemPojo.getSellingPrice(), 0.01);
			i++;
		}
	}

	@Test(expected = ApiException.class)
	public void testGetCheckForOrderId() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		int i = 0;
		// test add data
		orderItemService.add(list);
		// Delete should be successful and should not throw exception as data exists
		List<OrderItemPojo> orderItemPojos = orderItemService.getCheckForOrderId(o.getOrderId());
		for (OrderItemPojo orderItemPojo : orderItemPojos) {
			assertEquals(list.get(i).getOrderId(), orderItemPojo.getOrderId());
			assertEquals(list.get(i).getProductId(), orderItemPojo.getProductId());
			assertEquals(list.get(i).getQuantity(), orderItemPojo.getQuantity());
			assertEquals(list.get(i).getSellingPrice(), orderItemPojo.getSellingPrice(), 0.01);
			i++;
		}
		orderItemService.deleteByOrderId(o.getOrderId());
		orderItemService.getCheckForOrderId(o.getOrderId());
	}

	@Test
	public void testGetAll() throws ApiException {
		OrderItemPojo orderItemPojo = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(orderItemPojo);
		// test add data
		orderItemService.add(list);
		List<OrderItemPojo> orderItemPojos = orderItemService.getAll();
		assertEquals(1, orderItemPojos.size());
	}

	@Test
	public void testGetList() throws ApiException {
		OrderItemPojo orderItemPojo = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(orderItemPojo);
		// test add data
		orderItemService.add(list);
		List<Integer> orderIds = new ArrayList<Integer>();
		orderIds.add(orderItemPojo.getOrderId());
		List<OrderItemPojo> list1 = orderItemService.getList(orderIds);
		// test list size that should be 1
		assertEquals(1, list1.size());
	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	private OrderItemPojo getOrderItemPojoTest() throws ApiException {
		OrderItemPojo o = new OrderItemPojo();
		int quantity = 10;
		double sellingPrice = 10.25;
		// create data
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		orderService.add(op);
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		InventoryPojo i = new InventoryPojo();
		String barcode = StringUtil.getAlphaNumericString();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		brandService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		productService.add(p, b);
		i.setProductId(p.getId());
		i.setQuantity(quantity + 10);
		inventoryService.add(i);
		o.setOrderId(op.getId());
		o.setProductId(p.getId());
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		return o;
	}

}
