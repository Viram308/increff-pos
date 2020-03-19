package com.increff.employee.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.model.BillData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;

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
		dao.delete(id);
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
		OrderPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Order not exist for id : " + id);
		}
		return p;
	}

	public List<OrderItemForm> groupItemsByBarcode(OrderItemForm[] orderItemForms) {
		List<OrderItemForm> orderItemList = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		int i, j;
		for (i = 0; i < orderItemList.size(); i++) {
			for (j = i + 1; j < orderItemList.size(); j++) {
				// Check if same barcode exists in given list
				if (orderItemList.get(j).getBarcode().equals(orderItemList.get(i).getBarcode())) {
					// Add both quantities
					orderItemList.get(i)
							.setQuantity(orderItemList.get(i).getQuantity() + orderItemList.get(j).getQuantity());
					// Remove duplicate entry
					orderItemList.remove(j);
					// Reduce index
					j--;
				}
			}
		}
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
