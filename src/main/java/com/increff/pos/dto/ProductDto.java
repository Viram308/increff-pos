package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class ProductDto {

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;
	@Autowired
	private ConverterUtil converterUtil;

	public void add(ProductForm form) throws ApiException {
		BrandMasterPojo brandMasterPojo = brandService.getByBrandCategory(form.getBrand(), form.getCategory());
		checkData(form);
		ProductMasterPojo productMasterPojo = converterUtil.convertProductFormtoProductMasterPojo(form,
				brandMasterPojo);

		productService.add(productMasterPojo, brandMasterPojo);
	}

	public List<ProductData> searchProduct(ProductSearchForm form) throws ApiException {
		checkSearchData(form);
		BrandMasterPojo brandMasterPojo = new BrandMasterPojo();
		brandMasterPojo.setBrand(form.getBrand());
		brandMasterPojo.setCategory(form.getCategory());
		List<BrandMasterPojo> brandMasterPojoList = brandService.searchData(brandMasterPojo);
		List<Integer> brandIds = getBrandIdList(brandMasterPojoList);
		ProductMasterPojo productMasterPojo = converterUtil.convertProductSearchFormtoProductMasterPojo(form);
		List<ProductMasterPojo> list = productService.searchData(productMasterPojo, brandIds);
		return converterUtil.getProductDataList(list);
	}

	public ProductData get(int id) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.get(id);
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		return converterUtil.convertProductMasterPojotoProductData(productMasterPojo, brandMasterPojo);
	}

	public ProductData getByBarcode(String barcode) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.getByBarcode(barcode);
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		return converterUtil.convertProductMasterPojotoProductData(productMasterPojo, brandMasterPojo);
	}

	public List<ProductData> getAll() {
		List<ProductMasterPojo> list = productService.getAll();
		return converterUtil.getProductDataList(list);
	}

	public void update(int id, ProductForm form) throws ApiException {
		BrandMasterPojo brandMasterPojo = brandService.getByBrandCategory(form.getBrand(), form.getCategory());
		checkData(form);
		ProductMasterPojo productMasterPojo = converterUtil.convertProductFormtoProductMasterPojoUpdate(form,
				brandMasterPojo);
		productService.update(id, productMasterPojo, brandMasterPojo);
	}

	public List<Integer> getBrandIdList(List<BrandMasterPojo> brandMasterPojoList) {
		List<Integer> brandIdList = new ArrayList<Integer>();
		for (BrandMasterPojo brandMasterPojo : brandMasterPojoList) {
			brandIdList.add(brandMasterPojo.getId());
		}
		return brandIdList;
	}

	public void checkData(ProductForm b) throws ApiException {
		if (b.getName().isBlank() || b.getMrp() <= 0) {
			throw new ApiException("Please enter name, mrp(positive) !!");
		}
	}

	public void checkSearchData(ProductSearchForm productSearchForm) throws ApiException {
		if (productSearchForm.getBrand().isBlank() && productSearchForm.getCategory().isBlank()
				&& productSearchForm.getName().isBlank() && productSearchForm.getBarcode().isBlank()) {
			throw new ApiException("Please enter atleast one (brand,category,name,barcode) !!");
		}
	}

}
