package com.increff.pos.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ReportService {

	public List<Integer> getOrderIdList(List<OrderPojo> o, String startdate, String enddate)
			throws ParseException, ApiException {
		List<Integer> orderIds = new ArrayList<Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		for (OrderPojo p : o) {
			// Split datetime with space and get first element of array as date
			String receivedDate = p.getDatetime().split(" ")[0];
			// Compares date with startdate and enddate
			if ((sdf.parse(startdate).before(sdf.parse(receivedDate))
					|| sdf.parse(startdate).equals(sdf.parse(receivedDate)))
					&& (sdf.parse(receivedDate).before(sdf.parse(enddate))
							|| sdf.parse(receivedDate).equals(sdf.parse(enddate)))) {
				// Add id to array
				orderIds.add(p.getId());
			}
		}
		if (orderIds.size() == 0) {
			throw new ApiException("There are no orders for given dates");
		}
		return orderIds;
	}

	public List<SalesReportData> getSalesReportDataByBrandAndCategory(List<SalesReportData> salesReportData,
			String brand, String category) {
		brand = StringUtil.toLowerCase(brand);
		category = StringUtil.toLowerCase(category);
		if (brand.isBlank() && category.isBlank()) {
			// do nothing
			return salesReportData;
		} else if ((!brand.isBlank()) && category.isBlank()) {
			// Select SalesReportData brand wise
			String finalBrand = brand;
			return salesReportData.stream().filter(o -> o.brand.equals(finalBrand)).collect(Collectors.toList());
		} else if (brand.isBlank() && (!category.isBlank())) {
			// Select SalesReportData category wise
			String finalCategory = category;
			return salesReportData.stream().filter(o -> o.category.equals(finalCategory)).collect(Collectors.toList());
		}
		// Select SalesReportData brand and category wise
		String finalBrand = brand;
		String finalCategory = category;
		return salesReportData.stream().filter(o -> (o.category.equals(finalCategory) && o.brand.equals(finalBrand)))
				.collect(Collectors.toList());
	}

	public List<SalesReportData> groupSalesReportDataCategoryWise(List<SalesReportData> salesReportDatas)
			throws ApiException {
		LinkedHashMap<String, SalesReportData> m = new LinkedHashMap<String, SalesReportData>();
		for (SalesReportData salesReportData : salesReportDatas) {
			// check key already exists
			if (m.containsKey(salesReportData.category)) {
				// update existing one
				SalesReportData salesReportDataExisting = m.get(salesReportData.category);
				salesReportDataExisting.quantity = salesReportDataExisting.quantity + salesReportData.quantity;
				salesReportDataExisting.revenue = salesReportDataExisting.revenue + salesReportData.revenue;
				m.put(salesReportData.category, salesReportDataExisting);
			} else {
				// create new one
				m.put(salesReportData.category, salesReportData);
			}
		}
		Collection<SalesReportData> values = m.values();
		// convert hashmap to list
		List<SalesReportData> salesDataList = new ArrayList<SalesReportData>(values);
		if (salesDataList.isEmpty()) {
			throw new ApiException("There are no sales for given data");
		}
		return salesDataList;
	}

	public List<InventoryReportData> groupDataForInventoryReport(List<InventoryReportData> list) {
		LinkedHashMap<List<String>, InventoryReportData> map = new LinkedHashMap<List<String>, InventoryReportData>();
		for (InventoryReportData inventoryReportData : list) {
			List<String> key = new ArrayList<String>(
					Arrays.asList(inventoryReportData.brand, inventoryReportData.category));
			// check key already exists
			if (map.containsKey(key)) {
				// update existing one
				InventoryReportData in = map.get(key);
				in.quantity = in.quantity + inventoryReportData.quantity;
				map.put(key, in);
			} else {
				// create new one
				map.put(key, inventoryReportData);
			}
		}
		Collection<InventoryReportData> values = map.values();
		// convert hashmap to list
		List<InventoryReportData> inventoryReportDataList = new ArrayList<InventoryReportData>(values);
		return inventoryReportDataList;
	}

}
