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
import com.increff.pos.util.TestDataUtil;

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
		SalesReportForm salesReportForm = TestDataUtil.getSalesReportFormDto(ConverterUtil.getDateTime().split(" ")[0],
				ConverterUtil.getDateTime().split(" ")[0], "", "");
		// get order id list
		List<Integer> orderIds = reportDto.getOrderIds(salesReportForm);
		// test
		assertEquals(1, orderIds.size());
		salesReportForm = TestDataUtil.getSalesReportFormDto("", "", "", "");
		orderIds = reportDto.getOrderIds(salesReportForm);
		assertEquals(1, orderIds.size());
	}

	@Test
	public void testGetSalesReport() throws ApiException, ParseException {
		addOrder();
		// get sales report form
		SalesReportForm salesReportForm = TestDataUtil.getSalesReportFormDto("", "", "n", "");
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
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestle", "");
		// get brand report
		List<BrandData> brandDatas = reportDto.searchBrandReport(brandForm);
		// test
		assertEquals(2, brandDatas.size());
		brandForm = TestDataUtil.getBrandFormDto("nestle", "f");
		brandDatas = reportDto.searchBrandReport(brandForm);
		assertEquals(1, brandDatas.size());
	}

	@Test
	public void testSearchInventoryReport() throws ApiException {
		addOrder();
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestle", "");
		// get inventory report
		List<InventoryReportData> inventoryReportDatas = reportDto.searchInventoryReport(brandForm);
		// test data
		assertEquals(2, inventoryReportDatas.size());
		assertEquals(16, inventoryReportDatas.get(0).quantity);
		assertEquals(15, inventoryReportDatas.get(1).quantity);
		brandForm = TestDataUtil.getBrandFormDto("nestle", "f");
		inventoryReportDatas = reportDto.searchInventoryReport(brandForm);
		assertEquals(1, inventoryReportDatas.size());
		assertEquals(15, inventoryReportDatas.get(0).quantity);
	}

	// functions for creating data
	
	private List<BillData> addOrder() throws ApiException {
		ProductData productData1 = getProductData("nestle", "dairy", "munch", 10);
		InventoryForm inventoryForm1 = TestDataUtil.getInventoryFormDto(productData1.barcode, 20);
		inventoryDto.addInventory(inventoryForm1);
		ProductData productData2 = getProductData("nestle", "food", "kitkat", 15);
		InventoryForm inventoryForm2 = TestDataUtil.getInventoryFormDto(productData2.barcode, 20);
		inventoryDto.addInventory(inventoryForm2);

		OrderItemForm[] orderItemForms = TestDataUtil.getOrderItemFormArrayDto(productData1.barcode, productData2.barcode,
				productData1.name, productData2.name, 4, 5, productData1.mrp, productData2.mrp);
		return orderDto.createOrder(orderItemForms);
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
