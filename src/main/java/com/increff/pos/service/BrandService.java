package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.util.StringUtil;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	// CRUD operations for brand

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandMasterPojo b) throws ApiException {
		// check input data
		checkData(b);
		// normalize
		normalize(b);
		// check for existing pair
		BrandMasterPojo p = dao.selectByPair(b.getBrand(), b.getCategory());
		if (p == null) {
			dao.insert(b);
		} else {
			throw new ApiException("Given Brand and Category pair already exist");
		}

	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public int getId(String brand, String category) throws ApiException {
		if (brand.isBlank() || category.isBlank()) {
			throw new ApiException("Please enter brand and category !!");
		}
		// Get id
		int id = dao.selectId(StringUtil.toLowerCase(brand), StringUtil.toLowerCase(category));
		if (id > 0) {
			return id;
		} else {
			throw new ApiException("Given Brand and Category pair dosen't exist");
		}
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandMasterPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, BrandMasterPojo p) throws ApiException {
		checkData(p);
		normalize(p);
		BrandMasterPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(ex);
	}

	@Transactional
	public BrandMasterPojo getCheck(int id) throws ApiException {
		BrandMasterPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand and Category not exist for id : " + id);
		}
		return p;
	}

	public void checkData(BrandMasterPojo b) throws ApiException {
		if (b.getBrand().isBlank() || b.getCategory().isBlank()) {
			throw new ApiException("Please enter brand and category !!");
		}
	}

	protected static void normalize(BrandMasterPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
