package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserPrincipal;
import com.increff.pos.util.SecurityUtil;

@Controller
public abstract class AbstractUiController {

	@Autowired
	private InfoData info;

	// Gets value of base Url from properties file
	@Value("${app.baseUrl}")
	private String baseUrl;

	protected ModelAndView mav(String page) {
		// Get current user
		UserPrincipal principal = SecurityUtil.getPrincipal();

		info.email = principal == null ? "" : principal.email;
		info.role = principal == null ? "" : principal.role;
		// Set info
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("info", info);
		mav.addObject("baseUrl", baseUrl);
		return mav;
	}

}
