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
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryApiController {

	@Autowired
	private InventoryDto inventoryDto;

	// CRUD operations for inventory

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		inventoryDto.addInventory(form);
	}

	@ApiOperation(value = "Search a Product")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public List<InventoryData> search(@RequestBody InventorySearchForm form) throws ApiException {
		return inventoryDto.searchInventory(form);
	}

	@ApiOperation(value = "Gets an Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		return inventoryDto.getInventoryData(id);
	}

	@ApiOperation(value = "Gets list of all Inventory")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		return inventoryDto.getAllInventory();
	}

	@ApiOperation(value = "Updates an Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm form) throws ApiException {
		inventoryDto.updateInventory(id, form);
	}

}
