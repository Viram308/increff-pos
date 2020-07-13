package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.util.NormalizeUtil;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	// CRUD operations for brand

	@Transactional(rollbackFor = ApiException.class)
	public BrandMasterPojo add(BrandMasterPojo brandMasterPojo) throws ApiException {
		// normalize
		NormalizeUtil.normalizeBrandMasterPojo(brandMasterPojo);
		// check for existing pair
		getCheckExisting(brandMasterPojo.getBrand(), brandMasterPojo.getCategory());
		dao.insert(brandMasterPojo);
		return brandMasterPojo;
	}

	@Transactional(readOnly = true)
	public BrandMasterPojo getByBrandCategory(BrandForm brandForm) throws ApiException {
		NormalizeUtil.normalizeBrandForm(brandForm);
		return getCheckForBrandCategory(brandForm);
	}

	@Transactional(readOnly = true)
	public List<BrandMasterPojo> searchBrandData(BrandForm form) {
		NormalizeUtil.normalizeBrandForm(form);
		return dao.searchData(form.brand, form.category);
	}

	@Transactional(readOnly = true)
	public BrandMasterPojo get(int id) {
		return dao.select(BrandMasterPojo.class, id);
	}

	@Transactional(readOnly = true)
	public List<BrandMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackFor = ApiException.class)
	public BrandMasterPojo update(int id, BrandMasterPojo p) throws ApiException {
		NormalizeUtil.normalizeBrandMasterPojo(p);
		BrandMasterPojo brandMasterPojo = getCheck(id);
		brandMasterPojo.setCategory(p.getCategory());
		brandMasterPojo.setBrand(p.getBrand());
		dao.update(brandMasterPojo);
		return brandMasterPojo;
	}

	@Transactional(readOnly = true)
	public BrandMasterPojo getCheck(int id) throws ApiException {
		BrandMasterPojo brandMasterPojo = dao.select(BrandMasterPojo.class, id);
		if (brandMasterPojo == null) {
			throw new ApiException("Brand and Category not exist for id : " + id);
		}
		return brandMasterPojo;
	}

	@Transactional(readOnly = true)
	public void getCheckExisting(String brand, String category) throws ApiException {
		BrandMasterPojo p = dao.selectByBrandCategory(brand, category);
		if (p != null) {
			throw new ApiException("Given Brand and Category pair already exist");
		}
	}

	@Transactional(readOnly = true)
	public BrandMasterPojo getCheckForBrandCategory(BrandForm brandForm) throws ApiException {
		BrandMasterPojo brandMasterPojo = dao.selectByBrandCategory(brandForm.brand, brandForm.category);
		if (brandMasterPojo == null) {
			throw new ApiException("Given Brand and Category pair dosen't exist");
		}
		return brandMasterPojo;
	}

}
