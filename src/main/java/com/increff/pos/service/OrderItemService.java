package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductMasterPojo;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemDao dao;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ProductService productService;
	// CRUD operations for order item

	@Transactional
	public void add(List<OrderItemPojo> list) {
		for (OrderItemPojo o : list) {
			dao.insert(o);
		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(OrderItemPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public OrderItemPojo get(int id) throws ApiException {
		return dao.select(OrderItemPojo.class, id);
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
		OrderItemPojo p = dao.select(OrderItemPojo.class, id);
		if (p == null) {
			throw new ApiException("Order Item not exist for id : " + id);
		}
		return p;
	}

	public void checkInventory(int id, OrderItemForm orderItem) throws ApiException {
		int enteredQuantity, availableQuantity;
		OrderItemPojo orderItemPojo = getCheck(id);
		// Entered quantity
		enteredQuantity = orderItem.getQuantity();
		ProductMasterPojo productMasterPojo = productService.get(orderItemPojo.getProductId());
		// InventoryPojo for available quantity
		InventoryPojo ip = inventoryService.getByProductId(productMasterPojo);
		availableQuantity = ip.getQuantity() + orderItemPojo.getQuantity();
		// Check quantity
		if (enteredQuantity == availableQuantity) {
			throw new ApiException("Available Inventory for Barcode " + orderItem.getBarcode()
					+ " will be 0 !! Please enter lesser quantity !");
		}
		if (enteredQuantity > availableQuantity) {
			throw new ApiException(
					"Available Inventory for Barcode " + orderItem.getBarcode() + " is : " + ip.getQuantity());
		} else {
			InventoryPojo ip2 = new InventoryPojo();
			int quantity = ip.getQuantity() + orderItemPojo.getQuantity() - enteredQuantity;
			ip2.setQuantity(quantity);
			inventoryService.update(ip.getId(), ip2);
		}

	}

	public void checkEnteredQuantity(OrderItemForm f) throws ApiException {
		if (f.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero !!");
		}
	}

}
