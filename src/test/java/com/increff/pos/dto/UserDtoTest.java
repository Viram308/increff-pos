package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class UserDtoTest extends AbstractUnitTest {

	@Autowired
	private UserDto userDto;
	@Autowired
	private InfoData info;

	@Test
	public void testAddUser() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		// get user form
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// add user
		userDto.addUser(userForm);
		// get by email
		UserPojo userMasterPojo = userDto.getByEmail(userForm.getEmail());
		assertEquals("shahviram308@gmail.com", userMasterPojo.getEmail());
		// hash password
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update("password".getBytes("UTF-8"));
		String password = String.format("%032x", new BigInteger(1, md.digest()));
		// test hashed password
		assertEquals(password, userMasterPojo.getPassword());
		assertEquals("admin", userMasterPojo.getRole());
	}

	@Test
	public void testGetByEmail() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// add user
		userDto.addUser(userForm);
		// get by email
		UserPojo userMasterPojo = userDto.getByEmail(userForm.getEmail());
		// test data
		assertEquals("shahviram308@gmail.com", userMasterPojo.getEmail());
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update("password".getBytes("UTF-8"));
		String password = String.format("%032x", new BigInteger(1, md.digest()));
		assertEquals(password, userMasterPojo.getPassword());
		assertEquals("admin", userMasterPojo.getRole());
	}

	@Test
	public void testDeleteUser() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// add user
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.getEmail());
		// delete user
		userDto.deleteUser(userMasterPojo.getId());
		// test user deleted
		List<UserData> userDatas = userDto.getAllUsers();
		assertEquals(0, userDatas.size());
	}

	@Test
	public void testGetUser() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// add user
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.getEmail());
		// get user data
		UserData userData = userDto.getUserData(userMasterPojo.getId());
		// test
		assertEquals("shahviram308@gmail.com", userData.getEmail());
		assertEquals("admin", userData.getRole());
	}

	@Test
	public void testUpdateUser() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// add user
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.getEmail());
		UserForm userFormUpdate = getUserForm("shahviram308@gmail.com", "password", "Standard");
		// update user
		userDto.updateUser(userMasterPojo.getId(), userFormUpdate);
		UserPojo userMasterPojoUpdate = userDto.getByEmail(userForm.getEmail());
		// test
		assertEquals("standard", userMasterPojoUpdate.getRole());
	}

	@Test
	public void testGetAll() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// add user
		userDto.addUser(userForm);
		// test get all
		List<UserData> userMasterPojos = userDto.getAllUsers();
		assertEquals(1, userMasterPojos.size());
	}

	@Test
	public void testSearchUser() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		// add 2 users
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		userForm = getUserForm("  sHAHVIRAM123@gMail.coM  ", "password", "Standard");
		userDto.addUser(userForm);
		userForm = getUserForm("", "", "");
		// search user
		List<UserData> userDatas = userDto.searchData(userForm);
		// test
		assertEquals(2, userDatas.size());
		userForm = getUserForm("", "", "admin");
		userDatas = userDto.searchData(userForm);
		assertEquals(1, userDatas.size());
	}

	@Test
	public void testCheckInit() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm userForm1 = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		// check first time
		userDto.checkInit(userForm1);
		assertEquals("Application initialized", info.getMessage());
		// check second time
		userDto.checkInit(userForm1);
		assertEquals("Application already initialized. Please use existing credentials", info.getMessage());
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.validateData(userForm);
		// throw exception
		userForm = getUserForm("    ", "", "");
		userDto.validateData(userForm);
	}

	@Test
	public void testConvertUserPojotoAuthentication() {
		UserPojo p = getUserPojo("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		int id = 1;
		p.setId(id);
		Authentication token = userDto.convertUserPojotoAuthentication(p);
		String role = "";
		for (Iterator<? extends GrantedAuthority> i = token.getAuthorities().iterator(); i.hasNext();)
			role = i.next().toString();
		// Role here can be admin or standard as set by created data
		assertEquals(p.getRole(), role);
	}

	// functions for creeating data

	private UserForm getUserForm(String email, String password, String role) {
		UserForm userForm = new UserForm();
		userForm.setEmail(email);
		userForm.setPassword(password);
		userForm.setRole(role);
		return userForm;
	}

	private UserPojo getUserPojo(String email, String password, String role) {
		UserPojo u = new UserPojo();
		u.setEmail(email);
		u.setPassword(password);
		u.setRole(role);
		return u;
	}

}