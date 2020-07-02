package com.increff.pos.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.BillData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;

@Service
public class OrderService {

	@Autowired
	private OrderDao dao;
	@Autowired
	private ProductService pService;
	@Autowired
	private InventoryService inService;
	@Autowired
	private OrderService oService;

	// CRUD operations for order

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderPojo o) throws ApiException {
		dao.insert(o);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(OrderPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<OrderPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public OrderPojo getCheck(int id) throws ApiException {
		OrderPojo p = dao.select(OrderPojo.class, id);
		if (p == null) {
			throw new ApiException("Order not exist for id : " + id);
		}
		return p;
	}

	public List<OrderItemForm> groupItemsByBarcode(OrderItemForm[] orderItemForms) {
		LinkedHashMap<String, OrderItemForm> m = new LinkedHashMap<String, OrderItemForm>();
		int i;
		for (i = 0; i < orderItemForms.length; i++) {
			// check key already exists
			if (m.containsKey(orderItemForms[i].getBarcode())) {
				// update existing one
				OrderItemForm o = m.get(orderItemForms[i].getBarcode());
				o.setQuantity(o.getQuantity() + orderItemForms[i].getQuantity());
				m.put(orderItemForms[i].getBarcode(), o);
			} else {
				// create new one
				m.put(orderItemForms[i].getBarcode(), orderItemForms[i]);
			}
		}
		Collection<OrderItemForm> values = m.values();
		// convert hashmap to list
		List<OrderItemForm> orderItemList = new ArrayList<OrderItemForm>(values);
		return orderItemList;
	}

	public void checkInventory(List<OrderItemForm> orderItems) throws ApiException {
		int enteredQuantity;
		for (OrderItemForm i : orderItems) {
			// Entered quantity
			enteredQuantity = i.getQuantity();
			ProductMasterPojo p = pService.getByBarcode(i.getBarcode());
			// InventoryPojo for available quantity
			InventoryPojo ip = inService.getByProductId(p);
			// Check quantity
			if (enteredQuantity == ip.getQuantity()) {
				throw new ApiException("Available Inventory for Barcode " + i.getBarcode()
						+ " will be 0 !! Please enter lesser quantity !");
			}
			if (enteredQuantity > ip.getQuantity()) {
				throw new ApiException(
						"Available Inventory for Barcode " + i.getBarcode() + " is : " + ip.getQuantity());
			}
		}
	}

	public List<OrderItemPojo> getOrderItemObject(List<OrderItemForm> orderItemList, OrderPojo op) throws ApiException {
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		int orderId = op.getId();
		// Convert OrderItemForm to OrderItemPojo
		for (OrderItemForm o : orderItemList) {
			ProductMasterPojo p = pService.getByBarcode(o.getBarcode());
			OrderItemPojo item = new OrderItemPojo();
			item.setOrderPojo(oService.get(orderId));
			item.setProductMasterPojo(p);
			item.setQuantity(o.getQuantity());
			item.setSellingPrice(o.getMrp());
			list.add(item);
		}
		return list;
	}

	public void updateInventory(List<OrderItemPojo> list) throws ApiException {
		for (OrderItemPojo o : list) {
			InventoryPojo ip = inService.getByProductId(o.getProductMasterPojo());
			// Decrease quantity
			int quantity = ip.getQuantity() - o.getQuantity();
			ip.setQuantity(quantity);
			inService.update(ip.getId(), ip);
		}
	}

	public List<BillData> getBillDataObject(List<OrderItemPojo> list) throws ApiException {
		List<BillData> bill = new ArrayList<BillData>();
		int i = 1;
		// Convert OrderItemPojo to BillData
		for (OrderItemPojo o : list) {
			ProductMasterPojo p = o.getProductMasterPojo();
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
