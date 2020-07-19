package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestDataUtil;

public class InventoryDtoTest extends AbstractUnitTest {
	@Autowired
	private InventoryDto inventoryDto;

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;

	@Test
	public void testAddInventory() throws ApiException {
		// add
		ProductData productData = getProductData();
		InventoryForm inventoryForm = TestDataUtil.getInventoryFormDto(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		// search
		InventorySearchForm inventorySearchForm = TestDataUtil.getInventorySearchFormDto(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// test added data
		assertEquals(20, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testSearchInventory() throws ApiException {
		// add
		ProductData productData = getProductData();
		InventoryForm inventoryForm = TestDataUtil.getInventoryFormDto(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		// search
		InventorySearchForm inventorySearchForm = TestDataUtil.getInventorySearchFormDto(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// test retrieved data
		assertEquals("munch", inventoryDatas.get(0).name);
		assertEquals(20, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testGetInventoryData() throws ApiException {
		// add
		ProductData productData = getProductData();
		InventoryForm inventoryForm = TestDataUtil.getInventoryFormDto(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		// search
		InventorySearchForm inventorySearchForm = TestDataUtil.getInventorySearchFormDto(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// get data
		InventoryData inventoryData = inventoryDto.getInventoryData(inventoryDatas.get(0).id);
		// test
		assertEquals("munch", inventoryData.name);
		assertEquals(20, inventoryData.quantity);
	}

	@Test
	public void testUpdateInventory() throws ApiException {
		// add
		ProductData productData = getProductData();
		InventoryForm inventoryForm = TestDataUtil.getInventoryFormDto(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		// search
		InventorySearchForm inventorySearchForm = TestDataUtil.getInventorySearchFormDto(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		inventoryForm = TestDataUtil.getInventoryFormDto(inventoryDatas.get(0).barcode, 30);
		// update
		inventoryDto.updateInventory(inventoryDatas.get(0).id, inventoryForm);
		InventoryData inventoryData = inventoryDto.getInventoryData(inventoryDatas.get(0).id);
		// test updated data
		assertEquals(30, inventoryData.quantity);
	}

	@Test
	public void testGetAllInventory() throws ApiException {
		// add
		ProductData productData = getProductData();
		InventoryForm inventoryForm = TestDataUtil.getInventoryFormDto(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		// get all data
		List<InventoryData> inventoryDatas = inventoryDto.getAllInventory();
		// test
		assertEquals(1, inventoryDatas.size());
	}

	@Test(expected = ApiException.class)
	public void testValidateData() throws ApiException {
		InventoryForm inventoryForm = TestDataUtil.getInventoryFormDto("barcode", 10);
		// validate
		inventoryDto.validateData(inventoryForm);
		inventoryForm = TestDataUtil.getInventoryFormDto("barcode", -5);
		// throws exception
		inventoryDto.validateData(inventoryForm);
	}

	private ProductData getProductData() throws ApiException {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		return productDatas.get(0);
	}

}
