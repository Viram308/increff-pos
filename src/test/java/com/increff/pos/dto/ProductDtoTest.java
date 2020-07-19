package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductDetails;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestDataUtil;

public class ProductDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;

	@Test
	public void testAdd() throws ApiException {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		// search product
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		// test
		assertEquals("nestle", productDatas.get(0).brand);
		assertEquals("dairy", productDatas.get(0).category);
		assertEquals("munch", productDatas.get(0).name);
		assertEquals(10.50, productDatas.get(0).mrp, 0.01);
	}

	@Test
	public void testSearchProduct() throws ApiException {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		// search product
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		// test
		assertEquals("nestle", productDatas.get(0).brand);
		assertEquals("dairy", productDatas.get(0).category);
		assertEquals("munch", productDatas.get(0).name);
		assertEquals(10.50, productDatas.get(0).mrp, 0.01);
	}

	@Test
	public void testGet() throws ApiException {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		// search product
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		// get data
		ProductData productData = productDto.get(productDatas.get(0).id);
		// test
		assertEquals("nestle", productData.brand);
		assertEquals("dairy", productData.category);
		assertEquals("munch", productData.name);
		assertEquals(10.50, productData.mrp, 0.01);
	}

	@Test
	public void testGetByBarcode() throws ApiException {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		// search product
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		// get by barcode
		ProductDetails productDetails = productDto.getByBarcode(productDatas.get(0).barcode);
		// test
		assertEquals("nestle", productDetails.brand);
		assertEquals("dairy", productDetails.category);
		assertEquals("munch", productDetails.name);
		assertEquals(10.50, productDetails.mrp, 0.01);
		assertEquals(0, productDetails.availableQuantity);
	}

	@Test
	public void testGetAll() throws ApiException {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		// get all
		List<ProductData> productDatas = productDto.getAll();
		assertEquals(1, productDatas.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		// search product
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		ProductForm productForm2 = TestDataUtil.getProductFormDto("nestle", "dairy", "kitkat", 10.50);
		// update product
		productDto.update(productDatas.get(0).id, productForm2);
		// get and test
		ProductData productData = productDto.get(productDatas.get(0).id);
		assertEquals("kitkat", productData.name);
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		// validate
		productDto.validateData(productForm);
		// throws exception
		productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "   ", 0.0);
		productDto.validateData(productForm);
	}
}
