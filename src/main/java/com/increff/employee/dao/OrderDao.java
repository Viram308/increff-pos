package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {

	// delete according to id
	private static String delete_id = "delete from OrderPojo o where id=:id";
	// select according to id
	private static String select_id = "select o from OrderPojo o where id=:id";
	// select all
	private static String select_all = "select o from OrderPojo o";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderPojo b) {
		em.persist(b);
	}

	// delete according to id
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// select according to id
	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// select all
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}

}
