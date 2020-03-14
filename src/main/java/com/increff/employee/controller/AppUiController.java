package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	// Controller which returns model and view of given name of html file

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/admin/data")
	public ModelAndView data() {
		return mav("data.html");
	}

	@RequestMapping(value = "/ui/admin/reports")
	public ModelAndView report() {
		return mav("reports.html");
	}

	@RequestMapping(value = "/ui/admin/user")
	public ModelAndView admin() {
		return mav("user.html");
	}

}
