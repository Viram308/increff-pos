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
		// normalize
		normalize(b);
		// check for existing pair
		getCheckExisting(b.getBrand(), b.getCategory());
		dao.insert(b);
	}

	@Transactional(rollbackOn = ApiException.class)
	public void delete(int id) {
		dao.delete(BrandMasterPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandMasterPojo getByBrandCategory(String brand, String category) throws ApiException {
		// Get pojo
		brand = StringUtil.toLowerCase(brand);
		category = StringUtil.toLowerCase(category);
		return getCheckForBrandCategory(brand, category);
	}

	@Transactional
	public List<BrandMasterPojo> searchData(BrandMasterPojo brandPojo) {
		normalize(brandPojo);
		return dao.searchData(brandPojo.getBrand(), brandPojo.getCategory());
	}

	@Transactional
	public BrandMasterPojo get(int id) {
		return dao.select(BrandMasterPojo.class, id);
	}

	@Transactional
	public List<BrandMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, BrandMasterPojo p) throws ApiException {
		normalize(p);
		BrandMasterPojo brandMasterPojo = getCheck(id);
		brandMasterPojo.setCategory(p.getCategory());
		brandMasterPojo.setBrand(p.getBrand());
		dao.update(brandMasterPojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandMasterPojo getCheck(int id) throws ApiException {
		BrandMasterPojo brandMasterPojo = dao.select(BrandMasterPojo.class, id);
		if (brandMasterPojo == null) {
			throw new ApiException("Brand and Category not exist for id : " + id);
		}
		return brandMasterPojo;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void getCheckExisting(String brand, String category) throws ApiException {
		BrandMasterPojo p = dao.selectByBrandCategory(brand, category);
		if (p != null) {
			throw new ApiException("Given Brand and Category pair already exist");
		}
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandMasterPojo getCheckForBrandCategory(String brand, String category) throws ApiException {
		BrandMasterPojo brandMasterPojo = dao.selectByBrandCategory(StringUtil.toLowerCase(brand),
				StringUtil.toLowerCase(category));
		if (brandMasterPojo == null) {
			throw new ApiException("Given Brand and Category pair dosen't exist");
		}
		return brandMasterPojo;
	}

	public void normalize(BrandMasterPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}

}
