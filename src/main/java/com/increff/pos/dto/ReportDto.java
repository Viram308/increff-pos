package com.increff.pos.dto;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
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
	private ConverterUtil converterUtil;

	public List<OrderItemPojo> getOrderItemPojoList(SalesReportForm salesReportForm)
			throws ParseException, ApiException {
		List<OrderPojo> orderPojo = orderService.getAll();
		// Get list of order ids
		List<Integer> orderIds = reportService.getOrderIdList(orderPojo, salesReportForm.getStartdate(),
				salesReportForm.getEnddate());
		if (orderIds.size() == 0) {
			throw new ApiException("There are no orders for given dates");
		} else {
			return orderItemService.getList(orderIds);
		}
	}

	public List<OrderItemPojo> groupOrderItemPojoByProductId(List<OrderItemPojo> listOfOrderItemPojo) {
		return reportService.groupOrderItemPojoByProductId(listOfOrderItemPojo);
	}

	public List<SalesReportData> convertToSalesData(List<OrderItemPojo> listOfOrderItemPojo) {
		return converterUtil.convertToSalesData(listOfOrderItemPojo);
	}

	public List<SalesReportData> getSalesReportData(List<SalesReportData> salesReportData,
			SalesReportForm salesReportForm) throws ApiException {
		salesReportData = reportService.getSalesReportDataByBrandAndCategory(salesReportData,
				salesReportForm.getBrand(), salesReportForm.getCategory());
		// Group Sales Report Data category wise
		salesReportData = reportService.groupSalesReportDataCategoryWise(salesReportData);
		if (salesReportData.size() == 0) {
			throw new ApiException("There are no sales for given data");
		} else {
			return salesReportData;
		}
	}

	public List<BrandData> getBrandReportData() {
		List<BrandMasterPojo> list = brandService.getAll();
		return converterUtil.convertToBrandData(list);
	}

	public List<InventoryReportData> getInventoryReportData() {
		List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
		List<InventoryReportData> list2 = converterUtil.convertToInventoryReportData(inventoryPojoList);
		// Group list of InventoryReportData brand and category wise
		return reportService.groupDataForInventoryReport(list2);
	}

}
