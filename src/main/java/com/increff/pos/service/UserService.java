package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.StringUtil;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		// normalize
		normalize(p);
		// check existing user with given email
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(p);
	}

	@Transactional
	public UserPojo getByEmail(String email) throws ApiException {
		email = StringUtil.toLowerCase(email);
		return getCheckForEmail(email);
	}

	@Transactional
	public UserPojo get(int id) {
		return dao.select(UserPojo.class, id);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(UserPojo.class, id);
	}

	@Transactional
	public void update(int id, UserPojo p) throws ApiException {
		normalize(p);
		UserPojo u = getCheck(id);
		u.setRole(p.getRole());
		dao.update(u);
	}

	@Transactional
	public UserPojo getCheck(int id) throws ApiException {
		UserPojo userPojo = dao.select(UserPojo.class, id);
		if (userPojo == null) {
			throw new ApiException("User does not exist for id : " + id);
		}
		return userPojo;
	}

	@Transactional
	public UserPojo getCheckForEmail(String email) throws ApiException {
		UserPojo userPojo = dao.select(email);
		if (userPojo == null) {
			throw new ApiException("User does not exist for email : " + email);
		}
		return userPojo;
	}

	public static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}

}
