package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;

@Component
public class UserDto {

	@Autowired
	private ModelMapper modelMapper;

	// Converts UserForm to UserPojo
	public UserPojo convertUserFormtoUserPojo(UserForm form) throws ApiException {
		UserPojo userPojo = modelMapper.map(form, UserPojo.class);
		checkData(userPojo);
		return userPojo;
	}

	// Converts UserPojo to UserData
	public UserData convertUserPojotoUserData(UserPojo userPojo) {
		return modelMapper.map(userPojo, UserData.class);
	}

	// Converts list of UserPojo to list of UserData
	public List<UserData> getUserDataList(List<UserPojo> list) {
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo userPojo : list) {
			UserData userData = modelMapper.map(userPojo, UserData.class);
			list2.add(userData);
		}
		return list2;
	}

	public void checkData(UserPojo u) throws ApiException {
		if (u.getEmail().isBlank() || u.getPassword().isBlank() || u.getRole().isBlank()) {
			throw new ApiException("Please enter email, password and role !!");
		}
	}
}
