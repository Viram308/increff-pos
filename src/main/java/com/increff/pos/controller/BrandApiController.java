package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/brand")
public class BrandApiController {

	@Autowired
	private BrandDto brandDto;
	// CRUD operations for brand

	@ApiOperation(value = "Adds a Brand")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		brandDto.addBrand(form);
	}

	@ApiOperation(value = "Search a Brand")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public List<BrandData> search(@RequestBody BrandForm form) throws ApiException {
		return brandDto.searchBrand(form);
	}
	
	
	@ApiOperation(value = "Gets a Brand")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		return brandDto.getBrandData(id);
	}

	@ApiOperation(value = "Gets list of all Brands")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return brandDto.getAllBrands();
	}

	@ApiOperation(value = "Updates a Brand")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		brandDto.updateBrand(id, form);
	}

}
