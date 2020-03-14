package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.UserPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class UserServiceTest extends AbstractUnitTest {
	@Autowired
	private UserService service;

	// test user service
	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		// Add one time
		service.add(u);
		// Throw exception while entering second time
		service.add(u);
	}

	@Test
	public void testDelete() throws ApiException {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		service.add(u);
		// Delete should be successful and should not throw exception as data exists
		service.delete(u.getId());
	}

	@Test
	public void testGet() throws ApiException {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
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
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
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
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		UserService.normalize(u);
		// test normalization
		assertEquals("admin", u.getRole());
		assertEquals("admin", u.getPassword());
	}

}
