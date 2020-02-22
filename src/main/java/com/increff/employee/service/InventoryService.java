package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo i) throws ApiException {
		checkData(i);
		InventoryPojo p = dao.selectByProductId(i.getProductMasterPojo().getId());
		if (p == null) {
			dao.insert(i);
		} else {
			i.setQuantity(i.getQuantity() + p.getQuantity());
			update(p.getId(), i);
		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo getByProductId(int id) throws ApiException {
		InventoryPojo i = dao.selectByProductId(id);
		if (i == null) {
			throw new ApiException("Given Product Id dosen't exist");
		} else {
			return i;
		}
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, InventoryPojo p) throws ApiException {
		checkData(p);
		InventoryPojo b = getCheck(id);
		b.setProductMasterPojo(p.getProductMasterPojo());
		b.setQuantity(p.getQuantity());
		dao.update(b);
	}

	@Transactional
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Inventory not exist for id : " + id);
		}
		return p;
	}

	private void checkData(InventoryPojo i) throws ApiException {
		if (String.valueOf(i.getQuantity()).isBlank()) {
			throw new ApiException("Please enter quantity !!");
		}
		if (i.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero !!");
		}
	}
}
