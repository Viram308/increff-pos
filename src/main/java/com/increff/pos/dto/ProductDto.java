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
	private BrandDto brandDto;

	@Transactional(rollbackFor = ApiException.class)
	public ProductMasterPojo add(ProductForm form) throws ApiException {
		validateData(form);
		BrandForm brandForm = ConverterUtil.convertProductFormtoBrandForm(form);
		BrandMasterPojo brandMasterPojo = brandDto.getByBrandCategory(brandForm);
		ProductMasterPojo productMasterPojo = ConverterUtil.convertProductFormtoProductMasterPojo(form,
				brandMasterPojo);

		ProductMasterPojo productMasterPojo2 = productService.add(productMasterPojo, brandMasterPojo);
		// Add product with 0 inventory
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setProductId(productMasterPojo.getId());
		inventoryPojo.setQuantity(0);
		inventoryService.add(inventoryPojo);
		return productMasterPojo2;
	}

	public List<ProductData> searchProduct(ProductSearchForm form) throws ApiException {
		BrandForm brandForm = ConverterUtil.convertProductSearchFormtoBrandForm(form);
		// search brands
		List<BrandMasterPojo> brandMasterPojoList = brandService.searchBrandData(brandForm);
		// create brandId list
		List<Integer> brandIds = brandMasterPojoList.stream().map(o -> o.getId()).collect(Collectors.toList());
		// filter using brandId list
		List<ProductMasterPojo> list = productService.searchData(form).stream()
				.filter(o -> (brandIds.contains(o.getBrand_category_id()))).collect(Collectors.toList());
		// map ProductMasterPojo to ProductData
		return list.stream().map(
				o -> ConverterUtil.convertProductMasterPojotoProductData(o, brandService.get(o.getBrand_category_id())))
				.collect(Collectors.toList());
	}

	public ProductData get(int id) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.get(id);
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		return ConverterUtil.convertProductMasterPojotoProductData(productMasterPojo, brandMasterPojo);
	}

	public ProductDetails getByBarcode(String barcode) throws ApiException {
		ProductMasterPojo productMasterPojo = productService.getByBarcode(barcode);
		BrandMasterPojo brandMasterPojo = brandService.get(productMasterPojo.getBrand_category_id());
		ProductData productData = ConverterUtil.convertProductMasterPojotoProductData(productMasterPojo,
				brandMasterPojo);
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productMasterPojo);
		return ConverterUtil.convertProductDatatoProductDetails(productData, inventoryPojo);
	}

	public List<ProductData> getAll() {
		List<ProductMasterPojo> list = productService.getAll();
		// map ProductMasterPojo to ProductData
		return list.stream().map(
				o -> ConverterUtil.convertProductMasterPojotoProductData(o, brandService.get(o.getBrand_category_id())))
				.collect(Collectors.toList());
	}

	public ProductMasterPojo update(int id, ProductForm form) throws ApiException {
		validateData(form);
		BrandForm brandForm = ConverterUtil.convertProductFormtoBrandForm(form);
		BrandMasterPojo brandMasterPojo = brandService.getByBrandCategory(brandForm);
		ProductMasterPojo productMasterPojo = ConverterUtil.convertProductFormtoProductMasterPojoUpdate(form,
				brandMasterPojo);
		return productService.update(id, productMasterPojo, brandMasterPojo);
	}

	public void validateData(ProductForm b) throws ApiException {
		if (b.name.isBlank() || b.mrp <= 0) {
			throw new ApiException("Please enter name, mrp(positive) !!");
		}
	}

}
