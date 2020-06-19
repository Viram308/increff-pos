package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

public class OrderItemServiceTest extends AbstractUnitTest {
	@Autowired
	private OrderItemService service;
	@Autowired
	private BrandService bService;
	@Autowired
	private ProductService pService;
	@Autowired
	private OrderService oService;
	@Autowired
	private InventoryService inService;

	// test order item service
	@Test
	public void testAdd() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
	}

	@Test
	public void testDelete() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		// Delete should be successful and should not throw exception as data exists
		service.delete(o.getId());
	}

	@Test
	public void testGet() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		OrderItemPojo p = service.get(o.getId());
		// test entered data
		assertEquals(o.getOrderPojo().getId(), p.getOrderPojo().getId());
		assertEquals(o.getProductMasterPojo().getId(), p.getProductMasterPojo().getId());
		assertEquals(o.getQuantity(), p.getQuantity());
		assertEquals(o.getSellingPrice(), p.getSellingPrice(), 0.01);
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		OrderItemPojo p = service.get(o.getId());
		int newQuantity = 20;
		// update data
		p.setQuantity(newQuantity);
		service.update(p.getId(), p);
		OrderItemPojo pi = service.get(p.getId());
		// test updated data
		assertEquals(newQuantity, pi.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		OrderItemPojo p = service.getCheck(o.getId());
		service.delete(p.getId());
		// After delete throw exception while getting data
		service.getCheck(p.getId());
	}

	@Test
	public void testGetList() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		List<Integer> orderIds = new ArrayList<Integer>();
		orderIds.add(o.getOrderPojo().getId());
		List<OrderItemPojo> list1 = service.getList(orderIds);
		// test list size that should be 1
		assertEquals(1, list1.size());
	}

	@Test(expected = ApiException.class)
	public void testCheckInventoryZeroQuantity() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		OrderItemForm f = getOrderItemForm();
		InventoryPojo ip = inService.getByProductId(o.getProductMasterPojo());
		// set quantity such that available quantity is zero
		f.setQuantity(o.getQuantity() + ip.getQuantity());
		// throws exception for zero available quantity
		service.checkInventory(list.get(0).getId(), f);

	}

	@Test(expected = ApiException.class)
	public void testCheckInventoryNegativeQuantity() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		OrderItemForm f = getOrderItemForm();
		InventoryPojo ip = inService.getByProductId(o.getProductMasterPojo());
		// set quantity such that available quantity is negative
		f.setQuantity(o.getQuantity() + ip.getQuantity() + 1);
		// throws exception for negative available quantity
		service.checkInventory(list.get(0).getId(), f);

	}

	@Test
	public void testCheckInventoryPerfectQuantity() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		service.add(list);
		OrderItemForm f = getOrderItemForm();
		InventoryPojo ip = inService.getByProductId(o.getProductMasterPojo());
		// set quantity such that available quantity is one
		f.setQuantity(o.getQuantity() + ip.getQuantity() - 1);
		// Does not throws exception for positive available quantity
		service.checkInventory(list.get(0).getId(), f);
		InventoryPojo ip2 = inService.getByProductId(o.getProductMasterPojo());
		assertEquals(1, ip2.getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testCheckEnteredQuantityZero() throws ApiException {
		OrderItemForm f = getOrderItemForm();
		f.setQuantity(0);
		//
		service.checkEnteredQuantity(f);
	}

	@Test
	public void testCheckEnteredQuantityPerfect() throws ApiException {
		OrderItemForm f = getOrderItemForm();
		f.setQuantity(10);
		// throws exception for zero entered quantity
		service.checkEnteredQuantity(f);
	}

	private OrderItemForm getOrderItemForm() {
		OrderItemForm f = new OrderItemForm();
		double mrp = 10.06;
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 15;
		f.setMrp(mrp);
		f.setBarcode(barcode);
		f.setQuantity(quantity);
		return f;
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
		oService.add(op);
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		InventoryPojo i = new InventoryPojo();
		String barcode = StringUtil.getAlphaNumericString();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(b);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		pService.add(p);
		i.setProductMasterPojo(p);
		i.setQuantity(quantity + 10);
		inService.add(i);
		o.setOrderPojo(op);
		o.setProductMasterPojo(p);
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		return o;
	}

}
