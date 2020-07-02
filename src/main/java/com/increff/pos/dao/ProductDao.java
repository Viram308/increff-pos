package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductMasterPojo;

@Repository
public class ProductDao extends AbstractDao {

	// select according to barcode
	private static String select_barcode = "select p from ProductMasterPojo p where barcode=:barcode";
	// select all
	private static String select_all = "select p from ProductMasterPojo p";

	// select according to barcode
	public ProductMasterPojo selectByBarcode(String barcode) {
		TypedQuery<ProductMasterPojo> query = getQuery(select_barcode, ProductMasterPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	// select all
	public List<ProductMasterPojo> selectAll() {
		TypedQuery<ProductMasterPojo> query = getQuery(select_all, ProductMasterPojo.class);
		return query.getResultList();
	}

}
