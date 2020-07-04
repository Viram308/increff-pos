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
	private static String selectBrandCategory = "select b from BrandMasterPojo b where brand=:brand and category=:category";
	// select all
	private static String selectAll = "select b from BrandMasterPojo b";
	// search
	private static String search = "select b from BrandMasterPojo b where brand like :brand and category like :category";

	@PersistenceContext
	private EntityManager em;

	// select according to brand and category
	public BrandMasterPojo selectByBrandCategory(String brand, String category) {
		TypedQuery<BrandMasterPojo> query = getQuery(selectBrandCategory, BrandMasterPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	// select all
	public List<BrandMasterPojo> selectAll() {
		TypedQuery<BrandMasterPojo> query = getQuery(selectAll, BrandMasterPojo.class);
		return query.getResultList();
	}

	public List<BrandMasterPojo> searchData(String brand, String category) {
		TypedQuery<BrandMasterPojo> query = getQuery(search, BrandMasterPojo.class);
		query.setParameter("brand", brand+"%");
		query.setParameter("category", category+"%");
		return query.getResultList();
	}

}
