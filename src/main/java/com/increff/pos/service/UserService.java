package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ConverterUtil;

@Service
public class UserService {

	@Autowired
	private UserDao dao;
	@Autowired
	private InfoData info;
	@Autowired
	private UserService service;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		// check input data
		checkData(p);
		// normalize
		normalize(p);
		// check existing user with given email
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public UserPojo get(int id) {
		return dao.select(id);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional
	public void update(int id, UserPojo p) throws ApiException {
		checkData(p);
		normalize(p);
		UserPojo u = get(id);
		u.setRole(p.getRole());
		dao.update(u);
	}

	public void checkData(UserPojo u) throws ApiException {
		if (u.getEmail().isBlank() || u.getPassword().isBlank() || u.getRole().isBlank()) {
			throw new ApiException("Please enter email, password and role !!");
		}
	}

	public static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}

	public void checkAvailability(List<UserPojo> list, UserForm form) throws ApiException {
		// check if already exists
		if (list.size() > 0) {
			info.setMessage("Application already initialized. Please use existing credentials");
		} else {
			// Initialize with admin role
			form.setRole("admin");
			UserPojo p = ConverterUtil.convertUserFormtoUserPojo(form);
			service.add(p);
			info.setMessage("Application initialized");
		}
	}

}
