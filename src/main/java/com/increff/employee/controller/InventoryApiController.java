package com.increff.employee.controller;

import java.util.ArrayList;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {

	@Autowired
	private InventoryService iService;

	@Autowired
	private ProductService pService;

	// CRUD operations for inventory

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		ProductMasterPojo pm = pService.getByBarcode(form.getBarcode());
		InventoryPojo i = convert(form, pm);
		iService.add(i);

	}

	@ApiOperation(value = "Deletes Inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		iService.delete(id);
	}

	@ApiOperation(value = "Gets an Inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo i = iService.get(id);
		return convert(i, i.getProductMasterPojo().getBarcode());
	}

	@ApiOperation(value = "Gets list of all Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = iService.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo i : list) {
			list2.add(convert(i, i.getProductMasterPojo().getBarcode()));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an Inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		ProductMasterPojo pm = pService.getByBarcode(f.getBarcode());
		InventoryPojo p = convert(f, pm);
		iService.update(id, p);
	}

	// Converts InventoryPojo to InventoryData
	private InventoryData convert(InventoryPojo i, String barcode) {
		InventoryData d = new InventoryData();
		d.setId(i.getId());
		d.setBarcode(barcode);
		d.setQuantity(i.getQuantity());
		return d;
	}

	// Converts InventoryForm to InventoryPojo
	private InventoryPojo convert(InventoryForm f, ProductMasterPojo p) throws ApiException {
		InventoryPojo i = new InventoryPojo();
		i.setProductMasterPojo(p);
		i.setQuantity(f.getQuantity());
		return i;
	}

}
