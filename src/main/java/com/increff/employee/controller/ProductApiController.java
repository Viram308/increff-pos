package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {

	@Autowired
	private ProductService pService;

	@Autowired
	private BrandService bService;

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		int brand_category_id = bService.getId(form.getBrand(), form.getCategory());
		ProductMasterPojo p = convert(form, brand_category_id);
		pService.add(p);

	}

	@ApiOperation(value = "Deletes a Product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		pService.delete(id);
	}

	@ApiOperation(value = "Gets a Product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductMasterPojo p = pService.get(id);
		BrandMasterPojo b = bService.get(p.getBrand_category());
		return convert(p, b.getBrand(), b.getCategory());
	}

	@ApiOperation(value = "Gets a Product")
	@RequestMapping(path = "/api/product/", method = RequestMethod.GET)
	public ProductData getByBarcode(@RequestParam(value="barcode") String barcode) throws ApiException {
		ProductMasterPojo p = pService.getByBarcode(barcode);
		BrandMasterPojo b = bService.get(p.getBrand_category());
		return convert(p, b.getBrand(), b.getCategory());
	}
	
	@ApiOperation(value = "Gets list of all Products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		List<ProductMasterPojo> list = pService.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductMasterPojo p : list) {
			BrandMasterPojo b = bService.get(p.getBrand_category());
			list2.add(convert(p, b.getBrand(), b.getCategory()));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a Product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		int brand_category_id = bService.getId(f.getBrand(), f.getCategory());
		ProductMasterPojo p = convertUpdate(f, brand_category_id);
		pService.update(id, p);

	}

	private ProductMasterPojo convertUpdate(ProductForm f, int brand_category_id) {
		ProductMasterPojo p = new ProductMasterPojo();
		p.setBrand_category(brand_category_id);
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

	private static ProductData convert(ProductMasterPojo p, String brand, String category) {
		ProductData d = new ProductData();
		d.setBrand(brand);
		d.setCategory(category);
		d.setId(p.getId());
		d.setName(p.getName());
		d.setMrp(p.getMrp());
		d.setBarcode(p.getBarcode());
		return d;
	}

	private static ProductMasterPojo convert(ProductForm f, int brand_category_id) {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		p.setBarcode(barcode);
		p.setBrand_category(brand_category_id);
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

}
