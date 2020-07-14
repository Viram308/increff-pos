package com.increff.pos.dto;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

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
import com.increff.pos.util.PasswordUtil;
import com.increff.pos.util.StringUtil;

@Component
public class UserDto {

	@Autowired
	private UserService userService;
	@Autowired
	private InfoData info;

	public UserPojo addUser(UserForm form) throws Exception {
		validateData(form);
		UserPojo userPojo = ConverterUtil.convertUserFormtoUserPojo(form);
		userPojo.setPassword(PasswordUtil.getHash(form.getPassword()));
		return userService.add(userPojo);
	}

	public void deleteUser(int id) {
		userService.delete(id);
	}

	public UserData getUserData(int id) {
		UserPojo userPojo = userService.get(id);
		return ConverterUtil.convertUserPojotoUserData(userPojo);
	}

	public List<UserData> searchData(UserForm form) {
		List<UserPojo> list = userService.searchUserData(form);
		if (form.getRole().isEmpty()) {
			// map UserPojo to UserData
			return list.stream().map(o -> ConverterUtil.convertUserPojotoUserData(o)).collect(Collectors.toList());
		}
		// filter with role
		list = list.stream().filter(o -> (StringUtil.toLowerCase(form.getRole()).equals(o.getRole())))
				.collect(Collectors.toList());
		// map UserPojo to UserData
		return list.stream().map(o -> ConverterUtil.convertUserPojotoUserData(o)).collect(Collectors.toList());
	}

	public UserPojo getByEmail(String email) throws ApiException {
		return userService.getByEmail(email);
	}

	public UserPojo updateUser(int id, UserForm form)
			throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		validateData(form);
		UserPojo userPojo = ConverterUtil.convertUserFormtoUserPojo(form);
		return userService.update(id, userPojo);
	}

	public List<UserData> getAllUsers() {
		List<UserPojo> list = userService.getAll();
		// map UserPojo to UserData
		return list.stream().map(o -> ConverterUtil.convertUserPojotoUserData(o)).collect(Collectors.toList());
	}

	public void checkInit(UserForm form) throws Exception {
		List<UserPojo> list = userService.getAll();
		// check if already initialized
		// check if already exists
		if (list.size() > 0) {
			info.setMessage("Application already initialized. Please use existing credentials");
		} else {
			// Initialize with admin role
			form.setRole("admin");
			UserPojo p = ConverterUtil.convertUserFormtoUserPojo(form);
			p.setPassword(PasswordUtil.getHash(form.getPassword()));
			userService.add(p);
			info.setMessage("Application initialized");
		}
	}

	public void validateData(UserForm u) throws ApiException {
		if (u.getEmail().isBlank() || u.getRole().isBlank()) {
			throw new ApiException("Please enter email, password and role !!");
		}
	}

	public Authentication convertUserPojotoAuthentication(UserPojo userPojo) {
		return ConverterUtil.convertUserPojotoAuthentication(userPojo);
	}

}
