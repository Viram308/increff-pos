package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

	private static String select_all = "select i from OrderItemPojo i";
	// select according to order id
	private static String select_orderId = "select i from OrderItemPojo i where orderId IN :orderIds";

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

}
