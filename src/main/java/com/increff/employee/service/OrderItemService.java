package com.increff.employee.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
			InventoryPojo ip = iService.getByProductId(o.getProductId());
			int quantity = ip.getQuantity() - o.getQuantity();
			ip.setQuantity(quantity);
			iService.update(ip.getId(), ip);
			dao.insert(o);
		}
		List<BillData> billItemList = getBillDataObject(list);
		GenerateXML.createXml(billItemList, oService.getMax());
		return GeneratePDF.createPDF();
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
