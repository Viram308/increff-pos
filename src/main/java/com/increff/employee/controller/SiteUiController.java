package com.increff.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.InfoData;

@Controller
@RequestMapping(value = "/site")
public class SiteUiController extends AbstractUiController {

	// WEBSITE PAGES
	@Autowired
	private InfoData info;

	@RequestMapping(value = "/init")
	public ModelAndView init() {
		info.setMessage("");
		return mav("init.html");
	}

	@RequestMapping(value = "/login")
	public ModelAndView login() {
		return mav("login.html");
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout() {
		return mav("logout.html");
	}

}
