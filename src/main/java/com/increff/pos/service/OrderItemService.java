package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemDao dao;
	// CRUD operations for order item

	@Transactional
	public void add(List<OrderItemPojo> list) {
		// enter one by one
		for (OrderItemPojo o : list) {
			dao.insert(o);
		}
	}

	@Transactional
	public void deleteByOrderId(int orderId) {
		dao.deleteByOrderId(orderId);
	}

	@Transactional(readOnly = true)
	public List<OrderItemPojo> getByOrderId(int orderId) throws ApiException {
		return getCheckForOrderId(orderId);
	}

	@Transactional(readOnly = true)
	public List<OrderItemPojo> getList(List<Integer> orderIds) {
		return dao.selectList(orderIds);
	}

	@Transactional(readOnly = true)
	public List<OrderItemPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(readOnly = true)
	public List<OrderItemPojo> getCheckForOrderId(int orderId) throws ApiException {
		List<OrderItemPojo> orderItemPojos = dao.selectByOrderId(orderId);
		if (orderItemPojos.isEmpty()) {
			throw new ApiException("Order Items do not exist for orderId : " + orderId);
		}
		return orderItemPojos;
	}

}
