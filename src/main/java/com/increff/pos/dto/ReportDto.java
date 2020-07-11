package com.increff.pos.dto;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ReportService;
import com.increff.pos.util.ConverterUtil;

@Component
public class ReportDto {
	@Autowired
	private ReportService reportService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ConverterUtil converterUtil;

	public List<OrderItemPojo> getOrderItemPojoList(SalesReportForm salesReportForm)
			throws ParseException, ApiException {
		List<OrderPojo> orderPojo = orderService.getAll();
		// Get list of order ids
		List<Integer> orderIds = reportService.getOrderIdList(orderPojo, salesReportForm.startdate,
				salesReportForm.enddate);
		return orderItemService.getList(orderIds);
	}

	public List<SalesReportData> getSalesReportData(SalesReportForm salesReportForm)
			throws ApiException, ParseException {
		// Get list of order items by given order ids
		List<OrderItemPojo> listOfOrderItemPojo = getOrderItemPojoList(salesReportForm);
		// Group order item pojo by product id
		listOfOrderItemPojo = reportService.groupOrderItemPojoByProductId(listOfOrderItemPojo);
		// Converts OrderItemPojo to SalesReportData
		List<SalesReportData> salesReportData = listOfOrderItemPojo.stream()
				.map(o -> converterUtil.convertToSalesReportData(o,
						brandService.get(productService.get(o.getProductId()).getBrand_category_id())))
				.collect(Collectors.toList());
		// Remove sales report data according to brand and category
		salesReportData = reportService.getSalesReportDataByBrandAndCategory(salesReportData, salesReportForm.brand,
				salesReportForm.category);
		// Group Sales Report Data category wise
		return reportService.groupSalesReportDataCategoryWise(salesReportData);
	}

	public List<BrandData> getBrandReportData() {
		List<BrandMasterPojo> list = brandService.getAll();
		return list.stream().map(o -> converterUtil.convertBrandMasterPojotoBrandData(o)).collect(Collectors.toList());
	}

	public List<InventoryReportData> getInventoryReportData() {
		List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
		List<InventoryReportData> list2 = inventoryPojoList.stream()
				.map(o -> converterUtil.convertToInventoryReportData(o,
						brandService.get(productService.get(o.getProductid()).getBrand_category_id())))
				.collect(Collectors.toList());
		// Group list of InventoryReportData brand and category wise
		return reportService.groupDataForInventoryReport(list2);
	}

	public List<BrandData> searchBrandReport(BrandForm brandForm) throws ApiException {
		List<BrandMasterPojo> list = brandService.searchBrandData(brandForm);
		return list.stream().map(o -> converterUtil.convertBrandMasterPojotoBrandData(o)).collect(Collectors.toList());
	}

	public List<InventoryReportData> searchInventoryReport(BrandForm brandForm) throws ApiException {
		List<BrandMasterPojo> brandMasterPojoList = brandService.searchBrandData(brandForm);
		List<Integer> brandIds = brandMasterPojoList.stream().map(o -> o.getId()).collect(Collectors.toList());
		List<ProductMasterPojo> list = productService.getAll();
		list = list.stream().filter(o -> (brandIds.contains(o.getBrand_category_id()))).collect(Collectors.toList());
		List<Integer> productIds = list.stream().map(o -> o.getId()).collect(Collectors.toList());
		List<InventoryPojo> inventoryPojoList = inventoryService.searchData(productIds);
		List<InventoryReportData> list2 = inventoryPojoList.stream()
				.map(o -> converterUtil.convertToInventoryReportData(o,
						brandService.get(productService.get(o.getProductid()).getBrand_category_id())))
				.collect(Collectors.toList());
		// Group list of InventoryReportData brand and category wise
		return reportService.groupDataForInventoryReport(list2);
	}

}
