package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConverterUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/brand")
public class BrandApiController {

	@Autowired
	private BrandService service;

	// CRUD operations for brand

	@ApiOperation(value = "Adds a Brand")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		BrandMasterPojo p = ConverterUtil.convertBrandFormtoBrandMasterPojo(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes a Brand")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Brand")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		BrandMasterPojo p = service.get(id);
		return ConverterUtil.convertBrandMasterPojotoBrandData(p);
	}

	@ApiOperation(value = "Gets list of all Brands")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		List<BrandMasterPojo> list = service.getAll();
		return ConverterUtil.getBrandDataList(list);
	}

	@ApiOperation(value = "Updates a Brand")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
		BrandMasterPojo p = ConverterUtil.convertBrandFormtoBrandMasterPojo(f);
		service.update(id, p);
	}

}
