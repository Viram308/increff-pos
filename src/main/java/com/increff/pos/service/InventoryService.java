package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductMasterPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	// CRUD operations for inventory

	@Transactional
	public InventoryPojo add(InventoryPojo inventoryPojo) {
		dao.insert(inventoryPojo);
		return inventoryPojo;
	}

	@Transactional(readOnly = true)
	public InventoryPojo get(int id) {
		return dao.select(InventoryPojo.class, id);
	}

	@Transactional(readOnly = true)
	public InventoryPojo getByProductId(ProductMasterPojo productMasterPojo) {
		// Get inventory data by product id
		return dao.selectByProductId(productMasterPojo.getId());
	}

	@Transactional(readOnly = true)
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackFor = ApiException.class)
	public InventoryPojo update(int id, InventoryPojo p) throws ApiException {
		InventoryPojo inventoryPojo = getCheck(id);
		inventoryPojo.setQuantity(p.getQuantity());
		dao.update(inventoryPojo);
		return inventoryPojo;
	}

	@Transactional(readOnly = true)
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(InventoryPojo.class, id);
		if (p == null) {
			throw new ApiException("Inventory not exist for id : " + id);
		}
		return p;
	}

}
