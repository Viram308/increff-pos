package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandCategoryPojo;

@Repository
public class BrandDao {

	private static String delete_id = "delete from BrandCategoryPojo b where id=:id";
	private static String select_brand_category = "select b from BrandCategoryPojo b where brand=:brand and category=:category";
	private static String select_id = "select b from BrandCategoryPojo b where id=:id";
	private static String select_all = "select b from BrandCategoryPojo b";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandCategoryPojo b) {
		em.persist(b);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	
	public BrandCategoryPojo selectByPair(String brand, String category) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brand_category);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	public BrandCategoryPojo select(int id) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_id);
		query.setParameter("id", id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	
	public List<BrandCategoryPojo> selectAll() {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_all);
		return query.getResultList();
	}

	public void update(BrandCategoryPojo p) {
	}

	TypedQuery<BrandCategoryPojo> getQuery(String jpql) {
		return em.createQuery(jpql, BrandCategoryPojo.class);
	}
}
