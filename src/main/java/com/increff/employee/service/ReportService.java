package com.increff.employee.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		int i, j;
		for (i = 0; i < listOfOrderItemPojo.size(); i++) {
			for (j = i + 1; j < listOfOrderItemPojo.size(); j++) {
				// Check for duplicate product ids
				if (listOfOrderItemPojo.get(j).getProductMasterPojo().getId() == listOfOrderItemPojo.get(i)
						.getProductMasterPojo().getId()) {
					// Add quantities for same product ids
					listOfOrderItemPojo.get(i).setQuantity(
							listOfOrderItemPojo.get(i).getQuantity() + listOfOrderItemPojo.get(j).getQuantity());
					// Remove duplicate
					listOfOrderItemPojo.remove(j);
					// Reduce index
					j--;
				}
			}
		}
		return listOfOrderItemPojo;
	}

	public List<SalesReportData> getSalesReportDataByBrandAndCategory(List<SalesReportData> salesReportData, String brand,
			String category) {
		int i;
		brand=StringUtil.toLowerCase(brand);
		category=StringUtil.toLowerCase(category);
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
		int i,j;
		for (i = 0; i < salesReportData.size(); i++) {
			for (j = i + 1; j < salesReportData.size(); j++) {
				if (salesReportData.get(j).getCategory().equals(salesReportData.get(i).getCategory())) {
					// Add quantity and revenue
					salesReportData.get(i).setQuantity(
							salesReportData.get(i).getQuantity() + salesReportData.get(j).getQuantity());
					salesReportData.get(i)
							.setRevenue(salesReportData.get(i).getRevenue() + salesReportData.get(j).getRevenue());
					try {
						// Remove duplicate
						salesReportData.remove(j);
						// Reduce index
						j--;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		// Set ids to list items
		for (i = 0; i < salesReportData.size(); i++) {
			salesReportData.get(i).setId(i + 1);
		}
		return salesReportData;
	}
	public List<InventoryReportData> groupDataForInventoryReport(List<InventoryReportData> list) {
		int i, j;
		for (i = 0; i < list.size(); i++) {
			for (j = i + 1; j < list.size(); j++) {
				if (list.get(j).getBrand().equals(list.get(i).getBrand())
						&& list.get(j).getCategory().equals(list.get(i).getCategory())) {
					list.get(i).setQuantity(list.get(i).getQuantity() + list.get(j).getQuantity());
					list.remove(j);
					j--;
				}
			}
		}
		// Set ids to list items
		for (i = 0; i < list.size(); i++) {
			list.get(i).setId(i + 1);
		}
		return list;
	}
}
