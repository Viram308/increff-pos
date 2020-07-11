package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/admin/user")
public class AdminApiController {

	@Autowired
	private UserDto userDto;
	// CRUD operations for user

	@ApiOperation(value = "Adds a user")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public UserPojo addUser(@RequestBody UserForm form) throws ApiException {
		return userDto.addUser(form);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		userDto.deleteUser(id);
	}

	@ApiOperation(value = "Gets a User")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public UserData get(@PathVariable int id) {
		return userDto.getUserData(id);
	}

	@ApiOperation(value = "Updates a user")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public UserPojo updateUser(@PathVariable int id, @RequestBody UserForm form) throws ApiException {
		return userDto.updateUser(id, form);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		return userDto.getAllUsers();
	}
}
