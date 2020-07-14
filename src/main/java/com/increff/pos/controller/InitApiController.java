package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.UserForm;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/site/init")
public class InitApiController {

	@Autowired
	private UserDto userDto;

	@ApiOperation(value = "Initializes application")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView initSite(UserForm form) throws Exception {
		userDto.checkInit(form);
		return new ModelAndView("redirect:/site/login");
	}

}
