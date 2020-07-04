package com.increff.pos.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.BillData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.GeneratePDF;
import com.increff.pos.util.GenerateXML;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderApiController {
	@Autowired
	private OrderDto orderDto;

	@Autowired
	private OrderItemDto orderItemDto;
	// CRUD operations for customer order

	@Transactional(rollbackOn = ApiException.class)
	@ApiOperation(value = "Adds Order")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm[] orderItemForms, HttpServletResponse response)
			throws ApiException, ParserConfigurationException, TransformerException, FOPException, IOException {
		// Check entered inventory with available inventory
		List<OrderItemForm> orderItems = orderDto.groupItemsByBarcode(orderItemForms);
		OrderPojo orderPojo = orderDto.addOrder(orderItems);
		// Convert input to required format
		List<OrderItemPojo> list = orderDto.getOrderItemObject(orderItems, orderPojo);
		// Decrease inventory according to the entered quantity
		orderDto.updateInventory(list);
		// Add each order item
		orderItemDto.add(list);

		// Convert OrderItemPojo to BillData
		List<BillData> billItemList = orderDto.getBillDataObject(list);
		// Generate XML file using BillData list
		GenerateXML.createXml(billItemList, orderPojo.getId());
		// Create PDF from generated XML
		byte[] encodedBytes = GeneratePDF.createPDF();
		// Create response
		response = createResponse(response, encodedBytes);
		// send to output stream
		ServletOutputStream servletOutputStream = response.getOutputStream();
		servletOutputStream.write(encodedBytes);
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	private HttpServletResponse createResponse(HttpServletResponse response, byte[] encodedBytes) {
		String pdfFileName = "output.pdf";
		response.reset();
		response.addHeader("Pragma", "public");
		response.addHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment;filename=" + pdfFileName);
		response.setContentType("application/pdf");

		// avoid "byte shaving" by specifying precise length of transferred data
		response.setContentLength(encodedBytes.length);
		return response;
	}

	@ApiOperation(value = "Deletes Order")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		orderDto.delete(id);
	}

	@ApiOperation(value = "Gets a Order")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public OrderData get(@PathVariable int id) throws ApiException {
		return orderDto.get(id);
	}

	@ApiOperation(value = "Gets list of all Orders")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		return orderDto.getAll();
	}

}
