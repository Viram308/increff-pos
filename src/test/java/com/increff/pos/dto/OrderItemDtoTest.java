package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.OrderSearchForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtil;

public class OrderItemDtoTest extends AbstractUnitTest {
	@Autowired
	private OrderItemDto orderItemDto;
	@Autowired
	private InventoryDto inventoryDto;
	@Autowired
	private BrandDto brandDto;
	@Autowired
	private ProductDto productDto;
	@Autowired
	private OrderDto orderDto;

	@Test
	public void testGetByOrderId() throws ApiException, ParseException {
		// add data
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = TestUtil.getInventoryFormDto(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = TestUtil.getInventoryFormDto(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);
		// get order item array
		OrderItemForm[] orderItemForms = TestUtil.getOrderItemFormArrayDto(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		// create order
		orderDto.createOrder(orderItemForms);
		// create order search form
		OrderSearchForm orderSearchForm = TestUtil.getOrderSearchFormDto();
		// search
		List<OrderData> orderDatas = orderDto.searchOrder(orderSearchForm);
		int orderId = orderDatas.get(0).id;
		List<OrderItemData> orderItemDatas = orderItemDto.get(orderId);
		assertEquals(2, orderItemDatas.size());
	}

	// functions for creating data

	private ProductData getProductData(String brand, String category, String name, double mrp) throws ApiException {
		BrandForm brandForm = TestUtil.getBrandFormDto(brand, category);
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestUtil.getProductFormDto(brand, category, name, mrp);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = TestUtil.getProductSearchFormDto("", "", "", name);
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		return productDatas.get(0);
	}

}
