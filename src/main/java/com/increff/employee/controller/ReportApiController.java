package com.increff.employee.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private OrderService oService;

	@Autowired
	private OrderItemService iService;

	@Autowired
	private ProductService pService;
	@Autowired
	private BrandService bService;

	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(path = "/api/salesreport", method = RequestMethod.POST)
	public List<SalesReportData> get(@RequestBody SalesReportForm salesReportForm) throws ParseException, ApiException {
		return getSalesReportData(salesReportForm.getStartdate(), salesReportForm.getEnddate(),
				StringUtil.toLowerCase(salesReportForm.getBrand()), StringUtil.toLowerCase(salesReportForm.getCategory()));
	}

	private List<SalesReportData> getSalesReportData(String startdate, String enddate, String brand, String category)
			throws ParseException, ApiException {
		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
		List<OrderPojo> o = oService.getAll();
		List<Integer> orderIds = new ArrayList<Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		for (OrderPojo p : o) {
			String receivedDate = p.getDatetime().split(" ")[0];
			if ((sdf.parse(startdate).before(sdf.parse(receivedDate))
					|| sdf.parse(startdate).equals(sdf.parse(receivedDate)))
					&& (sdf.parse(receivedDate).before(sdf.parse(enddate))
							|| sdf.parse(receivedDate).equals(sdf.parse(enddate)))) {
				orderIds.add(p.getId());
			}
		}
		if (orderIds.size() == 0) {
			throw new ApiException("There are no orders for given dates");
		} else {
			List<OrderItemPojo> listOfOrderItemPojo = iService.getList(orderIds);
			int i, j;
			for (i = 0; i < listOfOrderItemPojo.size(); i++) {
				for (j = i + 1; j < listOfOrderItemPojo.size(); j++) {
					if (listOfOrderItemPojo.get(j).getProductId() == listOfOrderItemPojo.get(i).getProductId()) {
						listOfOrderItemPojo.get(i).setQuantity(
								listOfOrderItemPojo.get(i).getQuantity() + listOfOrderItemPojo.get(j).getQuantity());
						try {
							listOfOrderItemPojo.remove(j);
							j--;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			for (i = 0; i < listOfOrderItemPojo.size(); i++) {
				SalesReportData salesProductData = new SalesReportData();
				ProductMasterPojo p = pService.get(listOfOrderItemPojo.get(i).getId());
				BrandMasterPojo b = bService.get(p.getBrand_category());
				salesProductData.setBrand(b.getBrand());
				salesProductData.setCategory(b.getCategory());
				salesProductData.setQuantity(listOfOrderItemPojo.get(i).getQuantity());
				salesProductData.setRevenue(
						listOfOrderItemPojo.get(i).getQuantity() * listOfOrderItemPojo.get(i).getSellingPrice());
				salesReportData.add(salesProductData);
			}

			if (brand.isEmpty() && category.isEmpty()) {
				// do nothing
			} else if ((!brand.isEmpty()) && category.isEmpty()) {
				for (i = 0; i < salesReportData.size(); i++) {
					if (!salesReportData.get(i).getBrand().equals(brand)) {
						salesReportData.remove(i);
						i--;
					}
				}
			} else if (brand.isEmpty() && (!category.isEmpty())) {
				for (i = 0; i < salesReportData.size(); i++) {
					if (!salesReportData.get(i).getCategory().equals(category)) {
						salesReportData.remove(i);
						i--;
					}
				}
			} else if ((!(brand.isEmpty())) && (!(category.isEmpty()))) {
				for (i = 0; i < salesReportData.size(); i++) {
					if (!salesReportData.get(i).getBrand().equals(brand)
							|| !salesReportData.get(i).getCategory().equals(category)) {
						salesReportData.remove(i);
						i--;
					}
				}
			}
			for (i = 0; i < salesReportData.size(); i++) {
				for (j = i + 1; j < salesReportData.size(); j++) {
					if (salesReportData.get(j).getCategory().equals(salesReportData.get(i).getCategory())) {
						salesReportData.get(i).setQuantity(
								salesReportData.get(i).getQuantity() + salesReportData.get(j).getQuantity());
						salesReportData.get(i)
								.setRevenue(salesReportData.get(i).getRevenue() + salesReportData.get(j).getRevenue());
						try {
							salesReportData.remove(j);
							j--;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
			for (i = 0; i < salesReportData.size(); i++) {
				salesReportData.get(i).setId(i + 1);
			}
			return salesReportData;
		}
	}
}