package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ConverterUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/product")
public class ProductApiController {

	@Autowired
	private ProductService pService;

	@Autowired
	private BrandService bService;

	// CRUD operations for product

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		int brand_category_id = bService.getId(form.getBrand(), form.getCategory());
		ProductMasterPojo p = ConverterUtil.convertProductFormtoProductMasterPojo(form, brand_category_id,
				bService.get(brand_category_id));
		pService.add(p);
	}

	@ApiOperation(value = "Deletes a Product")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		pService.delete(id);
	}

	@ApiOperation(value = "Gets a Product")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductMasterPojo p = pService.get(id);
		return ConverterUtil.convertProductMasterPojotoProductData(p, p.getBrand_category().getBrand(),
				p.getBrand_category().getCategory());
	}

	@ApiOperation(value = "Gets a Product")
	@RequestMapping(value = "/b/{barcode}", method = RequestMethod.GET)
	public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
		ProductMasterPojo p = pService.getByBarcode(barcode);
		return ConverterUtil.convertProductMasterPojotoProductData(p, p.getBrand_category().getBrand(),
				p.getBrand_category().getCategory());
	}

	@ApiOperation(value = "Gets list of all Products")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductMasterPojo> list = pService.getAll();
		return ConverterUtil.getProductDataList(list);
	}

	@ApiOperation(value = "Updates a Product")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		int brand_category_id = bService.getId(f.getBrand(), f.getCategory());
		ProductMasterPojo p = ConverterUtil.convertProductFormtoProductMasterPojoUpdate(f, brand_category_id,
				bService.get(brand_category_id));
		pService.update(id, p);

	}

}
