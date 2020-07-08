package com.increff.pos.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConverterUtil;

@Component
public class UserDto {

	@Autowired
	private UserService userService;
	@Autowired
	private ConverterUtil converterUtil;
	@Autowired
	private InfoData info;

	public void addUser(UserForm form) throws ApiException {
		UserPojo userPojo = converterUtil.convertUserFormtoUserPojo(form);
		checkData(userPojo);
		userService.add(userPojo);
	}

	public void deleteUser(int id) {
		userService.delete(id);
	}

	public UserData getUserData(int id) {
		UserPojo userPojo = userService.get(id);
		return converterUtil.convertUserPojotoUserData(userPojo);
	}

	public UserPojo getByEmail(String email) throws ApiException {
		return userService.getByEmail(email);
	}

	public void updateUser(int id, UserForm form) throws ApiException {
		UserPojo userPojo = converterUtil.convertUserFormtoUserPojo(form);
		checkData(userPojo);
		userService.update(id, userPojo);
	}

	public List<UserData> getAllUsers() {
		List<UserPojo> list = userService.getAll();
		return converterUtil.getUserDataList(list);
	}

	public void checkInit(UserForm form) throws ApiException {
		List<UserPojo> list = userService.getAll();
		// check if already initialized
		// check if already exists
		if (list.size() > 0) {
			info.message = "Application already initialized. Please use existing credentials";
		} else {
			// Initialize with admin role
			form.role = "admin";
			UserPojo p = converterUtil.convertUserFormtoUserPojo(form);
			userService.add(p);
			info.message = "Application initialized";
		}
	}

	public void checkData(UserPojo u) throws ApiException {
		if (u.getEmail().isBlank() || u.getPassword().isBlank() || u.getRole().isBlank()) {
			throw new ApiException("Please enter email, password and role !!");
		}
	}

	public Authentication convertUserPojotoAuthentication(UserPojo userPojo) {
		return converterUtil.convertUserPojotoAuthentication(userPojo);
	}

}
