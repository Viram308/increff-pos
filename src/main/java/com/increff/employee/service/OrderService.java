package com.increff.employee.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;

@Service
public class OrderService {
	@Autowired
	private ProductService pService;

	@Autowired
	private InventoryService iService;

	@Autowired
	private OrderDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderPojo o, OrderItemForm[] orderItems) throws ApiException {

		int enteredQuantity, pId;
		for (OrderItemForm i : orderItems) {

			// OrderItemPojo i = dao.getExistingOrderItem(o.getOrderid(), o.getProductId());
			enteredQuantity = i.getQuantity();

			ProductMasterPojo p = pService.getId(i.getBarcode());
			pId = p.getId();
			InventoryPojo ip = iService.getByProductId(pId);
			if (enteredQuantity > ip.getQuantity()) {
				throw new ApiException(
						"Available Inventory for Barcode " + i.getBarcode() + " is : " + ip.getQuantity());
			}
		}
		o.setDatetime(getDateTime());
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

	public int getMax() {
		return dao.selectMax();
	}

	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

}
