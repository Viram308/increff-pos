package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/product")
public class ProductApiController {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductDto productDto;

	// CRUD operations for product

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductMasterPojo productMasterPojo = productDto.convertProductFormtoProductMasterPojo(form);
		service.add(productMasterPojo);
	}

	@ApiOperation(value = "Deletes a Product")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a Product")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductMasterPojo productMasterPojo = service.get(id);
		return productDto.convertProductMasterPojotoProductData(productMasterPojo);
	}

	@ApiOperation(value = "Gets a Product")
	@RequestMapping(value = "/b/{barcode}", method = RequestMethod.GET)
	public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
		ProductMasterPojo productMasterPojo = service.getByBarcode(barcode);
		return productDto.convertProductMasterPojotoProductData(productMasterPojo);
	}

	@ApiOperation(value = "Gets list of all Products")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductMasterPojo> list = service.getAll();
		return productDto.getProductDataList(list);
	}

	@ApiOperation(value = "Updates a Product")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
		ProductMasterPojo p = productDto.convertProductFormtoProductMasterPojoUpdate(form);
		service.update(id, p);
	}

}
