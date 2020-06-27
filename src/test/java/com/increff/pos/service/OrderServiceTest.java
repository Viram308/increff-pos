package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BillData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.StringUtil;

public class OrderServiceTest extends AbstractUnitTest {
	@Autowired
	private OrderService service;
	@Autowired
	private BrandService bService;
	@Autowired
	private ProductService pService;
	@Autowired
	private InventoryService inService;
	@Autowired
	private OrderItemService iService;

	// test order service
	@Test
	public void testAdd() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		// test add
		service.add(op);
	}

	@Test
	public void testDelete() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		service.add(op);
		// test delete
		service.delete(op.getId());
	}

	@Test
	public void testGet() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		service.add(op);
		OrderPojo o = service.get(op.getId());
		// test added data
		assertEquals(op.getDatetime(), o.getDatetime());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {

		OrderPojo op = getOrderPojoTest();
		service.add(op);
		OrderPojo o = service.getCheck(op.getId());
		service.delete(o.getId());
		// After delete throw exception while getting data
		service.getCheck(o.getId());
	}

	@Test
	public void testGroupItemsByBarcode() {
		OrderItemForm o1 = getOrderItemForm();
		OrderItemForm o2 = getOrderItemForm();
		// make list
		OrderItemForm[] orderItemForms = getArray(o1, o2);
		List<OrderItemForm> list = service.groupItemsByBarcode(orderItemForms);
		// test size and doubled quantity
		assertEquals(2, list.size());
		assertEquals(20, list.get(0).getQuantity());
	}

	@Test(expected = ApiException.class)
	public void testCheckInventory() throws ApiException {
		List<OrderItemForm> orderItemForms = getList();
		// throws exception for negative quantity
		service.checkInventory(orderItemForms);
	}

	@Test(expected = ApiException.class)
	public void testCheckInventoryZeroQuantity() throws ApiException {
		List<OrderItemForm> orderItemForms = getList();
		// Update created data so that inventory is zero
		orderItemForms.get(0).setQuantity(10);
		// throws exception for zero quantity available
		service.checkInventory(orderItemForms);
	}

	@Test
	public void testCheckInventoryPerfect() throws ApiException {
		List<OrderItemForm> orderItemForms = getList();
		// Update created data so that inventory is available
		orderItemForms.get(1).setQuantity(25);
		// Does not throws exception
		service.checkInventory(orderItemForms);
	}

	@Test
	public void testGetOrderItemObject() throws ApiException {
		OrderPojo op = getOrderPojoTest();
		service.add(op);
		List<OrderItemForm> orderItemForms = getList();
		List<OrderItemPojo> list = service.getOrderItemObject(orderItemForms, op);
		assertEquals(2, list.size());
	}

	@Test
	public void testUpdateInventory() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		iService.add(list);
		service.updateInventory(list);
	}

	@Test
	public void testGetBillDataObject() throws ApiException {
		OrderItemPojo o = getOrderItemPojoTest();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.add(o);
		// test add data
		iService.add(list);
		List<BillData> billDataList = service.getBillDataObject(list);
		assertEquals(1, billDataList.size());

	}

	public List<OrderItemForm> getList() throws ApiException {
		List<OrderItemForm> orderItemForms = new ArrayList<OrderItemForm>();
		String b1 = StringUtil.getAlphaNumericString();
		String b2 = StringUtil.getAlphaNumericString();
		String brand1 = " viram ";
		String brand2 = " Increff ";
		int quantity1 = 10;
		int quantity2 = 30;
		double mrp = 50;
		getData(brand1, b1, quantity1);
		getData(brand2, b2, quantity2);
		OrderItemForm o1 = new OrderItemForm();
		OrderItemForm o2 = new OrderItemForm();
		o1.setBarcode(b1);
		o1.setQuantity(5);
		o1.setMrp(mrp);
		o2.setBarcode(b2);
		o2.setQuantity(35);
		o2.setMrp(mrp);
		orderItemForms.add(o1);
		orderItemForms.add(o2);
		return orderItemForms;
	}

	private OrderItemForm[] getArray(OrderItemForm o1, OrderItemForm o2) {
		OrderItemForm[] orderItemForms = new OrderItemForm[4];
		orderItemForms[0] = o1;
		orderItemForms[1] = o2;
		orderItemForms[2] = o2;
		orderItemForms[3] = o1;
		return orderItemForms;
	}

	public void getData(String brand, String barcode, int quantity) throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		InventoryPojo i = new InventoryPojo();
		b.setBrand(brand);
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(b);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		pService.add(p);
		i.setProductMasterPojo(p);
		i.setQuantity(quantity);
		inService.add(i);
	}

	private OrderItemForm getOrderItemForm() {
		OrderItemForm orderItemForm = new OrderItemForm();
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 10;
		double mrp = 50.20;
		orderItemForm.setBarcode(barcode);
		orderItemForm.setQuantity(quantity);
		orderItemForm.setMrp(mrp);
		return orderItemForm;
	}

	private String getDateTime() {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	public OrderPojo getOrderPojoTest() throws ApiException {
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		// create data
		op.setDatetime(datetime);
		return op;
	}

	private OrderItemPojo getOrderItemPojoTest() throws ApiException {
		OrderItemPojo o = new OrderItemPojo();
		int quantity = 10;
		double sellingPrice = 10.25;
		// create data
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		service.add(op);
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
