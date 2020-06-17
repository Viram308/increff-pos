package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ui/admin")
public class AppAdminUiController extends AbstractUiController {

	// Controller which returns model and view of given name of html file

	@RequestMapping(value = "/data")
	public ModelAndView data() {
		return mav("data.html");
	}

	@RequestMapping(value = "/reports")
	public ModelAndView report() {
		return mav("reports.html");
	}

	@RequestMapping(value = "/user")
	public ModelAndView admin() {
		return mav("user.html");
	}

}
