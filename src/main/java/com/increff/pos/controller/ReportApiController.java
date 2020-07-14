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
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/admin")
public class ReportApiController {

	@Autowired
	private ReportDto reportDto;

	// Sales Report
	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(value = "/sales-report", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm salesReportForm)
			throws ParseException, ApiException {
		return reportDto.getSalesReport(salesReportForm);
	}

	// Brand Report
	@ApiOperation(value = "Gets Brand Report")
	@RequestMapping(value = "/brand-report", method = RequestMethod.POST)
	public List<BrandData> searchBrandReport(@RequestBody BrandForm brandForm) throws ApiException {
		return reportDto.searchBrandReport(brandForm);
	}

	@ApiOperation(value = "Gets Inventory Report")
	@RequestMapping(value = "/inventory-report", method = RequestMethod.POST)
	public List<InventoryReportData> searchInventoryReport(@RequestBody BrandForm brandForm) throws ApiException {
		return reportDto.searchInventoryReport(brandForm);
	}


}