package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryApiController {

	@Autowired
	private InventoryService service;

	@Autowired
	private InventoryDto inventoryDto;

	// CRUD operations for inventory

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		InventoryPojo i = inventoryDto.convertInventoryFormtoInventoryPojo(form);
		service.add(i);
	}

	@ApiOperation(value = "Deletes Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo inventoryPojo = service.get(id);
		return inventoryDto.convertInventoryPojotoInventoryData(inventoryPojo);
	}

	@ApiOperation(value = "Gets list of all Inventory")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = service.getAll();
		return inventoryDto.getInventoryDataList(list);
	}

	@ApiOperation(value = "Updates an Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm form) throws ApiException {
		InventoryPojo p = inventoryDto.convertInventoryFormtoInventoryPojo(form);
		service.update(id, p);
	}

}
