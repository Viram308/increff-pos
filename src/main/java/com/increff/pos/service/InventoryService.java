package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductMasterPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	// CRUD operations for inventory

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo i) throws ApiException {
		// check for existing inventory data
		InventoryPojo p = dao.selectByProductId(i.getProductMasterPojo().getId());
		if (p == null) {
			// if not exists then insert
			dao.insert(i);
		} else {
			// if exists then update
			i.setQuantity(i.getQuantity() + p.getQuantity());
			update(p.getId(), i);
		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(InventoryPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int id) throws ApiException {
		return dao.select(InventoryPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo getByProductId(ProductMasterPojo p) throws ApiException {
		// Get inventory data by id
		InventoryPojo i = dao.selectByProductId(p.getId());
		if (i == null) {
			throw new ApiException("Inventory for given product : " + p.getBarcode() + " dosen't exist");
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
		InventoryPojo b = getCheck(id);
		b.setQuantity(p.getQuantity());
		dao.update(b);
	}

	@Transactional
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(InventoryPojo.class, id);
		if (p == null) {
			throw new ApiException("Inventory not exist for id : " + id);
		}
		return p;
	}

}
