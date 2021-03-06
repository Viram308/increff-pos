package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.UserDao;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.StringUtil;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional(rollbackFor = ApiException.class)
	public UserPojo add(UserPojo p) throws ApiException {
		// normalize
		NormalizeUtil.normalizeUserPojo(p);
		// check existing user with given email
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(p);
		return p;
	}

	@Transactional(readOnly = true)
	public UserPojo getByEmail(String email){
		email = StringUtil.toLowerCase(email);
		return dao.select(email);
	}

	@Transactional(readOnly = true)
	public UserPojo get(int id) {
		return dao.select(UserPojo.class, id);
	}

	@Transactional(readOnly = true)
	public List<UserPojo> searchUserData(UserForm form) {
		NormalizeUtil.normalizeUserForm(form);
		return dao.searchData(form.getEmail());
	}
	
	@Transactional(readOnly = true)
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(UserPojo.class, id);
	}

	@Transactional(rollbackFor = ApiException.class)
	public UserPojo update(int id, UserPojo p) throws ApiException {
		NormalizeUtil.normalizeUserPojo(p);
		UserPojo u = getCheck(id);
		u.setRole(p.getRole());
		dao.update(u);
		return u;
	}

	@Transactional(readOnly = true)
	public UserPojo getCheck(int id) throws ApiException {
		UserPojo userPojo = dao.select(UserPojo.class, id);
		if (userPojo == null) {
			throw new ApiException("User doesn't exist for id : " + id);
		}
		return userPojo;
	}
	
}
