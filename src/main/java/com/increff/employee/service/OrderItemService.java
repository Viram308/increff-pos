package com.increff.employee.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.model.BillData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.util.GeneratePDF;
import com.increff.employee.util.GenerateXML;

@Service
public class OrderItemService {
	@Autowired
	private OrderService oService;

	@Autowired
	private ProductService pService;

	@Autowired
	private OrderItemDao dao;
	@Autowired
	private InventoryService iService;

	@Transactional(rollbackOn = ApiException.class)
	public byte[] add(OrderItemForm[] orderItems)
			throws ApiException, ParserConfigurationException, TransformerException, FOPException, IOException {
		List<OrderItemPojo> list = getOrderItemObject(orderItems);
		for (OrderItemPojo o : list) {
			OrderItemPojo i = dao.getExistingOrderItem(o.getOrderid(), o.getProductId());
			InventoryPojo ip = iService.getByProductId(o.getProductId());
			int quantity = ip.getQuantity() - o.getQuantity();
			ip.setQuantity(quantity);
			iService.update(ip.getId(), ip);
			if (i == null) {
				dao.insert(o);
			} else {
				o.setQuantity(o.getQuantity() + i.getQuantity());
				update(i.getId(), o);
			}
		}
		List<BillData> billItemList = getBillDataObject(list);
		GenerateXML.createXml(billItemList, oService.getMax());
		// File pdffile = new
		// File("F:\\Repos\\Home-assignment\\increff-pos\\src\\main\\resources\\com\\increff\\employee","resultPDF.pdf");
//		if(pdffile.exists()) {
//			if(pdffile.delete())
//			{
//				System.out.println("File deleted successfully");
//			}
//			else {
//				System.out.println("Failureeeeeeeeeeeee");
//			}
//			
//		}
//		if(pdffile.exists())
//		{
//			System.out.println("File Still Exist");
//		}
//		else {
//			System.out.println("Yayyyyyyyyyyyyyy");
//		}

		return GeneratePDF.createPDF();
//		if(pdffile.exists())
//		{
//			System.out.println("File Now Exist");
//		}
//		else {
//			System.out.println("Noooooooooooooooooooooo");
//		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderItemPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<OrderItemPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {
		OrderItemPojo ex = getCheck(id);
		ex.setQuantity(p.getQuantity());
		dao.update(ex);
	}

	@Transactional
	public OrderItemPojo getCheck(int id) throws ApiException {
		OrderItemPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Order Item not exist for id : " + id);
		}
		return p;
	}

	private List<OrderItemPojo> getOrderItemObject(OrderItemForm[] orderItems) throws ApiException {
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		int orderId = oService.getMax();
		for (OrderItemForm o : orderItems) {
			ProductMasterPojo p = pService.getId(o.getBarcode());
			OrderItemPojo item = new OrderItemPojo();
			item.setOrderid(orderId);
			item.setProductId(p.getId());
			item.setQuantity(o.getQuantity());
			item.setSellingPrice(o.getMrp());
			list.add(item);
		}
		return list;
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

}
