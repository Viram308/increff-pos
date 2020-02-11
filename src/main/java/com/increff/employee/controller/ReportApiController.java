package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

	@Autowired
	private OrderService oService;

	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(path = "/api/salesreport", method = RequestMethod.GET)
	public List<SalesReportData> get(@RequestBody SalesReportForm salesReportForm) {
		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
		if (salesReportForm.getBrand().isEmpty() && salesReportForm.getCategory().isEmpty()) {
			salesReportData = getSalesReportData(salesReportForm.getStartdate(), salesReportForm.getEnddate());
		}
		return salesReportData;
	}

	private List<SalesReportData> getSalesReportData(String startdate, String enddate) {
		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
		List<OrderPojo> o = oService.getAll();
		List<Integer> orderIds = new ArrayList<Integer>();
		for (OrderPojo p : o) {
			String[] receivedDate = p.getDatetime().split(" ")[0].split("/");
			String[] enteredStartDate = startdate.split("/");
			String[] enteredEndDate = enddate.split("/");
			int receivedYear = Integer.valueOf(receivedDate[2]);
			int receivedMonth = Integer.valueOf(receivedDate[1]);
			int receivedDay = Integer.valueOf(receivedDate[0]);

			int enteredStartYear = Integer.valueOf(enteredStartDate[2]);
			int enteredStartMonth = Integer.valueOf(enteredStartDate[1]);
			int enteredStartDay = Integer.valueOf(enteredStartDate[0]);

			int enteredEndYear = Integer.valueOf(enteredEndDate[2]);
			int enteredEndMonth = Integer.valueOf(enteredEndDate[1]);
			int enteredEndDay = Integer.valueOf(enteredEndDate[0]);

			if (receivedYear >= enteredStartYear && receivedYear <= enteredEndYear) {
				if (receivedMonth >= enteredStartMonth && receivedMonth <= enteredEndMonth) {
					if (receivedDay >= enteredStartDay && receivedDay <= enteredEndDay) {
						orderIds.add(p.getId());
					}
				}
			}
		}

		return salesReportData;
	}
}
