package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao {

	// select according to email
	private static String select_email = "select p from UserPojo p where email=:email";
	// select all
	private static String select_all = "select p from UserPojo p";
	// search
	private static String search = "select p from UserPojo p where email like :email";

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

	public List<UserPojo> searchData(String email) {
		TypedQuery<UserPojo> query = getQuery(search, UserPojo.class);
		query.setParameter("email", email + "%");
		return query.getResultList();
	}

}
