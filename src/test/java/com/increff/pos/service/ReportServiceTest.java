package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class ReportServiceTest extends AbstractUnitTest {
	@Autowired
	private ReportService service;
	@Autowired
	private OrderService oService;

	// test order id list with range(start date-end date)
	@Test(expected = ApiException.class)
	public void testGetOrderIdList() throws ParseException, ApiException {
		String startdate = "01-02-2020";
		String enddate = "23-03-2020";
		List<OrderPojo> list = getOrderPojoList();
		List<Integer> orderIds = service.getOrderIdList(list, startdate, enddate);
		assertEquals(1, orderIds.size());
		startdate = "01-02-2018";
		enddate = "23-03-2018";
		service.getOrderIdList(list, startdate, enddate);
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
	@Test(expected = ApiException.class)
	public void testGroupSalesReportDataCategoryWise() throws ApiException {
		List<SalesReportData> list = getSalesData();
		list = service.getSalesReportDataByBrandAndCategory(list, "", "shah");
		// check list size after selecting category
		assertEquals(3, list.size());
		// calculate total quantity and revenue
		int i, quantity = 0;
		double revenue = 0;
		for (i = 0; i < 3; i++) {
			quantity += list.get(i).quantity;
			revenue += list.get(i).revenue;
		}
		// group category wise
		list = service.groupSalesReportDataCategoryWise(list);
		// check results
		assertEquals(1, list.size());
		assertEquals(quantity, list.get(0).quantity);
		assertEquals(revenue, list.get(0).revenue, 0.01);
		list = service.getSalesReportDataByBrandAndCategory(list, "", "abc");
		service.groupSalesReportDataCategoryWise(list);
	}

	// test created inventory data
	@Test
	public void testGroupDataForInventoryReport() {
		List<InventoryReportData> list = getInventoryData();
		list = service.groupDataForInventoryReport(list);
		// compare data with actual values
		assertEquals(4, list.size());
		assertEquals(40, list.get(0).quantity);
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
			s.brand = brand[i];
			s.category = category[i];
			s.quantity = quantity[i];
			s.revenue = revenue[i];
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
			invData.brand = brand[i];
			invData.category = category[i];
			invData.quantity = quantity[i];
			list.add(invData);
		}
		return list;
	}
}
