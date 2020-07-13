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
	private UserService service;

	// test user service
	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		UserPojo u = getUserPojo();
		// Add one time
		service.add(u);
		// Throw exception while entering second time
		service.add(u);
	}

	@Test
	public void testDelete() throws ApiException {
		UserPojo u = getUserPojo();
		service.add(u);
		// Delete should be successful and should not throw exception as data exists
		service.delete(u.getId());
	}

	@Test
	public void testGet() throws ApiException {
		UserPojo u = getUserPojo();
		service.add(u);
		UserPojo p = service.getByEmail(u.getEmail());
		// test added data
		assertEquals("admin", p.getRole());
		assertEquals("admin", p.getPassword());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		UserPojo u = getUserPojo();
		service.add(u);
		UserPojo p = service.get(u.getId());
		// update data
		p.setPassword("password");
		p.setRole("standard");
		service.update(p.getId(), p);
		UserPojo up = service.get(p.getId());
		// test updated data
		assertEquals("password", up.getPassword());
		assertEquals("standard", up.getRole());
	}

//	@Test
//	public void testNormalize() {
//		UserPojo u = getUserPojo();
//		UserService.normalize(u);
//		// test normalization
//		assertEquals("shahviram308@gmail.com", u.getEmail());
//		assertEquals("admin", u.getRole());
//	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		UserPojo u = getUserPojo();
		service.add(u);
		UserPojo userPojo = service.getCheck(u.getId());
		service.delete(userPojo.getId());
		service.getCheck(userPojo.getId());
	}

	@Test
	public void testSearchUserData() {
		UserPojo u = getUserPojo();
		service.add(u);
		UserForm userForm = getUserForm("j  ", "", "");
		List<UserPojo> userPojos = service.searchUserData(userForm);
		assertEquals(0, userPojos.size());
		userForm = getUserForm("s", "", "");
		userPojos = service.searchUserData(userForm);
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
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		return u;
	}

}
