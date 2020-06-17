package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ui")
public class AppUiController extends AbstractUiController {

	// Controller which returns model and view of given name of html file

	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return mav("home.html");
	}
}
