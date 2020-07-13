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

public class InventoryDtoTest extends AbstractUnitTest {
	@Autowired
	private InventoryDto inventoryDto;

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;

	@Test
	public void testAddInventory() throws ApiException {
		ProductData productData = getProductData();
		InventoryForm inventoryForm = getInventoryForm(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		InventorySearchForm inventorySearchForm = getInventorySearchForm(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		assertEquals(20, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testSearchInventory() throws ApiException {
		ProductData productData = getProductData();
		InventoryForm inventoryForm = getInventoryForm(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		InventorySearchForm inventorySearchForm = getInventorySearchForm(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		assertEquals("munch", inventoryDatas.get(0).name);
		assertEquals(20, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testGetInventoryData() throws ApiException {
		ProductData productData = getProductData();
		InventoryForm inventoryForm = getInventoryForm(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		InventorySearchForm inventorySearchForm = getInventorySearchForm(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		InventoryData inventoryData = inventoryDto.getInventoryData(inventoryDatas.get(0).id);
		assertEquals("munch", inventoryData.name);
		assertEquals(20, inventoryData.quantity);
	}

	@Test
	public void testUpdateInventory() throws ApiException {
		ProductData productData = getProductData();
		InventoryForm inventoryForm = getInventoryForm(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		InventorySearchForm inventorySearchForm = getInventorySearchForm(productData.barcode, "");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		inventoryForm = getInventoryForm(inventoryDatas.get(0).barcode, 30);
		inventoryDto.updateInventory(inventoryDatas.get(0).id, inventoryForm);
		InventoryData inventoryData = inventoryDto.getInventoryData(inventoryDatas.get(0).id);
		assertEquals(30, inventoryData.quantity);
	}

	@Test
	public void testGetAllInventory() throws ApiException {
		ProductData productData = getProductData();
		InventoryForm inventoryForm = getInventoryForm(productData.barcode, 20);
		inventoryDto.addInventory(inventoryForm);
		List<InventoryData> inventoryDatas = inventoryDto.getAllInventory();
		assertEquals(1, inventoryDatas.size());
	}

	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		InventoryForm inventoryForm = getInventoryForm("barcode", 10);
		inventoryDto.validateData(inventoryForm);
		inventoryForm = getInventoryForm("barcode", -5);
		// throws exception
		inventoryDto.validateData(inventoryForm);
	}

	private InventorySearchForm getInventorySearchForm(String barcode, String name) {
		InventorySearchForm inventorySearchForm = new InventorySearchForm();
		inventorySearchForm.barcode = barcode;
		inventorySearchForm.name = name;
		return inventorySearchForm;
	}

	private InventoryForm getInventoryForm(String barcode, int quantity) {
		InventoryForm inventoryForm = new InventoryForm();
		inventoryForm.barcode = barcode;
		inventoryForm.quantity = quantity;
		return inventoryForm;
	}

	private ProductData getProductData() throws ApiException {
		BrandForm brandForm = getBrandForm("     nestLE        ", "DairY ");
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm("nestle", "dairy", "munch", 10.50);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "nestle", "", "");
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		return productDatas.get(0);
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
