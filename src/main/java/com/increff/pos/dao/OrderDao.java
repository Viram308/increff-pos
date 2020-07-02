package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {

	// select all
	private static String select_all = "select o from OrderPojo o";

	// select all
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}

}
