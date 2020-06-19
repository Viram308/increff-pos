package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.UserForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/site/init")
public class InitApiController {

	@Autowired
	private UserService service;

	@ApiOperation(value = "Initializes application")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView initSite(UserForm form) throws ApiException {
		List<UserPojo> list = service.getAll();
		// check if already initialized
		service.checkAvailability(list, form);
		return new ModelAndView("redirect:/site/login");
	}

}
