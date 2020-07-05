package com.increff.pos.dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

	public List<BrandData> searchBrandReport(BrandForm brandForm) throws ApiException {
		checkSearchData(brandForm);
		BrandMasterPojo brandPojo = converterUtil.convertBrandFormtoBrandMasterPojo(brandForm);
		List<BrandMasterPojo> list = brandService.searchData(brandPojo);
		return converterUtil.getBrandDataList(list);
	}
	
	public List<InventoryReportData> searchInventoryReport(BrandForm brandForm) throws ApiException {
		checkSearchData(brandForm);
		BrandMasterPojo brandMasterPojo = new BrandMasterPojo();
		brandMasterPojo.setBrand(brandForm.getBrand());
		brandMasterPojo.setCategory(brandForm.getCategory());
		List<BrandMasterPojo> brandMasterPojoList = brandService.searchData(brandMasterPojo);
		List<Integer> brandIds = getBrandIdList(brandMasterPojoList);
		List<ProductMasterPojo> list = productService.searchData(brandIds);
		List<Integer> productIds = getProductIdList(list);
		List<InventoryPojo> inventoryPojoList = inventoryService.searchData(productIds);
		List<InventoryReportData> list2 = converterUtil.convertToInventoryReportData(inventoryPojoList);
		// Group list of InventoryReportData brand and category wise
		return reportService.groupDataForInventoryReport(list2);
	}
	public List<Integer> getProductIdList(List<ProductMasterPojo> productMasterPojoList) {
		List<Integer> productIdList = new ArrayList<Integer>();
		for (ProductMasterPojo productMasterPojo : productMasterPojoList) {
			productIdList.add(productMasterPojo.getId());
		}
		return productIdList;
	}
	public List<Integer> getBrandIdList(List<BrandMasterPojo> brandMasterPojoList) {
		List<Integer> brandIdList = new ArrayList<Integer>();
		for (BrandMasterPojo brandMasterPojo : brandMasterPojoList) {
			brandIdList.add(brandMasterPojo.getId());
		}
		return brandIdList;
	}
	
	public void checkSearchData(BrandForm b) throws ApiException {
		if (b.getBrand().isBlank() && b.getCategory().isBlank()) {
			throw new ApiException("Please enter brand or category !!");
		}
	}

	
}
