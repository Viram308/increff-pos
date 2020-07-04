package com.increff.pos.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private ReportDto reportDto;

	// Sales Report
	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(value = "/api/salesreport", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm salesReportForm)
			throws ParseException, ApiException {

		// Get list of order items by given order ids
		List<OrderItemPojo> listOfOrderItemPojo = reportDto.getOrderItemPojoList(salesReportForm);
		// Group order item pojo by product id
		listOfOrderItemPojo = reportDto.groupOrderItemPojoByProductId(listOfOrderItemPojo);
		// Converts OrderItemPojo to SalesReportData
		List<SalesReportData> salesReportData = reportDto.convertToSalesData(listOfOrderItemPojo);
		// Remove sales report data according to brand and category
		return reportDto.getSalesReportData(salesReportData, salesReportForm);
	}

	// Brand Report
	@ApiOperation(value = "Gets Brand Report")
	@RequestMapping(value = "/api/brandreport", method = RequestMethod.GET)
	public List<BrandData> getBrandReport() {
		return reportDto.getBrandReportData();
	}

	// Inventory Report
	@ApiOperation(value = "Gets Inventory Report")
	@RequestMapping(value = "/api/inventoryreport", method = RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		return reportDto.getInventoryReportData();
	}

}