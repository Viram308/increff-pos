package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductMasterPojo b) throws ApiException {

		normalize(b);

		ProductMasterPojo p = dao.selectByBarcode(b.getBarcode());
		if (p == null) {
			dao.insert(b);
		} else {
			String barcode = StringUtil.getAlphaNumericString();
			p.setBrand_category(b.getBrand_category());
			p.setName(b.getName());
			p.setBarcode(barcode);
			p.setMrp(b.getMrp());
			add(p);
		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductMasterPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductMasterPojo getId(String barcode) throws ApiException {
		barcode = StringUtil.toLowerCase(barcode);
		ProductMasterPojo p = dao.selectByBarcode(barcode);
		if (p == null) {
			throw new ApiException("Given Barcode dosen't exist");
		} else {
			return p;
		}
	}

	@Transactional
	public List<ProductMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, ProductMasterPojo p) throws ApiException {
		normalize(p);
		ProductMasterPojo b = getCheck(id);
		b.setBrand_category(p.getBrand_category());
		b.setName(p.getName());
		b.setMrp(p.getMrp());
		dao.update(b);
	}

	@Transactional
	public ProductMasterPojo getCheck(int id) throws ApiException {
		ProductMasterPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product not exist for id : " + id);
		}
		return p;
	}

	protected static void normalize(ProductMasterPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
