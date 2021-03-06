package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.AboutAppDto;
import com.increff.pos.model.AboutAppData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/about")
public class AboutAppController {

	@Autowired
	private AboutAppDto aboutAppDto;

	// Gives application name and version
	@ApiOperation(value = "Gives application name and version")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public AboutAppData getDetails() {
		return aboutAppDto.getNameandVersion();
	}

}
