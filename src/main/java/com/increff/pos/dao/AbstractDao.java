package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

	@PersistenceContext
	private EntityManager em;

	public <T> void insert(T b) {
		em.persist(b);
	}

	public <T> void delete(Class<T> c, int id) {
		T p = em.find(c, id);
		em.remove(p);
	}

	public <T> T select(Class<T> c, int id) {
		return em.find(c, id);
	}

	public <T> void update(T b) {
		em.merge(b);
	}

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
