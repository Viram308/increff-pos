package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
		UserPojo p = service.get(u.getEmail());
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

	@Test
	public void testNormalize() {
		UserPojo u = getUserPojo();
		UserService.normalize(u);
		// test normalization
		assertEquals("admin", u.getRole());
		assertEquals("admin", u.getPassword());
	}

//	@Test(expected = ApiException.class)
//	public void testCheckData() throws ApiException {
//		UserPojo u = getUserPojo();
//		service.checkData(u);
//		// throw exception
//		u.setEmail("");
//		service.checkData(u);
//	}

	@Test
	public void testCheckAvailbility() throws ApiException {
		List<UserPojo> list = new ArrayList<UserPojo>();
		UserPojo u = getUserPojo();
		UserForm f = getUserForm();
		// Not initialized as list has 0 users
		service.checkAvailability(list, f);
		// add user
		service.add(u);
		// update list
		list = service.getAll();
		// Initialized
		service.checkAvailability(list, f);
	}

	private UserForm getUserForm() {
		UserForm f = new UserForm();
		f.setEmail("shahviram123@gmail.com");
		f.setPassword("admin");
		f.setRole("admin");
		return f;
	}

	private UserPojo getUserPojo() {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		return u;
	}

}
