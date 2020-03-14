package com.increff.employee.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

	@PersistenceContext
	private EntityManager em;

	// Gives single result from database if exists or null
	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	// Creates query
	protected <T> TypedQuery<T> getQuery(String jpql, Class<T> c) {
		return em.createQuery(jpql, c);
	}

	// Returns entity manager instance
	protected EntityManager em() {
		return em;
	}

}
