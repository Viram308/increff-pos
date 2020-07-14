package com.increff.pos.dto;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
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
import com.increff.pos.util.StringUtil;

@Component
public class UserDto {

	@Autowired
	private UserService userService;
	@Autowired
	private InfoData info;

	public UserPojo addUser(UserForm form) throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		validateData(form);
		UserPojo userPojo = ConverterUtil.convertUserFormtoUserPojo(form);
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
	    md.update(form.getPassword().getBytes("UTF-8"));
	    userPojo.setPassword(String.format("%032x", new BigInteger(1, md.digest())));
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
			return list.stream().map(o -> ConverterUtil.convertUserPojotoUserData(o)).collect(Collectors.toList());
		}
		list = list.stream().filter(o -> (StringUtil.toLowerCase(form.getRole()).equals(o.getRole())))
				.collect(Collectors.toList());
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
		return list.stream().map(o -> ConverterUtil.convertUserPojotoUserData(o)).collect(Collectors.toList());
	}

	public void checkInit(UserForm form) throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		List<UserPojo> list = userService.getAll();
		// check if already initialized
		// check if already exists
		if (list.size() > 0) {
			info.setMessage("Application already initialized. Please use existing credentials");
		} else {
			// Initialize with admin role
			form.setRole("admin");
			UserPojo p = ConverterUtil.convertUserFormtoUserPojo(form);
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
		    md.update(form.getPassword().getBytes("UTF-8"));
		    p.setPassword(String.format("%032x", new BigInteger(1, md.digest())));
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
