package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BillData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.OrderSearchForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.ConverterUtil;
import com.increff.pos.util.TestDataUtil;

public class OrderDtoTest extends AbstractUnitTest {
	@Autowired
	private InventoryDto inventoryDto;

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;
	@Autowired
	private OrderDto orderDto;

	@Test
	public void testCreateOrder() throws ApiException, ParseException {
		// get array of items
		OrderItemForm[] orderItemForms = getOrderItemArray();
		// create order
		List<BillData> billDatas = orderDto.createOrder(orderItemForms);
		// test values
		assertEquals(2, billDatas.size());
		assertEquals("munch", billDatas.get(0).name);
		assertEquals("kitkat", billDatas.get(1).name);
		assertEquals(10, billDatas.get(0).mrp, 0.01);
		assertEquals(15, billDatas.get(1).mrp, 0.01);
	}

	@Test
	public void testAddOrder() throws ApiException {
		// get array of items
		OrderItemForm[] orderItemForms = getOrderItemArray();
		List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		OrderPojo orderPojo = orderDto.addOrder(orderItems);
		assertEquals(ConverterUtil.getDateTime().split(" ")[0], orderPojo.getDatetime().split(" ")[0]);
	}

	@Test
	public void testChangeOrder() throws ApiException, ParseException {
		// add data
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = TestDataUtil.getInventoryFormDto(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = TestDataUtil.getInventoryFormDto(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);
		// get array of items
		OrderItemForm[] orderItemForms = TestDataUtil.getOrderItemFormArrayDto(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		// create order
		orderDto.createOrder(orderItemForms);
		// create order search form
		OrderSearchForm orderSearchForm = TestDataUtil.getOrderSearchFormDto();
		List<OrderData> orderDatas = orderDto.searchOrder(orderSearchForm);
		int orderId = orderDatas.get(0).id;
		// update array
		orderItemForms = TestDataUtil.getOrderItemFormArrayDto(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 2, 8, productData1.mrp, productData2.mrp);
		// update order
		orderDto.changeOrder(orderId, orderItemForms);
		// create inventory search form
		InventorySearchForm inventorySearchForm = TestDataUtil.getInventorySearchFormDto("", "munch");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// test updated inventory
		assertEquals(18, inventoryDatas.get(0).quantity);
		// create inventory search form
		inventorySearchForm = TestDataUtil.getInventorySearchFormDto("", "kitkat");
		inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// test updated inventory
		assertEquals(12, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testUpdateOrder() throws ApiException {
		// get array of items
		OrderItemForm[] orderItemForms = getOrderItemArray();
		List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
		OrderPojo orderPojo1 = orderDto.addOrder(orderItems);
		// update order details
		OrderPojo orderPojo2 = orderDto.updateOrder(orderPojo1.getId(), orderItems);
		assertEquals(orderPojo1.getId(), orderPojo2.getId());
	}

	@Test
	public void testAddInInventory() throws ApiException {
		// add data
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = TestDataUtil.getInventoryFormDto(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = TestDataUtil.getInventoryFormDto(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);
		// get order item data
		List<OrderItemData> orderItemDatas = TestDataUtil.getOrderItemDataListDto(productData1, productData2, 5, 10);
		// add in inventory
		orderDto.addInInventory(orderItemDatas);
		// create inventory search form
		InventorySearchForm inventorySearchForm = TestDataUtil.getInventorySearchFormDto("", "munch");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// test added inventory
		assertEquals(25, inventoryDatas.get(0).quantity);
		// create inventory search form
		inventorySearchForm = TestDataUtil.getInventorySearchFormDto("", "kitkat");
		inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		// test added inventory
		assertEquals(30, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testGet() throws ApiException, ParseException {
		// get order item array
		OrderItemForm[] orderItemForms = getOrderItemArray();
		// create order
		orderDto.createOrder(orderItemForms);
		// create order search form
		OrderSearchForm orderSearchForm = TestDataUtil.getOrderSearchFormDto();
		// search order
		List<OrderData> orderDatas = orderDto.searchOrder(orderSearchForm);
		OrderData orderData = orderDto.get(orderDatas.get(0).id);
		// test
		assertEquals(orderDatas.get(0).datetime, orderData.datetime);
	}

	@Test
	public void testGetAll() throws ApiException {
		// get order item array
		OrderItemForm[] orderItemForms = getOrderItemArray();
		// create order
		orderDto.createOrder(orderItemForms);
		List<OrderData> orderDatas = orderDto.getAll();
		assertEquals(1, orderDatas.size());
	}

	@Test
	public void testSearchOrder() throws ApiException, ParseException {
		// get order item array
		OrderItemForm[] orderItemForms = getOrderItemArray();
		// create order
		orderDto.createOrder(orderItemForms);
		// search order
		OrderSearchForm orderSearchForm = TestDataUtil.getOrderSearchFormDto();
		List<OrderData> orderDatas = orderDto.searchOrder(orderSearchForm);
		assertEquals(1, orderDatas.size());
		int orderId = orderDatas.get(0).id;
		// search by order id
		orderSearchForm.orderId = orderId;
		orderDatas = orderDto.searchOrder(orderSearchForm);
		assertEquals(1, orderDatas.size());
		orderSearchForm.orderId = orderId + 1;
		orderDatas = orderDto.searchOrder(orderSearchForm);
		assertEquals(0, orderDatas.size());
	}

	// functions for creating proper data

	private OrderItemForm[] getOrderItemArray() throws ApiException {
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = TestDataUtil.getInventoryFormDto(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = TestDataUtil.getInventoryFormDto(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);

		return TestDataUtil.getOrderItemFormArrayDto(productData1.barcode, productData2.barcode, productData1.name,
				productData2.name, 4, 5, productData1.mrp, productData2.mrp);
	}

	private ProductData getProductData(String brand, String category, String name, double mrp) throws ApiException {
		BrandForm brandForm = TestDataUtil.getBrandFormDto(brand, category);
		brandDto.addBrand(brandForm);
		ProductForm productForm = TestDataUtil.getProductFormDto(brand, category, name, mrp);
		productDto.add(productForm);
		ProductSearchForm productSearchForm = TestDataUtil.getProductSearchFormDto("", "", "", name);
		List<ProductData> productDatas = productDto.searchProduct(productSearchForm);
		return productDatas.get(0);
	}
}
