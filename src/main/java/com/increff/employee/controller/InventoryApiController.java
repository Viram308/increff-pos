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

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		int product_id = pService.getId(form.getBarcode());
		InventoryPojo i = convert(form, product_id);
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
		ProductMasterPojo p = pService.get(i.getProductId());
		return convert(i, p.getBarcode());
	}

	@ApiOperation(value = "Gets list of all Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = iService.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo i : list) {
			ProductMasterPojo p = pService.get(i.getProductId());
			list2.add(convert(i, p.getBarcode()));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an Inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		int product_id = pService.getId(f.getBarcode());
		InventoryPojo p = convert(f, product_id);
		iService.update(id, p);
	}

	private static InventoryData convert(InventoryPojo i, String barcode) {
		InventoryData d = new InventoryData();
		d.setId(i.getId());
		d.setBarcode(barcode);
		d.setQuantity(i.getQuantity());
		return d;
	}

	private static InventoryPojo convert(InventoryForm f, int product_id) {
		InventoryPojo p = new InventoryPojo();
		p.setProductId(product_id);
		p.setQuantity(f.getQuantity());
		return p;
	}

}
