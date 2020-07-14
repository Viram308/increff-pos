package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BillData;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.ConverterUtil;

public class ReportDtoTest extends AbstractUnitTest {
	@Autowired
	private ReportDto reportDto;
	@Autowired
	private InventoryDto inventoryDto;

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private ProductDto productDto;
	@Autowired
	private OrderDto orderDto;

	@Test
	public void testGetOrderIds() throws ApiException, ParseException {
		addOrder();
		// get sales report form
		SalesReportForm salesReportForm = getSalesReportForm(ConverterUtil.getDateTime().split(" ")[0],
				ConverterUtil.getDateTime().split(" ")[0], "", "");
		// get order id list
		List<Integer> orderIds = reportDto.getOrderIds(salesReportForm);
		// test
		assertEquals(1, orderIds.size());
		salesReportForm = getSalesReportForm("", "", "", "");
		orderIds = reportDto.getOrderIds(salesReportForm);
		assertEquals(1, orderIds.size());
	}

	@Test
	public void testGetSalesReport() throws ApiException, ParseException {
		addOrder();
		// get sales report form
		SalesReportForm salesReportForm = getSalesReportForm("", "", "n", "");
		// get sales report
		List<SalesReportData> salesReportDatas = reportDto.getSalesReport(salesReportForm);
		// test data
		assertEquals(2, salesReportDatas.size());
		assertEquals(40, salesReportDatas.get(0).revenue, 0.01);
		assertEquals(75, salesReportDatas.get(1).revenue, 0.01);
		assertEquals(4, salesReportDatas.get(0).quantity);
		assertEquals(5, salesReportDatas.get(1).quantity);
	}

	@Test
	public void testSearchBrandReport() throws ApiException {
		addOrder();
		BrandForm brandForm = getBrandForm("nestle", "");
		// get brand report
		List<BrandData> brandDatas = reportDto.searchBrandReport(brandForm);
		// test
		assertEquals(2, brandDatas.size());
		brandForm = getBrandForm("nestle", "f");
		brandDatas = reportDto.searchBrandReport(brandForm);
		assertEquals(1, brandDatas.size());
	}

	@Test
	public void testSearchInventoryReport() throws ApiException {
		addOrder();
		BrandForm brandForm = getBrandForm("nestle", "");
		// get inventory report
		List<InventoryReportData> inventoryReportDatas = reportDto.searchInventoryReport(brandForm);
		// test data
		assertEquals(2, inventoryReportDatas.size());
		assertEquals(16, inventoryReportDatas.get(0).quantity);
		assertEquals(15, inventoryReportDatas.get(1).quantity);
		brandForm = getBrandForm("nestle", "f");
		inventoryReportDatas = reportDto.searchInventoryReport(brandForm);
		assertEquals(1, inventoryReportDatas.size());
		assertEquals(15, inventoryReportDatas.get(0).quantity);
	}

	// functions for creating data
	
	private List<BillData> addOrder() throws ApiException {
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = getInventoryForm(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = getInventoryForm(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);

		OrderItemForm[] orderItemForms = getOrderItemFormArray(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		return orderDto.createOrder(orderItemForms);
	}

	private SalesReportForm getSalesReportForm(String startdate, String enddate, String brand, String category) {
		SalesReportForm salesReportForm = new SalesReportForm();
		salesReportForm.startdate = startdate;
		salesReportForm.enddate = enddate;
		salesReportForm.brand = brand;
		salesReportForm.category = category;
		return salesReportForm;
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
