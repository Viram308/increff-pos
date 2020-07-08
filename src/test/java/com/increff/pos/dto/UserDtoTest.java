package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

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
	public void testAddUser() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.email);
		assertEquals("shahviram308@gmail.com", userMasterPojo.getEmail());
		assertEquals("password", userMasterPojo.getPassword());
		assertEquals("admin", userMasterPojo.getRole());
	}

	@Test
	public void testGetByEmail() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.email);
		assertEquals("shahviram308@gmail.com", userMasterPojo.getEmail());
		assertEquals("password", userMasterPojo.getPassword());
		assertEquals("admin", userMasterPojo.getRole());
	}

	@Test(expected = ApiException.class)
	public void testDeleteUser() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.email);
		userDto.deleteUser(userMasterPojo.getId());
		// throws exception
		userDto.getByEmail(userForm.email);
	}

	@Test
	public void testGetUser() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.email);
		UserData userData = userDto.getUserData(userMasterPojo.getId());
		assertEquals("shahviram308@gmail.com", userData.email);
		assertEquals("password", userData.password);
		assertEquals("admin", userData.role);
	}

	@Test
	public void testUpdateUser() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		UserPojo userMasterPojo = userDto.getByEmail(userForm.email);
		UserForm userFormUpdate = getUserForm("shahviram308@gmail.com", "password", "Standard");
		userDto.updateUser(userMasterPojo.getId(), userFormUpdate);
		UserPojo userMasterPojoUpdate = userDto.getByEmail(userForm.email);
		assertEquals("standard", userMasterPojoUpdate.getRole());
	}

	@Test
	public void testGetAll() throws ApiException {
		UserForm userForm = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.addUser(userForm);
		List<UserData> userMasterPojos = userDto.getAllUsers();
		assertEquals(1, userMasterPojos.size());
	}

	@Test
	public void testCheckInit() throws ApiException {
		UserForm userForm1 = getUserForm("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.checkInit(userForm1);
		assertEquals("Application initialized", info.message);
		userDto.checkInit(userForm1);
		assertEquals("Application already initialized. Please use existing credentials", info.message);
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		UserPojo u = getUserPojo("  sHAHVIRAM308@gMail.coM  ", "password", "Admin");
		userDto.checkData(u);
		// throw exception
		u = getUserPojo("    ", "", "");
		userDto.checkData(u);
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

	private UserForm getUserForm(String email, String password, String role) {
		UserForm userForm = new UserForm();
		userForm.email = email;
		userForm.password = password;
		userForm.role = role;
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
