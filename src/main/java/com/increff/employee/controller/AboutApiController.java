package com.increff.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.AboutAppData;
import com.increff.employee.service.AboutAppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/about")
public class AboutApiController {

	@Autowired
	private AboutAppService service;

	// Gives application name and version
	@ApiOperation(value = "Gives application name and version")
	@RequestMapping(value = "",method = RequestMethod.GET)
	public AboutAppData getDetails() {
		return service.getNameandVersion();
	}

}
