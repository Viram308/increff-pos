package com.increff.employee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderService oService;

	@Autowired
	private OrderItemService iService;

	@ApiOperation(value = "Adds Order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm[] orderItems, HttpServletResponse response)
			throws ApiException, ParserConfigurationException, TransformerException, FOPException, IOException {
		OrderPojo o = new OrderPojo();
		oService.add(o, orderItems);
		byte[] encodedBytes = iService.add(orderItems);
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_PDF);
//	    // Here you have to set the actual filename of your pdf
//	    String filename = "output.pdf";
//	    headers.setContentDispositionFormData(filename, filename);
//	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
//	    return response;
		// InputStreamResource resource = new InputStreamResource(new
		// FileInputStream("F:\\Repos\\Home-assignment\\increff-pos\\src\\main\\resources\\com\\increff\\employee\\resultPDF.pdf"));
		// return resource;

//		response.setContentType("application/pdf");
//		response.setHeader("Content-Disposition", "attachment; filename=\"out.pdf\"");
//
//		InputStream inputStream = new FileInputStream(
//				"F:\\Repos\\Home-assignment\\increff-pos\\src\\main\\resources\\com\\increff\\employee\\resultPDF.pdf");
//
//		return outputStream -> {
//			int nRead;
//			byte[] data = new byte[1024];
//			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
//				System.out.println("Writing some bytes..");
//				outputStream.write(data, 0, nRead);
//			}
//		};
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

}
