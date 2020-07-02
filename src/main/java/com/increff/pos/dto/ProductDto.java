package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.StringUtil;

@Component
public class ProductDto {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BrandService bService;

	// Converts ProductForm to ProductMasterPojo during update operation
	public ProductMasterPojo convertProductFormtoProductMasterPojoUpdate(ProductForm form) throws ApiException {
		BrandMasterPojo brandMasterPojo = bService.getByBrandCategory(form.getBrand(), form.getCategory());
		ProductMasterPojo productMasterPojo = modelMapper.map(form, ProductMasterPojo.class);
		productMasterPojo.setBrand_category(brandMasterPojo);
		checkData(productMasterPojo);
		return productMasterPojo;
	}

	// Converts ProductMasterPojo to ProductData
	public ProductData convertProductMasterPojotoProductData(ProductMasterPojo productMasterPojo) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return modelMapper.map(productMasterPojo, ProductData.class);
	}

	// Converts ProductForm to ProductMasterPojo during insert operation
	public ProductMasterPojo convertProductFormtoProductMasterPojo(ProductForm form) throws ApiException {
		BrandMasterPojo brandMasterPojo = bService.getByBrandCategory(form.getBrand(), form.getCategory());
		ProductMasterPojo productMasterPojo = modelMapper.map(form, ProductMasterPojo.class);
		String barcode = StringUtil.getAlphaNumericString();
		productMasterPojo.setBarcode(barcode);
		productMasterPojo.setBrand_category(brandMasterPojo);
		checkData(productMasterPojo);
		return productMasterPojo;
	}

	// Converts list of ProductMasterPojo to list of ProductData
	public List<ProductData> getProductDataList(List<ProductMasterPojo> list) {
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductMasterPojo productMasterPojo : list) {
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			ProductData productData = modelMapper.map(productMasterPojo, ProductData.class);
			list2.add(productData);
		}
		return list2;
	}

	public void checkData(ProductMasterPojo b) throws ApiException {
		if (b.getName().isBlank() || b.getMrp() <= 0) {
			throw new ApiException("Please enter name, mrp(positive) !!");
		}
	}
}
