package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandMasterPojo;

@Repository
public class BrandDao extends AbstractDao {

	// select according to brand and category
	private static String select_brand_category = "select b from BrandMasterPojo b where brand=:brand and category=:category";
	// select all
	private static String select_all = "select b from BrandMasterPojo b";

	@PersistenceContext
	private EntityManager em;

	// select according to brand and category
	public BrandMasterPojo selectByBrandCategory(String brand, String category) {
		TypedQuery<BrandMasterPojo> query = getQuery(select_brand_category, BrandMasterPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	// select all
	public List<BrandMasterPojo> selectAll() {
		TypedQuery<BrandMasterPojo> query = getQuery(select_all, BrandMasterPojo.class);
		return query.getResultList();
	}

}
