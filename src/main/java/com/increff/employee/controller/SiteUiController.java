package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/site")
public class SiteUiController extends AbstractUiController {

	// WEBSITE PAGES
	@RequestMapping(value = "/login")
	public ModelAndView login() {
		return mav("login.html");
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout() {
		return mav("logout.html");
	}

	@RequestMapping(value = "/pricing")
	public ModelAndView pricing() {
		return mav("pricing.html");
	}

	@RequestMapping(value = "/features")
	public ModelAndView features() {
		return mav("features.html");
	}

}
