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
import com.increff.pos.util.ConverterUtil;

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
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = getInventoryForm(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = getInventoryForm(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);

		OrderItemForm[] orderItemForms = getOrderItemFormArray(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		orderDto.createOrder(orderItemForms);
		OrderSearchForm orderSearchForm = getOrderSearchForm();
		List<OrderData> orderDatas = orderDto.searchOrder(orderSearchForm);
		int orderId = orderDatas.get(0).id;
		List<OrderItemData> orderItemDatas = orderItemDto.get(orderId);
		assertEquals(2, orderItemDatas.size());
	}

	@Test
	public void testSearchOrderItems() throws ApiException {
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = getInventoryForm(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = getInventoryForm(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);

		OrderItemForm[] orderItemForms = getOrderItemFormArray(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		orderDto.createOrder(orderItemForms);
		OrderItemData orderItemData = getOrderItemData("", "m", 0);
		List<OrderItemData> orderItemDatas = orderItemDto.searchOrderItem(orderItemData);
		assertEquals("munch", orderItemDatas.get(0).name);
		assertEquals(10, orderItemDatas.get(0).sellingPrice, 0.01);
		assertEquals(productData1.barcode, orderItemDatas.get(0).barcode);
		assertEquals(4, orderItemDatas.get(0).quantity);
		
		List<OrderData> orderDatas = orderDto.getAll();
		int orderId=orderDatas.get(0).id;
		orderItemData = getOrderItemData("", "m", orderId);
		orderItemDatas = orderItemDto.searchOrderItem(orderItemData);
		assertEquals(1, orderItemDatas.size());
		orderItemData = getOrderItemData("", "m", orderId+1);
		orderItemDatas = orderItemDto.searchOrderItem(orderItemData);
		assertEquals(0, orderItemDatas.size());
		
	}

	private OrderItemData getOrderItemData(String barcode, String name, int orderId) {
		OrderItemData orderItemData = new OrderItemData();
		orderItemData.barcode = barcode;
		orderItemData.orderId = orderId;
		orderItemData.name = name;
		return orderItemData;
	}

	private OrderSearchForm getOrderSearchForm() {
		OrderSearchForm orderSearchForm = new OrderSearchForm();
		orderSearchForm.startdate = ConverterUtil.getDateTime().split(" ")[0];
		orderSearchForm.enddate = ConverterUtil.getDateTime().split(" ")[0];
		orderSearchForm.orderId = 0;
		orderSearchForm.orderCreater="";
		return orderSearchForm;
	}

	private OrderItemForm[] getOrderItemFormArray(String barcode1, String barcode2, String name1, String name2,
			int quantity1, int quantity2, double mrp1, double mrp2) {
		OrderItemForm[] orderItemForms = new OrderItemForm[2];
		orderItemForms[0] = new OrderItemForm();
		orderItemForms[1] = new OrderItemForm();
		orderItemForms[0].barcode = barcode1;
		orderItemForms[0].name = name1;
		orderItemForms[0].quantity = quantity1;
		orderItemForms[0].sellingPrice = mrp1;
		orderItemForms[1].barcode = barcode2;
		orderItemForms[1].name = name2;
		orderItemForms[1].quantity = quantity2;
		orderItemForms[1].sellingPrice = mrp2;
		return orderItemForms;
	}

	private InventoryForm getInventoryForm(String barcode, int quantity) {
		InventoryForm inventoryForm = new InventoryForm();
		inventoryForm.barcode = barcode;
		inventoryForm.quantity = quantity;
		return inventoryForm;
	}

	private ProductData getProductData(String brand, String category, String name, double mrp) throws ApiException {
		BrandForm brandForm = getBrandForm(brand, category);
		brandDto.addBrand(brandForm);
		ProductForm productForm = getProductForm(brand, category, name, mrp);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = getProductSearchForm("", "", "", name);
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
