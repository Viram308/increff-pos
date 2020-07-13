package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {

	// select all
	private static String selectAll = "select o from OrderPojo o";
	// search
	private static String search = "select o from OrderPojo o where orderCreater like :orderCreater";

	// select all
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(selectAll, OrderPojo.class);
		return query.getResultList();
	}

	public List<OrderPojo> searchOrder(String orderCreater) {
		TypedQuery<OrderPojo> query = getQuery(search, OrderPojo.class);
		query.setParameter("orderCreater", orderCreater + "%");
		return query.getResultList();
	}

}
