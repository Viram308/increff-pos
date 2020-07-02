package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;
	@Autowired
	private ProductService service;

	// CRUD operations for product

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductMasterPojo b) throws ApiException {

		// normalize
		normalize(b);
		// check for existing product data with given barcode
		ProductMasterPojo p = dao.selectByBarcode(b.getBarcode());
		if (p == null) {
			// if not exists then insert
			dao.insert(b);
		} else {
			// if exists then change barcode
			String barcode = StringUtil.getAlphaNumericString();
			ProductMasterPojo pr = new ProductMasterPojo();
			pr.setBrand_category(b.getBrand_category());
			pr.setName(b.getName());
			pr.setBarcode(barcode);
			pr.setMrp(b.getMrp());
			service.add(pr);
		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(ProductMasterPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductMasterPojo get(int id) throws ApiException {
		return dao.select(ProductMasterPojo.class, id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductMasterPojo getByBarcode(String barcode) throws ApiException {
		if (barcode.isBlank()) {
			throw new ApiException("Please enter barcode !!");
		}
		barcode = StringUtil.toLowerCase(barcode);
		// check for existing product data with given barcode
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
		ProductMasterPojo p = dao.select(ProductMasterPojo.class, id);
		if (p == null) {
			throw new ApiException("Product not exist for id : " + id);
		}
		return p;
	}

	public static void normalize(ProductMasterPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
}
