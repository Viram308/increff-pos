package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	// CRUD operations for product

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductMasterPojo productMasterPojo, BrandMasterPojo brandMasterPojo) throws ApiException {

		// normalize
		normalize(productMasterPojo);
		// check for existing product data with given barcode
		ProductMasterPojo productMasterPojoTemp = dao.selectByBarcode(productMasterPojo.getBarcode());
		if (productMasterPojoTemp == null) {
			// if not exists then insert
			dao.insert(productMasterPojo);
		} else {
			// if exists then change barcode
			String barcode = StringUtil.getAlphaNumericString();
			ProductMasterPojo productMasterPojoFinal = new ProductMasterPojo();
			productMasterPojoFinal.setBrand_category_id(brandMasterPojo.getId());
			productMasterPojoFinal.setName(productMasterPojo.getName());
			productMasterPojoFinal.setBarcode(barcode);
			productMasterPojoFinal.setMrp(productMasterPojo.getMrp());
			add(productMasterPojoFinal, brandMasterPojo);
		}
	}

	@Transactional
	public void delete(int id) {
		dao.delete(ProductMasterPojo.class, id);
	}

	public List<ProductMasterPojo> searchData(ProductMasterPojo productMasterPojo, List<Integer> brandIds) {
		normalize(productMasterPojo);
		return dao.searchData(productMasterPojo.getBarcode(), productMasterPojo.getName(), brandIds);
	}

	public List<ProductMasterPojo> searchData(List<Integer> brandIds) {
		return dao.searchData(brandIds);
	}

	public List<ProductMasterPojo> searchData(ProductMasterPojo productMasterPojo) {
		normalize(productMasterPojo);
		return dao.searchData(productMasterPojo.getBarcode(), productMasterPojo.getName());
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductMasterPojo get(int id) {
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
	public void update(int id, ProductMasterPojo productMasterPojo, BrandMasterPojo brandMasterPojo)
			throws ApiException {
		normalize(productMasterPojo);
		ProductMasterPojo productMasterPojoUpdate = getCheck(id);
		productMasterPojoUpdate.setBrand_category_id(brandMasterPojo.getId());
		productMasterPojoUpdate.setName(productMasterPojo.getName());
		productMasterPojoUpdate.setMrp(productMasterPojo.getMrp());
		dao.update(productMasterPojoUpdate);
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
