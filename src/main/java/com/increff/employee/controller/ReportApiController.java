package com.increff.employee.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ReportService;
import com.increff.employee.util.ConverterUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private ReportService service;
	@Autowired
	private OrderService oService;

	@Autowired
	private OrderItemService iService;
	@Autowired
	private InventoryService inService;
	@Autowired
	private BrandService bService;

	// Sales Report
	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(value = "/api/salesreport", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm salesReportForm)
			throws ParseException, ApiException {
		List<OrderPojo> o = oService.getAll();
		// Get list of order ids
		List<Integer> orderIds = service.getOrderIdList(o, salesReportForm.getStartdate(),
				salesReportForm.getEnddate());
		if (orderIds.size() == 0) {
			throw new ApiException("There are no orders for given dates");
		} else {
			// Get list of order items by given order ids
			List<OrderItemPojo> listOfOrderItemPojo = iService.getList(orderIds);
			// Group order item pojo by product id
			listOfOrderItemPojo = service.groupOrderItemPojoByProductId(listOfOrderItemPojo);
			// Converts OrderItemPojo to SalesReportData
			List<SalesReportData> salesReportData = ConverterUtil.convertToSalesData(listOfOrderItemPojo);
			// Remove sales report data according to brand and category
			salesReportData = service.getSalesReportDataByBrandAndCategory(salesReportData, salesReportForm.getBrand(),
					salesReportForm.getCategory());
			// Group Sales Report Data category wise
			salesReportData = service.groupSalesReportDataCategoryWise(salesReportData);
			if (salesReportData.size() == 0) {
				throw new ApiException("There are no sales for given data");
			} else {
				return salesReportData;
			}
		}
	}

	// Brand Report
	@ApiOperation(value = "Gets Brand Report")
	@RequestMapping(value = "/api/brandreport", method = RequestMethod.GET)
	public List<BrandData> getBrandReport() {
		List<BrandMasterPojo> list = bService.getAll();
		return ConverterUtil.convertToBrandData(list);
	}

	// Inventory Report
	@ApiOperation(value = "Gets Inventory Report")
	@RequestMapping(value = "/api/inventoryreport", method = RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		List<InventoryPojo> ip = inService.getAll();
		List<InventoryReportData> list2 = ConverterUtil.convertToInventoryReportData(ip);
		// Group list of InventoryReportData brand and category wise
		return service.groupDataForInventoryReport(list2);
	}

}