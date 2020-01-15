package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.util.StringUtil;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandCategoryPojo b) throws ApiException {

		normalize(b);
		
		if (StringUtil.isEmpty(b.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if (StringUtil.isEmpty(b.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		
		BrandCategoryPojo p = dao.selectByPair(b.getBrand(), b.getCategory());
		if(p==null) {
			dao.insert(b);	
		}
		else {
			throw new ApiException("Given Brand and Category pair already exist");	
		}
		
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandCategoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandCategoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, BrandCategoryPojo p) throws ApiException {
		normalize(p);
		BrandCategoryPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(ex);
	}

	@Transactional
	public BrandCategoryPojo getCheck(int id) throws ApiException {
		BrandCategoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException(
					"Brand and Category not exist for id : " + id);
		}
		return p;
	}

	protected static void normalize(BrandCategoryPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
