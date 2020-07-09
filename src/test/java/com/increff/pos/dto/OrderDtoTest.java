package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
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

public class OrderDtoTest extends AbstractUnitTest {
	@Autowired
	private InventoryDto inventoryDto;

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;
	@Autowired
	private OrderDto orderDto;
	@Autowired
	private ConverterUtil converterUtil;

	@Test
	public void testCreateOrder() throws ApiException, ParseException {

		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = getInventoryForm(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = getInventoryForm(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);

		OrderItemForm[] orderItemForms = getOrderItemFormArray(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		List<BillData> billDatas = orderDto.createOrder(orderItemForms);
		assertEquals(2, billDatas.size());
		assertEquals("munch", billDatas.get(0).name);
		assertEquals("kitkat", billDatas.get(1).name);
		assertEquals(10, billDatas.get(0).mrp, 0.01);
		assertEquals(15, billDatas.get(1).mrp, 0.01);
	}

	@Test
	public void testAddOrder() throws ApiException {
		OrderPojo orderPojo = orderDto.addOrder();
		assertEquals(converterUtil.getDateTime().split(" ")[0], orderPojo.getDatetime().split(" ")[0]);
	}

	@Test
	public void testChangeOrder() throws ApiException, ParseException {
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
		orderItemForms = getOrderItemFormArray(productData1.barcode, productData2.barcode, productData1.name,
				productData2.name, 2, 8, productData1.mrp, productData2.mrp);
		orderDto.changeOrder(orderId, orderItemForms);
		InventorySearchForm inventorySearchForm = getInventorySearchForm("", "munch");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		assertEquals(18, inventoryDatas.get(0).quantity);
		inventorySearchForm = getInventorySearchForm("", "kitkat");
		inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		assertEquals(12, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testUpdateOrder() throws ApiException {
		OrderPojo orderPojo1 = orderDto.addOrder();
		OrderPojo orderPojo2 = orderDto.updateOrder(orderPojo1.getId());
		assertEquals(orderPojo1.getId(), orderPojo2.getId());
	}

	@Test
	public void testAddInInventory() throws ApiException {
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = getInventoryForm(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = getInventoryForm(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);
		List<OrderItemData> orderItemDatas = getOrderItemDataList(productData1, productData2, 5, 10);
		orderDto.addInInventory(orderItemDatas);
		InventorySearchForm inventorySearchForm = getInventorySearchForm("", "munch");
		List<InventoryData> inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		assertEquals(25, inventoryDatas.get(0).quantity);
		inventorySearchForm = getInventorySearchForm("", "kitkat");
		inventoryDatas = inventoryDto.searchInventory(inventorySearchForm);
		assertEquals(30, inventoryDatas.get(0).quantity);
	}

	@Test
	public void testGet() throws ApiException {
		OrderPojo orderPojo = orderDto.addOrder();
		OrderData orderData = orderDto.get(orderPojo.getId());
		assertEquals(orderPojo.getDatetime(), orderData.datetime);
	}

	@Test
	public void testGetAll() throws ApiException {
		orderDto.addOrder();
		orderDto.addOrder();
		List<OrderData> orderDatas = orderDto.getAll();
		assertEquals(2, orderDatas.size());
	}

	@Test
	public void testSearchOrder() throws ApiException, ParseException {
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
		assertEquals(1, orderDatas.size());
	}

	@Test(expected = ApiException.class)
	public void testZeroSearchOrder() throws ParseException, ApiException {
		OrderSearchForm orderSearchForm = getOrderSearchForm();
		orderDto.searchOrder(orderSearchForm);
	}

	@Test(expected = ApiException.class)
	public void testCheckSearchData() throws ApiException {
		OrderSearchForm orderSearchForm = getOrderSearchForm();
		orderDto.checkSearchData(orderSearchForm);
		orderSearchForm = getOrderSearchForm();
		orderSearchForm.startdate = "";
		orderDto.checkSearchData(orderSearchForm);
	}

	private List<OrderItemData> getOrderItemDataList(ProductData productData1, ProductData productData2, int quantity1,
			int quantity2) {
		List<OrderItemData> orderItemDatas = new ArrayList<OrderItemData>();
		OrderItemData orderItemData1 = new OrderItemData();
		OrderItemData orderItemData2 = new OrderItemData();
		orderItemData1.barcode = productData1.barcode;
		orderItemData1.quantity = quantity1;
		orderItemDatas.add(orderItemData1);
		orderItemData2.barcode = productData2.barcode;
		orderItemData2.quantity = quantity2;
		orderItemDatas.add(orderItemData2);
		return orderItemDatas;
	}

	private InventorySearchForm getInventorySearchForm(String barcode, String name) {
		InventorySearchForm inventorySearchForm = new InventorySearchForm();
		inventorySearchForm.barcode = barcode;
		inventorySearchForm.name = name;
		return inventorySearchForm;
	}

	private OrderSearchForm getOrderSearchForm() {
		OrderSearchForm orderSearchForm = new OrderSearchForm();
		orderSearchForm.startdate = converterUtil.getDateTime().split(" ")[0];
		orderSearchForm.enddate = converterUtil.getDateTime().split(" ")[0];
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
