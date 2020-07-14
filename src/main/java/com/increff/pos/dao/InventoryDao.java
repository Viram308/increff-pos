package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {

	// select according to product id
	private static String selectByProductId = "select i from InventoryPojo i where productId=:productId";
	// select all
	private static String selectAll = "select i from InventoryPojo i";

	// select according to product id
	public InventoryPojo selectByProductId(int pid) {
		TypedQuery<InventoryPojo> query = getQuery(selectByProductId, InventoryPojo.class);
		query.setParameter("productId", pid);
		return getSingle(query);
	}

	// select all
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(selectAll, InventoryPojo.class);
		return query.getResultList();
	}

}
