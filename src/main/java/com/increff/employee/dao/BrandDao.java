package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandMasterPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String delete_id = "delete from BrandMasterPojo b where id=:id";
	private static String select_brand_category = "select b from BrandMasterPojo b where brand=:brand and category=:category";
	private static String select_brand_category_id = "select id from BrandMasterPojo b where brand=:brand and category=:category";
	private static String select_id = "select b from BrandMasterPojo b where id=:id";
	private static String select_all = "select b from BrandMasterPojo b";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandMasterPojo b) {
		em.persist(b);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public int selectId(String brand, String category) {
		TypedQuery<Integer> query = getQuery(select_brand_category_id, Integer.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList().stream().findFirst().orElse(-1);
	}

	public BrandMasterPojo selectByPair(String brand, String category) {
		TypedQuery<BrandMasterPojo> query = getQuery(select_brand_category,BrandMasterPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	public BrandMasterPojo select(int id) {
		TypedQuery<BrandMasterPojo> query = getQuery(select_id,BrandMasterPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandMasterPojo> selectAll() {
		TypedQuery<BrandMasterPojo> query = getQuery(select_all,BrandMasterPojo.class);
		return query.getResultList();
	}

	public void update(BrandMasterPojo p) {
	}

}
