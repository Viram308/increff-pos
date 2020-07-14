package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class UserServiceTest extends AbstractUnitTest {
	@Autowired
	private UserService userService;

	// test user service
	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		UserPojo userPojo = getUserPojo();
		// Add one time
		userService.add(userPojo);
		// Throw exception while entering second time
		userService.add(userPojo);
	}

	@Test
	public void testDelete() throws ApiException {
		UserPojo userPojo = getUserPojo();
		userService.add(userPojo);
		// Delete should be successful and should not throw exception as data exists
		userService.delete(userPojo.getId());
	}

	@Test
	public void testGet() throws ApiException {
		UserPojo userPojo = getUserPojo();
		userService.add(userPojo);
		UserPojo userPojoFinal = userService.getByEmail(userPojo.getEmail());
		// test added data
		assertEquals("admin", userPojoFinal.getRole());
		assertEquals("admin", userPojoFinal.getPassword());
	}

	@Test
	public void testGetAll() throws ApiException {
		UserPojo userPojo = getUserPojo();
		userService.add(userPojo);
		// test get all
		List<UserPojo> userPojos = userService.getAll();
		assertEquals(1, userPojos.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		UserPojo userPojo = getUserPojo();
		userService.add(userPojo);
		UserPojo userPojoFinal = userService.get(userPojo.getId());
		// update data
		userPojoFinal.setPassword("password");
		userPojoFinal.setRole("standard");
		userService.update(userPojoFinal.getId(), userPojoFinal);
		UserPojo userPojoUpdated = userService.get(userPojoFinal.getId());
		// test updated data
		assertEquals("password", userPojoUpdated.getPassword());
		assertEquals("standard", userPojoUpdated.getRole());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		UserPojo userPojo = getUserPojo();
		userService.add(userPojo);
		UserPojo userPojoFinal = userService.getCheck(userPojo.getId());
		userService.delete(userPojoFinal.getId());
		userService.getCheck(userPojoFinal.getId());
	}

	@Test
	public void testSearchUserData() {
		UserPojo userPojo = getUserPojo();
		userService.add(userPojo);
		// create user form
		UserForm userForm = getUserForm("j  ", "", "");
		// search
		List<UserPojo> userPojos = userService.searchUserData(userForm);
		assertEquals(0, userPojos.size());
		// create user form
		userForm = getUserForm("s", "", "");
		// search
		userPojos = userService.searchUserData(userForm);
		assertEquals(1, userPojos.size());
	}

	private UserForm getUserForm(String email, String password, String role) {
		UserForm userForm = new UserForm();
		userForm.setEmail(email);
		userForm.setPassword(password);
		userForm.setRole(role);
		return userForm;
	}

	private UserPojo getUserPojo() {
		UserPojo userPojo = new UserPojo();
		userPojo.setEmail(" Shahviram308@gmail.coM ");
		userPojo.setPassword("admin");
		userPojo.setRole(" AdmiN ");
		return userPojo;
	}

}
