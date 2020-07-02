package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandMasterPojo;

@Repository
public class BrandDao extends AbstractDao {

	// delete according to id
	private static String delete_id = "delete from BrandMasterPojo b where id=:id";
	// select according to brand and category
	private static String select_brand_category = "select b from BrandMasterPojo b where brand=:brand and category=:category";
	// select id according to brand and category
	private static String select_brand_category_id = "select id from BrandMasterPojo b where brand=:brand and category=:category";
	// select according to id
	private static String select_id = "select b from BrandMasterPojo b where id=:id";
	// select all
	private static String select_all = "select b from BrandMasterPojo b";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandMasterPojo b) {
		em.persist(b);
	}

	// delete according to id
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// select id according to brand and category
	public int selectId(String brand, String category) {
		TypedQuery<Integer> query = getQuery(select_brand_category_id, Integer.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList().stream().findFirst().orElse(-1);
	}

	// select according to brand and category
	public BrandMasterPojo selectByBrandCategory(String brand, String category) {
		TypedQuery<BrandMasterPojo> query = getQuery(select_brand_category, BrandMasterPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	// select according to id
	public BrandMasterPojo select(int id) {
		TypedQuery<BrandMasterPojo> query = getQuery(select_id, BrandMasterPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// select all
	public List<BrandMasterPojo> selectAll() {
		TypedQuery<BrandMasterPojo> query = getQuery(select_all, BrandMasterPojo.class);
		return query.getResultList();
	}

	public void update(BrandMasterPojo p) {
	}

}
