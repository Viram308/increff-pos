package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductDetails;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.ConverterUtil;

public class ProductDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;

	@Autowired
	private ConverterUtil converterUtil;

	@Test
	public void testAdd() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		assertEquals("nestle", productDatas.get(0).brand);
		assertEquals("dairy", productDatas.get(0).category);
		assertEquals("munch", productDatas.get(0).name);
		assertEquals(10.50, productDatas.get(0).mrp, 0.01);
	}

	@Test
	public void testSearchProduct() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		assertEquals("nestle", productDatas.get(0).brand);
		assertEquals("dairy", productDatas.get(0).category);
		assertEquals("munch", productDatas.get(0).name);
		assertEquals(10.50, productDatas.get(0).mrp, 0.01);
	}

	@Test
	public void testGet() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		ProductData productData = productDto.get(productDatas.get(0).id);
		assertEquals("nestle", productData.brand);
		assertEquals("dairy", productData.category);
		assertEquals("munch", productData.name);
		assertEquals(10.50, productData.mrp, 0.01);
	}

	@Test
	public void testGetByBarcode() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		ProductDetails productDetails = productDto.getByBarcode(productDatas.get(0).barcode);
		assertEquals("nestle", productDetails.brand);
		assertEquals("dairy", productDetails.category);
		assertEquals("munch", productDetails.name);
		assertEquals(10.50, productDetails.mrp, 0.01);
		assertEquals(0, productDetails.availableQuantity);
	}

	@Test
	public void testGetAll() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		List<ProductData> productDatas = productDto.getAll();
		assertEquals(1, productDatas.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		ProductForm productForm2 = getProductForm("nestle", "dairy", "kitkat", 10.50);
		productDto.update(productDatas.get(0).id, productForm2);
		ProductData productData = productDto.get(productDatas.get(0).id);
		assertEquals("kitkat", productData.name);
	}

	@Test
	public void testGetProductIdList() throws ApiException {
		BrandMasterPojo brandMasterPojo1 = new BrandMasterPojo();
		brandMasterPojo1.setId(1);
		BrandMasterPojo brandMasterPojo2 = new BrandMasterPojo();
		brandMasterPojo2.setId(2);
		ProductForm productForm1 = getProductForm("nestle", "dairy", "munch", 10.50);
		ProductForm productForm2 = getProductForm("nestle", "dairy", "kitkat", 10.50);
		ProductMasterPojo productMasterPojo1 = converterUtil.convertProductFormtoProductMasterPojo(productForm1,
				brandMasterPojo1);
		ProductMasterPojo productMasterPojo2 = converterUtil.convertProductFormtoProductMasterPojo(productForm2,
				brandMasterPojo2);
		productMasterPojo1.setId(1);
		productMasterPojo2.setId(2);
		List<ProductMasterPojo> productMasterPojolist = new ArrayList<ProductMasterPojo>();
		productMasterPojolist.add(productMasterPojo1);
		productMasterPojolist.add(productMasterPojo2);
		List<Integer> productIds = productDto.getProductIdList(productMasterPojolist);
		assertEquals(2, productIds.size());
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.validateData(productForm);
		// throws exception
		productForm = getProductForm("nestle", "dairy", "   ", 0.0);
		productDto.validateData(productForm);
	}

	@Test(expected = ApiException.class)
	public void testCheckSearchData() throws ApiException {
		ProductSearchForm productSearchForm = getProductSearchForm("barcode", "nestle", "dairy", "munch");
		productDto.checkSearchData(productSearchForm);
		// throws exception
		productSearchForm = getProductSearchForm("", " ", "", "");
		productDto.checkSearchData(productSearchForm);
	}

	private ProductSearchForm getProductSearchForm(String barcode, String brand, String category, String name) {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = barcode;
		productSearchForm.brand = brand;
		productSearchForm.category = category;
		productSearchForm.name = name;
		return productSearchForm;
	}

	private BrandForm getBrandForm(String brand, String category) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = brand;
		brandForm.category = category;
		return brandForm;
	}

	private ProductForm getProductForm(String brand, String category, String name, double mrp) {
		ProductForm productForm = new ProductForm();
		productForm.brand = brand;
		productForm.category = category;
		productForm.name = name;
		productForm.mrp = mrp;
		return productForm;
	}
}
