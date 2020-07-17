package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	// CRUD operations for product

	@Transactional(rollbackFor = ApiException.class)
	public ProductMasterPojo add(ProductMasterPojo productMasterPojo, BrandMasterPojo brandMasterPojo)
			throws ApiException {

		// normalize
		NormalizeUtil.normalizeProductMasterPojo(productMasterPojo);
		// check for existing product data with given barcode
		ProductMasterPojo productMasterPojoTemp = dao.selectByBarcode(productMasterPojo.getBarcode());
		if (productMasterPojoTemp == null) {
			// if not exists then insert
			dao.insert(productMasterPojo);
			return productMasterPojo;
		}
		// if exists then change barcode
		String barcode = StringUtil.getAlphaNumericString();
		ProductMasterPojo productMasterPojoFinal = new ProductMasterPojo();
		productMasterPojoFinal.setBrand_category_id(brandMasterPojo.getId());
		productMasterPojoFinal.setName(productMasterPojo.getName());
		productMasterPojoFinal.setBarcode(barcode);
		productMasterPojoFinal.setMrp(productMasterPojo.getMrp());
		return add(productMasterPojoFinal, brandMasterPojo);
	}

	@Transactional(readOnly = true)
	public List<ProductMasterPojo> searchData(ProductSearchForm productSearchForm) {
		NormalizeUtil.normalizeProductSearchForm(productSearchForm);
		return dao.searchData(productSearchForm.barcode, productSearchForm.name);
	}

	@Transactional(readOnly = true)
	public ProductMasterPojo get(int id) {
		return dao.select(ProductMasterPojo.class, id);
	}

	@Transactional(readOnly = true)
	public ProductMasterPojo getByBarcode(String barcode) throws ApiException {
		barcode = StringUtil.toLowerCase(barcode);
		// check for existing product data with given barcode
		ProductMasterPojo p = dao.selectByBarcode(barcode);
		if (p == null) {
			throw new ApiException("Given Barcode dosen't exist");
		}
		return p;
	}

	@Transactional(readOnly = true)
	public List<ProductMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackFor = ApiException.class)
	public ProductMasterPojo update(int id, ProductMasterPojo productMasterPojo, BrandMasterPojo brandMasterPojo)
			throws ApiException {
		NormalizeUtil.normalizeProductMasterPojo(productMasterPojo);
		ProductMasterPojo productMasterPojoUpdate = getCheck(id);
		productMasterPojoUpdate.setBrand_category_id(brandMasterPojo.getId());
		productMasterPojoUpdate.setName(productMasterPojo.getName());
		productMasterPojoUpdate.setMrp(productMasterPojo.getMrp());
		dao.update(productMasterPojoUpdate);
		return productMasterPojoUpdate;
	}

	@Transactional(readOnly = true)
	public ProductMasterPojo getCheck(int id) throws ApiException {
		ProductMasterPojo p = dao.select(ProductMasterPojo.class, id);
		if (p == null) {
			throw new ApiException("Product doesn't exist for id : " + id);
		}
		return p;
	}

	

}
