package com.increff.pos.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.BillData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.OrderSearchForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;

@Service
public class OrderService {

	@Autowired
	private OrderDao dao;
	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;

	// CRUD operations for order

	@Transactional
	public void add(OrderPojo o) {
		dao.insert(o);
	}

	@Transactional(readOnly = true)
	public OrderPojo get(int id) {
		return dao.select(OrderPojo.class, id);
	}

	@Transactional(readOnly = true)
	public List<OrderPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(readOnly = true)
	public OrderPojo getCheck(int id) throws ApiException {
		OrderPojo p = dao.select(OrderPojo.class, id);
		if (p == null) {
			throw new ApiException("Order not exist for id : " + id);
		}
		return p;
	}
	
	@Transactional(readOnly = true)
	public List<OrderPojo> searchOrder(OrderSearchForm form) {
		return dao.searchOrder(form.orderCreater);
	}

	public void checkInventory(List<OrderItemForm> orderItems) throws ApiException {
		int enteredQuantity;
		for (OrderItemForm i : orderItems) {
			// Entered quantity
			enteredQuantity = i.quantity;
			ProductMasterPojo p = productService.getByBarcode(i.barcode);
			// InventoryPojo for available quantity
			InventoryPojo ip = inventoryService.getByProductId(p);
			// Check quantity
			if (enteredQuantity > ip.getQuantity()) {
				throw new ApiException("Available Inventory for Barcode " + i.barcode + " is : " + ip.getQuantity());
			}
		}
	}

	public void updateInventory(List<OrderItemPojo> list) throws ApiException {
		for (OrderItemPojo o : list) {
			ProductMasterPojo productMasterPojo = productService.get(o.getProductId());
			InventoryPojo ip = inventoryService.getByProductId(productMasterPojo);
			// Decrease quantity
			int quantity = ip.getQuantity() - o.getQuantity();
			ip.setQuantity(quantity);
			inventoryService.update(ip.getId(), ip);
		}
	}

	public List<BillData> getBillDataObject(List<OrderItemPojo> list) throws ApiException {
		List<BillData> bill = new ArrayList<BillData>();
		int i = 1;
		// Convert OrderItemPojo to BillData
		for (OrderItemPojo o : list) {
			ProductMasterPojo p = productService.get(o.getProductId());
			BillData item = new BillData();
			item.id = i;
			item.name = p.getName();
			item.quantity = o.getQuantity();
			item.mrp = o.getSellingPrice();
			i++;
			bill.add(item);
		}
		return bill;
	}

	public List<OrderPojo> getList(List<OrderPojo> orderPojoList, String startdate, String enddate)
			throws ParseException {
		List<OrderPojo> orderList = new ArrayList<OrderPojo>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		for (OrderPojo orderPojo : orderPojoList) {
			// Split datetime with space and get first element of array as date
			String receivedDate = orderPojo.getDatetime().split(" ")[0];
			// Compares date with startdate and enddate
			if ((sdf.parse(startdate).before(sdf.parse(receivedDate))
					|| sdf.parse(startdate).equals(sdf.parse(receivedDate)))
					&& (sdf.parse(receivedDate).before(sdf.parse(enddate))
							|| sdf.parse(receivedDate).equals(sdf.parse(enddate)))) {
				// Add id to array
				orderList.add(orderPojo);
			}
		}
		return orderList;
	}

	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, OrderPojo orderPojo) throws ApiException {
		OrderPojo orderPojoFinal = getCheck(id);
		orderPojoFinal.setDatetime(orderPojo.getDatetime());
		orderPojoFinal.setOrderCreater(orderPojo.getOrderCreater());
		dao.update(orderPojoFinal);
	}

	

}
