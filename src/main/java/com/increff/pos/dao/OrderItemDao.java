package com.increff.pos.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

	private static String selectAll = "select i from OrderItemPojo i";
	// select according to order id list
	private static String selectByOrderIdList = "select i from OrderItemPojo i where orderId IN :orderIds";
	// select according to order id
	private static String selectByOrderId = "select i from OrderItemPojo i where orderId=:orderId";
	// delete according to orderId
	private static String deleteByOrderId = "delete from OrderItemPojo i where orderId=:orderId";
	// search
	private static String search = "select i from OrderItemPojo i where orderId=:orderId and productid in :productIds";
	// search
	private static String searchByProductId = "select i from OrderItemPojo i where productid in :productIds";

	// select according to order id list
	public List<OrderItemPojo> selectList(List<Integer> orderIds) {
		TypedQuery<OrderItemPojo> query = getQuery(selectByOrderIdList, OrderItemPojo.class);
		query.setParameter("orderIds", orderIds);
		return query.getResultList();
	}

	// select all
	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(selectAll, OrderItemPojo.class);
		return query.getResultList();
	}

	public List<OrderItemPojo> selectByOrderId(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(selectByOrderId, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}

	public int deleteByOrderId(int orderId) {
		Query query = em().createQuery(deleteByOrderId);
		query.setParameter("orderId", orderId);
		return query.executeUpdate();
	}

	public List<OrderItemPojo> searchData(int orderId, List<Integer> productIds) {
		TypedQuery<OrderItemPojo> query = getQuery(search, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		query.setParameter("productIds", productIds);
		return query.getResultList();
	}

	public List<OrderItemPojo> searchData(List<Integer> productIds) {
		TypedQuery<OrderItemPojo> query = getQuery(searchByProductId, OrderItemPojo.class);
		query.setParameter("productIds", productIds);
		return query.getResultList();
	}

}
