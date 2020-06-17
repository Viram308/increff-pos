package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.InfoData;
import com.increff.employee.model.UserForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;
import com.increff.employee.util.ConverterUtil;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/site/init")
public class InitApiController extends AbstractUiController {

	@Autowired
	private UserService service;
	@Autowired
	private InfoData info;

	@ApiOperation(value = "Initializes application")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView showPage(UserForm form) throws ApiException {
		info.setMessage("");
		return mav("init.html");
	}

	@ApiOperation(value = "Initializes application")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView initSite(UserForm form) throws ApiException {
		List<UserPojo> list = service.getAll();
		if (list.size() > 0) {
			info.setMessage("Application already initialized. Please use existing credentials");
		} else {
			form.setRole("admin");
			UserPojo p = ConverterUtil.convertUserFormtoUserPojo(form);
			service.add(p);
			info.setMessage("Application initialized");
		}
		return mav("login.html");

	}

}
