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

	// delete according to id
	private static String delete_id = "delete from ProductMasterPojo p where id=:id";
	// select according to barcode
	private static String select_barcode = "select p from ProductMasterPojo p where barcode=:barcode";
	// select according to id
	private static String select_id = "select p from ProductMasterPojo p where id=:id";
	// select all
	private static String select_all = "select p from ProductMasterPojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(ProductMasterPojo p) {
		em.persist(p);
	}

	// delete according to id
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// select according to barcode
	public ProductMasterPojo selectByBarcode(String barcode) {
		TypedQuery<ProductMasterPojo> query = getQuery(select_barcode, ProductMasterPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	// select according to id
	public ProductMasterPojo select(int id) {
		TypedQuery<ProductMasterPojo> query = getQuery(select_id, ProductMasterPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// select all
	public List<ProductMasterPojo> selectAll() {
		TypedQuery<ProductMasterPojo> query = getQuery(select_all, ProductMasterPojo.class);
		return query.getResultList();
	}

	public void update(ProductMasterPojo p) {
	}

}
