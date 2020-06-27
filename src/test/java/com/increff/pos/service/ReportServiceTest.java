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

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.StringUtil;

public class ReportServiceTest extends AbstractUnitTest {
	@Autowired
	private ReportService service;
	@Autowired
	private OrderService oService;
	@Autowired
	private BrandService bService;
	@Autowired
	private ProductService pService;
	@Autowired
	private InventoryService inService;
	@Autowired
	private OrderItemService iService;

	// test order id list with range(start date-end date)
	@Test
	public void testGetOrderIdList() throws ParseException, ApiException {
		String startdate = "01-02-2020";
		String enddate = "23-03-2020";
		List<OrderPojo> list = getOrderPojoList();
		List<Integer> orderIds = service.getOrderIdList(list, startdate, enddate);
		assertEquals(1, orderIds.size());
	}

	// test order items with same product id
	@Test
	public void testGroupOrderItemPojoByProductId() throws ApiException {
		getOrderItemPojoTest();
		List<OrderItemPojo> list = iService.getAll();
		list = service.groupOrderItemPojoByProductId(list);
		// check results
		assertEquals(2, list.size());
		assertEquals(25, list.get(0).getQuantity());
	}

	// test all the conditions of brand-category pair
	@Test
	public void testGetSalesReportDataByBrandAndCategory() {

		List<SalesReportData> list = getSalesData();
		list = service.getSalesReportDataByBrandAndCategory(list, "", "");
		assertEquals(5, list.size());
		list = getSalesData();
		list = service.getSalesReportDataByBrandAndCategory(list, "increff", "");
		assertEquals(2, list.size());
		list = getSalesData();
		list = service.getSalesReportDataByBrandAndCategory(list, "", "shah");
		assertEquals(3, list.size());
		list = getSalesData();
		list = service.getSalesReportDataByBrandAndCategory(list, "viram", "shah");
		assertEquals(2, list.size());
	}

	// test created sales data category wise
	@Test
	public void testGroupSalesReportDataCategoryWise() {
		List<SalesReportData> list = getSalesData();
		list = service.getSalesReportDataByBrandAndCategory(list, "", "shah");
		// check list size after selecting category
		assertEquals(3, list.size());
		// calculate total quantity and revenue
		int i, quantity = 0;
		double revenue = 0;
		for (i = 0; i < 3; i++) {
			quantity += list.get(i).getQuantity();
			revenue += list.get(i).getRevenue();
		}
		// group category wise
		list = service.groupSalesReportDataCategoryWise(list);
		// check results
		assertEquals(1, list.size());
		assertEquals(quantity, list.get(0).getQuantity());
		assertEquals(revenue, list.get(0).getRevenue(), 0.01);
	}

	// test created inventory data
	@Test
	public void testGroupDataForInventoryReport() {
		List<InventoryReportData> list = getInventoryData();
		list = service.groupDataForInventoryReport(list);
		// compare data with actual values
		assertEquals(4, list.size());
		assertEquals(40, list.get(0).getQuantity());
	}

	// create order data using dates
	public List<OrderPojo> getOrderPojoList() throws ApiException {
		List<OrderPojo> list = new ArrayList<OrderPojo>();
		String[] dates = { "03-02-2020", "01-01-2020", "02-02-2019", "03-05-2020" };
		int i;
		for (i = 0; i < dates.length; i++) {
			OrderPojo o = new OrderPojo();
			o.setDatetime(dates[i]);
			oService.add(o);
			list.add(o);
		}
		return list;
	}

	// Returns date and time in required format
	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	// create sales report data using brand,category,quantity and revenue
	private List<SalesReportData> getSalesData() {
		List<SalesReportData> list = new ArrayList<SalesReportData>();
		String[] brand = { "viram", "increff", "nextscm", "increff", "viram" };
		String[] category = { "shah", "pos", "pvt", "shah", "shah" };
		int[] quantity = { 10, 15, 5, 20, 30 };
		double[] revenue = { 100.50, 150, 100, 400, 3000 };
		int i;
		for (i = 0; i < 5; i++) {
			SalesReportData s = new SalesReportData();
			s.setBrand(brand[i]);
			s.setCategory(category[i]);
			s.setQuantity(quantity[i]);
			s.setRevenue(revenue[i]);
			list.add(s);
		}
		return list;
	}

	// create inventory report data using brand,category and quantity
	private List<InventoryReportData> getInventoryData() {
		List<InventoryReportData> list = new ArrayList<InventoryReportData>();
		String[] brand = { "viram", "increff", "nextscm", "increff", "viram" };
		String[] category = { "shah", "pos", "pvt", "shah", "shah" };
		int[] quantity = { 10, 15, 5, 20, 30 };
		int i;
		for (i = 0; i < 5; i++) {
			InventoryReportData invData = new InventoryReportData();
			invData.setBrand(brand[i]);
			invData.setCategory(category[i]);
			invData.setQuantity(quantity[i]);
			list.add(invData);
		}
		return list;
	}

	private void getOrderItemPojoTest() throws ApiException {
		OrderItemPojo o1 = new OrderItemPojo();
		OrderItemPojo o2 = new OrderItemPojo();
		OrderItemPojo o3 = new OrderItemPojo();
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		int quantity = 10;
		double sellingPrice = 10.25;
		// create data
		OrderPojo op = new OrderPojo();
		String datetime = getDateTime();
		op.setDatetime(datetime);
		oService.add(op);
		ProductMasterPojo p1 = new ProductMasterPojo();
		ProductMasterPojo p2 = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		InventoryPojo i = new InventoryPojo();
		String barcode1 = StringUtil.getAlphaNumericString();
		String barcode2 = StringUtil.getAlphaNumericString();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p1.setBarcode(barcode1);
		p1.setBrand_category(b);
		p1.setName(" ProDuct ");
		p1.setMrp(mrp);
		p2.setBarcode(barcode2);
		p2.setBrand_category(b);
		p2.setName(" ProDuct ");
		p2.setMrp(mrp);
		pService.add(p1);
		pService.add(p2);
		i.setProductMasterPojo(p1);
		i.setQuantity(quantity + 10);
		inService.add(i);
		o1.setOrderPojo(op);
		o1.setProductMasterPojo(p1);
		o1.setQuantity(quantity);
		o1.setSellingPrice(sellingPrice);
		o2.setOrderPojo(op);
		o2.setProductMasterPojo(p1);
		o2.setQuantity(quantity + 5);
		o2.setSellingPrice(sellingPrice);
		o3.setOrderPojo(op);
		o3.setProductMasterPojo(p2);
		o3.setQuantity(quantity + 5);
		o3.setSellingPrice(sellingPrice);
		list.add(o1);
		list.add(o2);
		list.add(o3);
		iService.add(list);
	}
}
