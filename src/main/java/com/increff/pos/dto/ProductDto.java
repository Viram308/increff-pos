package com.increff.pos.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductDetails;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class ProductDto {

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ConverterUtil converterUtil;
	@Autowired
	private BrandDto brandDto;

	@Transactional(rollbackFor = ApiException.class)
	public ProductMasterPojo add(ProductForm form) throws ApiException {
		validateData(form);
		BrandForm brandForm = converterUtil.convertProductFormtoBrandForm(form);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		ProductMasterPojo productMasterPojo = converterUtil.convertProductFormtoProductMasterPojo(form,
				brandMasterPojo);

		ProductMasterPojo productMasterPojo2=productService.add(productMasterPojo, brandMasterPojo);
		// Add product with 0 inventory
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setProductid(productMasterPojo.getId());
		inventoryPojo.setQuantity(0);
		inventoryService.add(inventoryPojo);
		return productMasterPojo2;
	}

	public List<ProductData> searchProduct(ProductSearchForm form) throws ApiException {
		BrandForm brandForm = converterUtil.convertProductSearchFormtoBrandForm(form);
		List<BrandMasterPojo> brandMasterPojoList = brandService.searchBrandData(brandForm);
		List<Integer> brandIds = brandMasterPojoList.stream().map(o -> o.getId()).collect(Collectors.toList());
		List<ProductMasterPojo> list = productService.searchData(form).stream()
				.filter(o -> (brandIds.contains(o.getBrand_category_id()))).collect(Collectors.toList());
		return list.stream().map(
				o -> converterUtil.convertProductMasterPojotoProductData(o, brandService.get(o.getBrand_category_id())))
				.collect(Collectors.toList());
	}

	public ProductData get(int id) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.get(id);
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		return converterUtil.convertProductMasterPojotoProductData(productMasterPojo, brandMasterPojo);
	}

	public ProductDetails getByBarcode(String barcode) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.getByBarcode(barcode);
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		ProductData productData = converterUtil.convertProductMasterPojotoProductData(productMasterPojo,
				brandMasterPojo);
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productMasterPojo);
		return converterUtil.convertProductDatatoProductDetails(productData, inventoryPojo);
	}

	public List<ProductData> getAll() {
		List<ProductMasterPojo> list = productService.getAll();
		return list.stream().map(
				o -> converterUtil.convertProductMasterPojotoProductData(o, brandService.get(o.getBrand_category_id())))
				.collect(Collectors.toList());
	}

	public ProductMasterPojo update(int id, ProductForm form) throws ApiException {
		validateData(form);
		BrandForm brandForm = converterUtil.convertProductFormtoBrandForm(form);
		BrandMasterPojo brandMasterPojo = brandService.getByBrandCategory(brandForm);
		ProductMasterPojo productMasterPojo = converterUtil.convertProductFormtoProductMasterPojoUpdate(form,
				brandMasterPojo);
		return productService.update(id, productMasterPojo, brandMasterPojo);
	}

	public void validateData(ProductForm b) throws ApiException {
		if (b.name.isBlank() || b.mrp <= 0) {
			throw new ApiException("Please enter name, mrp(positive) !!");
		}
	}

}
