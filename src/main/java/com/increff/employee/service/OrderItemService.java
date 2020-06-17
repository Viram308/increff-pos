package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemDao dao;
	@Autowired
	private InventoryService inService;
	// CRUD operations for order item

	@Transactional
	public void add(List<OrderItemPojo> list) {
		for (OrderItemPojo o : list) {
			dao.insert(o);
		}
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
	public List<OrderItemPojo> getList(List<Integer> orderIds) {
		return dao.selectList(orderIds);
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

	public void checkInventory(int id, OrderItemForm orderItem) throws ApiException {
		int enteredQuantity, availableQuantity;
		OrderItemPojo o = getCheck(id);
		// Entered quantity
		enteredQuantity = orderItem.getQuantity();
		// InventoryPojo for available quantity
		InventoryPojo ip = inService.getByProductId(o.getProductMasterPojo());
		availableQuantity = ip.getQuantity() + o.getQuantity();
		// Check quantity
		if(enteredQuantity == availableQuantity) {
			throw new ApiException(
					"Available Inventory for Barcode " + orderItem.getBarcode() + " will be 0 !! Please enter lesser quantity !");
		}
		if (enteredQuantity > availableQuantity) {
			throw new ApiException(
					"Available Inventory for Barcode " + orderItem.getBarcode() + " is : " + ip.getQuantity());
		} else {
			updateInventory(o, ip, enteredQuantity);
		}

	}

	public void updateInventory(OrderItemPojo o, InventoryPojo ip2, int enteredQuantity) throws ApiException {
		InventoryPojo ip = new InventoryPojo();
		int quantity = ip2.getQuantity() + o.getQuantity() - enteredQuantity;
		ip.setQuantity(quantity);
		inService.update(ip2.getId(), ip);

	}

	public void checkEnteredQuantity(OrderItemForm f) throws ApiException {
		if (f.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero !!");
		}
	}

}
