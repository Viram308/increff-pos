package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.ProductMasterPojo;

@Repository
public class ProductDao extends AbstractDao {

	private static String delete_id = "delete from ProductMasterPojo p where id=:id";
	private static String select_barcode = "select p from ProductMasterPojo p where barcode=:barcode";
	private static String select_brand_category_id = "select id from ProductMasterPojo p where brand=:brand and category=:category";
	private static String select_id = "select p from ProductMasterPojo p where id=:id";
	private static String select_all = "select p from ProductMasterPojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(ProductMasterPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public ProductMasterPojo selectByBarcode(String barcode) {
		TypedQuery<ProductMasterPojo> query = getQuery(select_barcode, ProductMasterPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public int selectId(String brand, String category) {
		TypedQuery<Integer> query = getQuery(select_brand_category_id, Integer.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getSingleResult();
	}

	public ProductMasterPojo selectByPair(String brand, String category) {
		TypedQuery<ProductMasterPojo> query = getQuery(select_barcode, ProductMasterPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	public ProductMasterPojo select(int id) {
		TypedQuery<ProductMasterPojo> query = getQuery(select_id, ProductMasterPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<ProductMasterPojo> selectAll() {
		TypedQuery<ProductMasterPojo> query = getQuery(select_all, ProductMasterPojo.class);
		return query.getResultList();
	}

	public void update(ProductMasterPojo p) {
	}

}