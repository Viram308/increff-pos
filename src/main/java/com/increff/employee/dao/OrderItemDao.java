package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

	// delete according to id
	private static String delete_id = "delete from OrderItemPojo i where id=:id";
	// select according to id
	private static String select_id = "select i from OrderItemPojo i where id=:id";
	// select all
	private static String select_all = "select i from OrderItemPojo i";
	// select according to order id
	private static String select_orderId = "select i from OrderItemPojo i where orderId IN :orderIds";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderItemPojo o) {
		em.persist(o);
	}

	// delete according to id
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// select according to id
	public OrderItemPojo select(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// select according to order id
	public List<OrderItemPojo> selectList(List<Integer> orderIds) {
		TypedQuery<OrderItemPojo> query = getQuery(select_orderId, OrderItemPojo.class);
		query.setParameter("orderIds", orderIds);
		return query.getResultList();
	}

	// select all
	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
		return query.getResultList();
	}

	public void update(OrderItemPojo p) {
	}

}
