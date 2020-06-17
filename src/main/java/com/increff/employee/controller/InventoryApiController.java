package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ConverterUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryApiController {

	@Autowired
	private InventoryService iService;

	@Autowired
	private ProductService pService;

	// CRUD operations for inventory

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		ProductMasterPojo pm = pService.getByBarcode(form.getBarcode());
		InventoryPojo i = ConverterUtil.convertInventoryFormtoInventoryPojo(form, pm);
		iService.add(i);
	}

	@ApiOperation(value = "Deletes Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		iService.delete(id);
	}

	@ApiOperation(value = "Gets an Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo i = iService.get(id);
		return ConverterUtil.convertInventoryPojotoInventoryData(i, i.getProductMasterPojo().getBarcode());
	}

	@ApiOperation(value = "Gets list of all Inventory")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = iService.getAll();
		return ConverterUtil.getInventoryDataList(list);
	}

	@ApiOperation(value = "Updates an Inventory")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		ProductMasterPojo pm = pService.getByBarcode(f.getBarcode());
		InventoryPojo p = ConverterUtil.convertInventoryFormtoInventoryPojo(f, pm);
		iService.update(id, p);
	}

}
