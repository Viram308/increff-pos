package com.increff.employee.controller;

import java.text.ParseException;
import java.util.ArrayList;
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
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ReportService;

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
	@RequestMapping(path = "/api/salesreport", method = RequestMethod.POST)
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
			List<SalesReportData> salesReportData = convertToSalesData(listOfOrderItemPojo);
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
	@RequestMapping(path = "/api/brandreport", method = RequestMethod.GET)
	public List<BrandData> getBrandReport() {
		List<BrandMasterPojo> list = bService.getAll();
		return convertToBrandData(list);
	}

	// Inventory Report
	@ApiOperation(value = "Gets Inventory Report")
	@RequestMapping(path = "/api/inventoryreport", method = RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		List<InventoryPojo> ip = inService.getAll();
		List<InventoryReportData> list2 = convertToInventoryReportData(ip);
		// Group list of InventoryReportData brand and category wise
		return service.groupDataForInventoryReport(list2);
	}

	private List<SalesReportData> convertToSalesData(List<OrderItemPojo> listOfOrderItemPojo) {
		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
		int i;
		// Converts OrderItemPojo to SalesReportData
		for (i = 0; i < listOfOrderItemPojo.size(); i++) {
			SalesReportData salesProductData = new SalesReportData();
			ProductMasterPojo p = listOfOrderItemPojo.get(i).getProductMasterPojo();
			BrandMasterPojo b = p.getBrand_category();
			salesProductData.setBrand(b.getBrand());
			salesProductData.setCategory(b.getCategory());
			salesProductData.setQuantity(listOfOrderItemPojo.get(i).getQuantity());
			salesProductData.setRevenue(
					listOfOrderItemPojo.get(i).getQuantity() * listOfOrderItemPojo.get(i).getSellingPrice());
			salesReportData.add(salesProductData);
		}
		return salesReportData;
	}

	private List<BrandData> convertToBrandData(List<BrandMasterPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		int i = 0;
		// Converts BrandMasterPojo to BrandData
		for (i = 0; i < list.size(); i++) {
			BrandData b = new BrandData();
			b.setId(i + 1);
			b.setBrand(list.get(i).getBrand());
			b.setCategory(list.get(i).getCategory());
			list2.add(b);
		}
		return list2;
	}

	private List<InventoryReportData> convertToInventoryReportData(List<InventoryPojo> ip) {
		List<InventoryReportData> list2 = new ArrayList<InventoryReportData>();
		int i;
		// Converts InventoryPojo to InventoryReportData
		for (i = 0; i < ip.size(); i++) {
			ProductMasterPojo p = ip.get(i).getProductMasterPojo();
			BrandMasterPojo b = p.getBrand_category();
			InventoryReportData ir = new InventoryReportData();
			ir.setBrand(b.getBrand());
			ir.setCategory(b.getCategory());
			ir.setQuantity(ip.get(i).getQuantity());
			list2.add(ir);
		}
		return list2;
	}
}