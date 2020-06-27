package com.increff.pos.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao {

	// delete according to id
	private static String delete_id = "delete from UserPojo p where id=:id";
	// select according to id
	private static String select_id = "select p from UserPojo p where id=:id";
	// select according to email
	private static String select_email = "select p from UserPojo p where email=:email";
	// select all
	private static String select_all = "select p from UserPojo p";

	@Transactional
	public void insert(UserPojo p) {
		em().persist(p);
	}

	// delete according to id
	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// select according to id
	public UserPojo select(int id) {
		TypedQuery<UserPojo> query = getQuery(select_id, UserPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// select according to email
	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(select_email, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	// select all
	public List<UserPojo> selectAll() {
		TypedQuery<UserPojo> query = getQuery(select_all, UserPojo.class);
		return query.getResultList();
	}

	public void update(UserPojo p) {
	}

}
