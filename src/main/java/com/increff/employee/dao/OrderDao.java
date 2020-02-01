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

	private static String delete_id = "delete from OrderPojo o where id=:id";
	private static String select_id = "select o from OrderPojo o where id=:id";
	private static String select_all = "select o from OrderPojo o";
	private static String select_max = "select max(id) from OrderPojo o";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(OrderPojo b) {
		em.persist(b);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}

	public int selectMax() {
		TypedQuery<Integer> query = getQuery(select_max, Integer.class);
		return query.getResultList().stream().findFirst().orElse(-1);
	}

}
