package com.increff.employee.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ReportService {

	public List<Integer> getOrderIdList(List<OrderPojo> o, String startdate, String enddate) throws ParseException {
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
		return orderIds;
	}

	public List<OrderItemPojo> groupOrderItemPojoByProductId(List<OrderItemPojo> listOfOrderItemPojo) {
		int i;
		LinkedHashMap<Integer, OrderItemPojo> m = new LinkedHashMap<Integer, OrderItemPojo>();
		for (i = 0; i < listOfOrderItemPojo.size(); i++) {
			// check key already exists
			if (m.containsKey(listOfOrderItemPojo.get(i).getProductMasterPojo().getId())) {
				// update existing one
				OrderItemPojo o = m.get(listOfOrderItemPojo.get(i).getProductMasterPojo().getId());
				o.setQuantity(o.getQuantity() + listOfOrderItemPojo.get(i).getQuantity());
				m.put(listOfOrderItemPojo.get(i).getProductMasterPojo().getId(), o);
			} else {
				// create new one
				m.put(listOfOrderItemPojo.get(i).getProductMasterPojo().getId(), listOfOrderItemPojo.get(i));
			}
		}
		Collection<OrderItemPojo> values = m.values();
		// convert hashmap to list
		List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>(values);
		return orderItemPojoList;
	}

	public List<SalesReportData> getSalesReportDataByBrandAndCategory(List<SalesReportData> salesReportData,
			String brand, String category) {
		int i;
		brand = StringUtil.toLowerCase(brand);
		category = StringUtil.toLowerCase(category);
		if (brand.isBlank() && category.isBlank()) {
			// do nothing
		} else if ((!brand.isBlank()) && category.isBlank()) {
			// Select SalesReportData brand wise
			for (i = 0; i < salesReportData.size(); i++) {
				if (!salesReportData.get(i).getBrand().equals(brand)) {
					salesReportData.remove(i);
					i--;
				}
			}
		} else if (brand.isBlank() && (!category.isBlank())) {
			// Select SalesReportData category wise
			for (i = 0; i < salesReportData.size(); i++) {
				if (!salesReportData.get(i).getCategory().equals(category)) {
					salesReportData.remove(i);
					i--;
				}
			}
		} else if ((!(brand.isBlank())) && (!(category.isBlank()))) {
			// Select SalesReportData brand and category wise
			for (i = 0; i < salesReportData.size(); i++) {
				if (!salesReportData.get(i).getBrand().equals(brand)
						|| !salesReportData.get(i).getCategory().equals(category)) {
					salesReportData.remove(i);
					i--;
				}
			}
		}
		return salesReportData;
	}

	public List<SalesReportData> groupSalesReportDataCategoryWise(List<SalesReportData> salesReportData) {
		int i;
		LinkedHashMap<String, SalesReportData> m = new LinkedHashMap<String, SalesReportData>();
		for (i = 0; i < salesReportData.size(); i++) {
			// check key already exists
			if (m.containsKey(salesReportData.get(i).getCategory())) {
				// update existing one
				SalesReportData o = m.get(salesReportData.get(i).getCategory());
				o.setQuantity(o.getQuantity() + salesReportData.get(i).getQuantity());
				o.setRevenue(o.getRevenue() + salesReportData.get(i).getRevenue());
				m.put(salesReportData.get(i).getCategory(), o);
			} else {
				// create new one
				m.put(salesReportData.get(i).getCategory(), salesReportData.get(i));
			}
		}
		Collection<SalesReportData> values = m.values();
		// convert hashmap to list
		List<SalesReportData> salesDataList = new ArrayList<SalesReportData>(values);
		for (i = 0; i < salesDataList.size(); i++) {
			salesDataList.get(i).setId(i + 1);
		}
		return salesDataList;
	}

	public List<InventoryReportData> groupDataForInventoryReport(List<InventoryReportData> list) {
		int i;
		LinkedHashMap<List<String>, InventoryReportData> map = new LinkedHashMap<List<String>, InventoryReportData>();
		for (i = 0; i < list.size(); i++) {
			List<String> key = new ArrayList<String>(Arrays.asList(list.get(i).getBrand(), list.get(i).getCategory()));
			// check key already exists
			if (map.containsKey(key)) {
				// update existing one
				InventoryReportData in = map.get(key);
				in.setQuantity(in.getQuantity() + list.get(i).getQuantity());
				map.put(key, in);
			} else {
				// create new one
				map.put(key, list.get(i));
			}
		}
		Collection<InventoryReportData> values = map.values();
		// convert hashmap to list
		List<InventoryReportData> inventoryReportDataList = new ArrayList<InventoryReportData>(values);
		for (i = 0; i < inventoryReportDataList.size(); i++) {
			inventoryReportDataList.get(i).setId(i + 1);
		}
		return inventoryReportDataList;
	}
}
