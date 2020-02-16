package com.increff.employee.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
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

import com.increff.employee.model.BillData;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.GeneratePDF;
import com.increff.employee.util.GenerateXML;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderService oService;
	@Autowired
	private ProductService pService;
	@Autowired
	private OrderItemService iService;
	@Autowired
	private InventoryService inService;

	@Transactional
	@ApiOperation(value = "Adds Order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm[] orderItems, HttpServletResponse response)
			throws ApiException, ParserConfigurationException, TransformerException, FOPException, IOException {

		checkInventory(orderItems);
		OrderPojo op = new OrderPojo();
		op.setDatetime(getDateTime());
		oService.add(op);
		List<OrderItemPojo> list = getOrderItemObject(orderItems);
		updateInventory(list);
		for (OrderItemPojo o : list) {
			iService.add(o);
		}
		List<BillData> billItemList = getBillDataObject(list);
		GenerateXML.createXml(billItemList, oService.getMax());
		byte[] encodedBytes = GeneratePDF.createPDF();
		String pdfFileName = "output.pdf";

		response.reset();
		response.addHeader("Pragma", "public");
		response.addHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment;filename=" + pdfFileName);
		response.setContentType("application/pdf");

		// avoid "byte shaving" by specifying precise length of transferred data
		response.setContentLength(encodedBytes.length);

		// send to output stream
		ServletOutputStream servletOutputStream = response.getOutputStream();

		servletOutputStream.write(encodedBytes);
		servletOutputStream.flush();
		servletOutputStream.close();

	}

	private void checkInventory(OrderItemForm[] orderItems) throws ApiException {
		int enteredQuantity, pId;
		for (OrderItemForm i : orderItems) {

			enteredQuantity = i.getQuantity();

			ProductMasterPojo p = pService.getByBarcode(i.getBarcode());
			pId = p.getId();
			InventoryPojo ip = inService.getByProductId(pId);
			if (enteredQuantity > ip.getQuantity()) {
				throw new ApiException(
						"Available Inventory for Barcode " + i.getBarcode() + " is : " + ip.getQuantity());
			}
		}
	}

	@ApiOperation(value = "Deletes Order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		oService.delete(id);
	}

	@ApiOperation(value = "Gets a Order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderData get(@PathVariable int id) throws ApiException {
		OrderPojo p = oService.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all Orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		List<OrderPojo> list = oService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	private static OrderData convert(OrderPojo o) {
		OrderData d = new OrderData();
		d.setId(o.getId());
		d.setDatetime(o.getDatetime());
		return d;
	}

	private List<OrderItemPojo> getOrderItemObject(OrderItemForm[] orderItems) throws ApiException {
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		List<OrderItemForm> orderItemList = new LinkedList<OrderItemForm>(Arrays.asList(orderItems));
		int i, j, orderId = oService.getMax();
		for (i = 0; i < orderItemList.size(); i++) {
			for (j = i + 1; j < orderItemList.size(); j++) {
				if (orderItemList.get(j).getBarcode().equals(orderItemList.get(i).getBarcode())) {
					orderItemList.get(i)
							.setQuantity(orderItemList.get(i).getQuantity() + orderItemList.get(j).getQuantity());
					try {
						orderItemList.remove(j);
						j--;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		for (OrderItemForm o : orderItemList) {
			ProductMasterPojo p = pService.getByBarcode(o.getBarcode());
			OrderItemPojo item = new OrderItemPojo();
			item.setOrderid(orderId);
			item.setProductId(p.getId());
			item.setQuantity(o.getQuantity());
			item.setSellingPrice(o.getMrp());
			list.add(item);
		}
		return list;
	}

	private void updateInventory(List<OrderItemPojo> list) throws ApiException {
		for (OrderItemPojo o : list) {
			InventoryPojo ip = inService.getByProductId(o.getProductId());
			int quantity = ip.getQuantity() - o.getQuantity();
			ip.setQuantity(quantity);
			inService.update(ip.getId(), ip);
		}
	}

	private List<BillData> getBillDataObject(List<OrderItemPojo> list) throws ApiException {
		List<BillData> bill = new ArrayList<BillData>();
		int i = 1;
		for (OrderItemPojo o : list) {
			ProductMasterPojo p = pService.get(o.getProductId());
			BillData item = new BillData();
			item.setId(i);
			item.setName(p.getName());
			item.setQuantity(o.getQuantity());
			item.setMrp(o.getSellingPrice());
			i++;
			bill.add(item);
		}
		return bill;
	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}
}
