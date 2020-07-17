package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtil;

public class ReportServiceTest extends AbstractUnitTest {
	@Autowired
	private ReportService reportService;
	@Autowired
	private OrderService orderService;

	// test order id list with range(start date-end date)
	@Test(expected = ApiException.class)
	public void testGetOrderIdList() throws ParseException, ApiException {
		String startdate = "01-02-2020";
		String enddate = "23-03-2020";
		List<OrderPojo> list = TestUtil.getOrderPojoListForSalesReport();
		for (OrderPojo orderPojo : list) {
			orderService.add(orderPojo);
		}

		List<Integer> orderIds = reportService.getOrderIdList(list, startdate, enddate);
		assertEquals(1, orderIds.size());
		startdate = "01-02-2018";
		enddate = "23-03-2018";
		reportService.getOrderIdList(list, startdate, enddate);
	}

	// test created sales data category wise
	@Test(expected = ApiException.class)
	public void testGroupSalesReportDataCategoryWise() throws ApiException {
		List<SalesReportData> list = TestUtil.getSalesData();
		// group category wise
		list = reportService.groupSalesReportDataCategoryWise(list);
		// check results
		assertEquals(3, list.size());
		assertEquals(60, list.get(0).quantity);
		assertEquals(3500.50, list.get(0).revenue, 0.01);
		list.clear();
		// throws exception for empty list
		reportService.groupSalesReportDataCategoryWise(list);
	}

	// test created inventory data
	@Test
	public void testGroupDataForInventoryReport() {
		List<InventoryReportData> list = TestUtil.getInventoryData();
		list = reportService.groupDataForInventoryReport(list);
		// compare data with actual values
		assertEquals(4, list.size());
		assertEquals(40, list.get(0).quantity);
	}

}
