package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.UserPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class UserServiceTest extends AbstractUnitTest {
	@Autowired
	private UserService service;

	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		service.add(u);
		service.add(u);
	}

	@Test
	public void testDelete() throws ApiException {
		UserPojo u = new UserPojo();
		u.setEmail(" Shahviram308@gmail.coM ");
		u.setPassword("admin");
		u.setRole(" AdmiN ");
		service.add(u);
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
		p.setPassword("password");
		p.setRole("standard");
		service.update(p.getId(), p);
		UserPojo up = service.get(p.getId());
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
		assertEquals("admin", u.getRole());
		assertEquals("admin", u.getPassword());
	}

}
