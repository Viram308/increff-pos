package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {

	// delete according to id
	private static String delete_id = "delete from InventoryPojo i where id=:id";
	// select according to product id
	private static String select_productId = "select i from InventoryPojo i where productId=:productId";
	// select according to id
	private static String select_id = "select i from InventoryPojo i where id=:id";
	// select all
	private static String select_all = "select i from InventoryPojo i";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(InventoryPojo p) {
		em.persist(p);
	}

	// delete according to id
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// select according to product id
	public InventoryPojo selectByProductId(int pid) {
		TypedQuery<InventoryPojo> query = getQuery(select_productId, InventoryPojo.class);
		query.setParameter("productId", pid);
		return getSingle(query);
	}

	// select according to id
	public InventoryPojo select(int id) {
		TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// select all
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}

	public void update(InventoryPojo p) {
	}

}
