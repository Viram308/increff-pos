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

	private static String delete_id = "delete from OrderItemPojo i where id=:id";
	private static String select_id = "select i from OrderItemPojo i where id=:id";
	private static String select_all = "select i from OrderItemPojo i";
	private static String select_existing_orderitem = "select i from OrderItemPojo i where orderId=:orderId and productId=:productId";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderItemPojo o) {
		em.persist(o);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public OrderItemPojo select(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
		return query.getResultList();
	}

	public void update(OrderItemPojo p) {
	}

	public OrderItemPojo getExistingOrderItem(int orderId, int productId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_existing_orderitem, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		query.setParameter("productId", productId);
		return getSingle(query);
	}

}
