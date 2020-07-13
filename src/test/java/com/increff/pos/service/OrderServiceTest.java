package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
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
		// throw exception while getting data for id + 1
		service.getCheck(o.getId() + 1);
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
		orderItemForms.get(0).quantity = 10;
		// throws exception for zero quantity available
		service.checkInventory(orderItemForms);
	}

	@Test
	public void testCheckInventoryPerfect() throws ApiException {
		List<OrderItemForm> orderItemForms = getList();
		// Update created data so that inventory is available
		orderItemForms.get(1).quantity = 25;
		// Does not throws exception
		service.checkInventory(orderItemForms);
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

	@Test
	public void testGetList() throws ParseException {
		List<OrderPojo> orderPojos = getOrderPojoList();
		List<OrderPojo> orderPojoList = service.getList(orderPojos, "02-05-2020", "02-07-2020");
		assertEquals(1, orderPojoList.size());
		orderPojoList = service.getList(orderPojos, "02-05-2020", "02-06-2020");
		assertEquals(0, orderPojoList.size());
		orderPojoList = service.getList(orderPojos, "02-05-2020", "07-07-2020");
		assertEquals(2, orderPojoList.size());
		orderPojoList = service.getList(orderPojos, "01-07-2020", "07-07-2020");
		assertEquals(2, orderPojoList.size());
		orderPojoList = service.getList(orderPojos, "01-07-2020", "08-07-2020");
		assertEquals(2, orderPojoList.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		OrderPojo orderPojo1 = getOrderPojoTest();
		service.add(orderPojo1);
		OrderPojo orderPojo2 = new OrderPojo();
		orderPojo2.setDatetime("02-02-2020 09:45");
		service.update(orderPojo1.getId(), orderPojo2);
		assertEquals(orderPojo2.getDatetime(), orderPojo1.getDatetime());
	}

	private List<OrderPojo> getOrderPojoList() {
		OrderPojo orderPojo1 = new OrderPojo();
		OrderPojo orderPojo2 = new OrderPojo();
		orderPojo1.setDatetime("01-07-2020 09:45");
		orderPojo2.setDatetime("07-07-2020 09:45");
		List<OrderPojo> orderPojos = new ArrayList<OrderPojo>();
		orderPojos.add(orderPojo1);
		orderPojos.add(orderPojo2);
		return orderPojos;
	}

	private List<OrderItemForm> getList() throws ApiException {
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
		o1.barcode = b1;
		o1.quantity = 5;
		o1.sellingPrice = mrp;
		o2.barcode = b2;
		o2.quantity = 35;
		o2.sellingPrice = mrp;
		orderItemForms.add(o1);
		orderItemForms.add(o2);
		return orderItemForms;
	}

	private void getData(String brand, String barcode, int quantity) throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		InventoryPojo i = new InventoryPojo();
		b.setBrand(brand);
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		pService.add(p, b);
		i.setProductId(p.getId());
		i.setQuantity(quantity);
		inService.add(i);
	}

	private String getDateTime() {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	private OrderPojo getOrderPojoTest() {
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
		p.setBrand_category_id(b.getId());
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		pService.add(p, b);
		i.setProductId(p.getId());
		i.setQuantity(quantity + 10);
		inService.add(i);
		o.setOrderId(op.getId());
		o.setProductId(p.getId());
		o.setQuantity(quantity);
		o.setSellingPrice(sellingPrice);
		return o;
	}
}
