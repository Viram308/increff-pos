package com.increff.pos.util;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductDetails;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class ConverterUtilTest extends AbstractUnitTest {

	// test ConverterUtil

	// All the tests are just testing converted data with expected one

	@Test
	public void testConvertUserFormtoUserPojo() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		UserForm f = TestUtil.getUserForm();
		UserPojo p = ConverterUtil.convertUserFormtoUserPojo(f);
		assertEquals(f.getEmail(), p.getEmail());
		assertEquals(f.getRole(), p.getRole());
	}

	@Test
	public void testConvertUserPojotoUserData() {
		UserPojo p = TestUtil.getUserPojo();
		int id = 1;
		p.setId(id);
		UserData d = ConverterUtil.convertUserPojotoUserData(p);
		assertEquals(p.getEmail(), d.getEmail());
		assertEquals(p.getRole(), d.getRole());
		assertEquals(id, d.getId());
	}

	@Test
	public void testConvertBrandMasterPojotoBrandData() throws ApiException {
		BrandMasterPojo b = TestUtil.getBrandMasterPojo();
		int id = 1;
		b.setId(id);
		BrandData d = ConverterUtil.convertBrandMasterPojotoBrandData(b);
		assertEquals(b.getBrand(), d.brand);
		assertEquals(b.getCategory(), d.category);
		assertEquals(b.getId(), d.id);
	}

	@Test
	public void testConvertBrandFormtoBrandMasterPojo() {
		BrandForm f = TestUtil.getBrandForm();
		BrandMasterPojo p = ConverterUtil.convertBrandFormtoBrandMasterPojo(f);
		assertEquals(f.brand, p.getBrand());
		assertEquals(f.category, p.getCategory());
	}

	@Test
	public void testConvertInventoryPojotoInventoryData() throws ApiException {
		InventoryPojo i = TestUtil.getInventoryPojo();
		ProductMasterPojo productMasterPojo = TestUtil.getProductMasterPojo();
		InventoryData d = ConverterUtil.convertInventoryPojotoInventoryData(i, productMasterPojo);
		assertEquals(i.getQuantity(), d.quantity);
		assertEquals(productMasterPojo.getBarcode(), d.barcode);
	}

	@Test
	public void testConvertInventoryFormtoInventoryPojo() throws ApiException {
		InventoryForm f = TestUtil.getInventoryForm();
		InventoryPojo p = ConverterUtil.convertInventoryFormtoInventoryPojo(f, TestUtil.getProductMasterPojo());
		assertEquals(f.quantity, p.getQuantity());
	}

	@Test
	public void testConvertUserPojotoAuthentication() {
		UserPojo p = TestUtil.getUserPojo();
		int id = 1;
		p.setId(id);
		Authentication token = ConverterUtil.convertUserPojotoAuthentication(p);
		String role = "";
		for (Iterator<? extends GrantedAuthority> i = token.getAuthorities().iterator(); i.hasNext();)
			role = i.next().toString();
		// Role here can be admin or standard as set by created data
		assertEquals(p.getRole(), role);
	}

	@Test
	public void testGetDateTime() {
		String datetime = ConverterUtil.getDateTime();
		// date and time
		int s1 = datetime.split(" ").length;
		// day, month and year
		int s2 = datetime.split(" ")[0].split("-").length;
		// hour and minute
		int s3 = datetime.split(" ")[1].split(":").length;
		assertEquals(2, s1);
		assertEquals(3, s2);
		assertEquals(2, s3);
	}

	@Test
	public void testConvertOrderItemPojotoOrderItemData() throws ApiException {
		OrderItemPojo i = TestUtil.getOrderItemPojo();
		ProductMasterPojo productMasterPojo = TestUtil.getProductMasterPojo();
		OrderItemData d = ConverterUtil.convertOrderItemPojotoOrderItemData(i, productMasterPojo);
		assertEquals(productMasterPojo.getName(), d.name);
		assertEquals(productMasterPojo.getBarcode(), d.barcode);
		assertEquals(i.getSellingPrice(), d.sellingPrice, 0.01);
		assertEquals(i.getQuantity(), d.quantity);
	}

	@Test
	public void testConvertOrderItemFormtoOrderItemPojo() {
		OrderItemForm f = TestUtil.getOrderItemForm();
		OrderItemPojo o = ConverterUtil.convertOrderItemFormtoOrderItemPojo(f);
		assertEquals(o.getQuantity(), f.quantity);
	}

	@Test
	public void testConvertProductFormtoProductMasterPojoUpdate() throws ApiException {
		ProductForm f = TestUtil.getProductForm();
		BrandMasterPojo b = TestUtil.getBrandMasterPojo();
		ProductMasterPojo p = ConverterUtil.convertProductFormtoProductMasterPojoUpdate(f, b);
		assertEquals(b.getId(), p.getBrand_category_id());
		assertEquals(f.name, p.getName());
		assertEquals(f.mrp, p.getMrp(), 0.01);
	}

	@Test
	public void testConvertProductMasterPojotoProductData() throws ApiException {
		ProductMasterPojo p = TestUtil.getProductMasterPojo();
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		ProductData d = ConverterUtil.convertProductMasterPojotoProductData(p, brandMasterPojo);
		assertEquals(d.brand, brandMasterPojo.getBrand());
		assertEquals(d.category, brandMasterPojo.getCategory());
		assertEquals(d.barcode, p.getBarcode());
		assertEquals(d.name, p.getName());
		assertEquals(d.mrp, p.getMrp(), 0.01);
	}

	@Test
	public void testConvertProductFormtoProductMasterPojo() throws ApiException {
		ProductForm f = TestUtil.getProductForm();
		BrandMasterPojo b = TestUtil.getBrandMasterPojo();
		ProductMasterPojo p = ConverterUtil.convertProductFormtoProductMasterPojo(f, b);
		assertEquals(b.getId(), p.getBrand_category_id());
		assertEquals(f.name, p.getName());
		assertEquals(f.mrp, p.getMrp(), 0.01);
	}

	@Test
	public void testConvertToSalesData() throws ApiException {

		OrderItemPojo orderItemPojo = TestUtil.getOrderItemPojo();
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		SalesReportData salesReportData = ConverterUtil.convertToSalesReportData(orderItemPojo, brandMasterPojo);
		assertEquals(brandMasterPojo.getBrand(), salesReportData.brand);
		assertEquals(brandMasterPojo.getCategory(), salesReportData.category);
		assertEquals(orderItemPojo.getQuantity(), salesReportData.quantity);
		assertEquals(orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice(), salesReportData.revenue, 0.01);
	}

	@Test
	public void testConvertToInventoryReportData() throws ApiException {
		InventoryPojo inventoryPojo = TestUtil.getInventoryPojo();
		BrandMasterPojo brandMasterPojo = TestUtil.getBrandMasterPojo();
		InventoryReportData inventoryReportData = ConverterUtil.convertToInventoryReportData(inventoryPojo,
				brandMasterPojo);
		assertEquals(brandMasterPojo.getBrand(), inventoryReportData.brand);
		assertEquals(brandMasterPojo.getCategory(), inventoryReportData.category);
		assertEquals(inventoryPojo.getQuantity(), inventoryReportData.quantity);
	}

	@Test
	public void testConvertProductDatatoProductDetails() throws ApiException {
		ProductData productData = TestUtil.getProductData();
		InventoryPojo inventoryPojo = TestUtil.getInventoryPojo();
		ProductDetails productDetails = ConverterUtil.convertProductDatatoProductDetails(productData, inventoryPojo);
		assertEquals(productData.barcode, productDetails.barcode);
		assertEquals(productData.brand, productDetails.brand);
		assertEquals(productData.category, productDetails.category);
		assertEquals(productData.id, productDetails.id);
		assertEquals(productData.mrp, productDetails.mrp, 0.01);
		assertEquals(productData.name, productDetails.name);
		assertEquals(inventoryPojo.getQuantity(), productDetails.availableQuantity);
	}

	@Test
	public void testConvertProductSearchFormtoProductMasterPojo() {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.name = "munch";
		productSearchForm.barcode = StringUtil.getAlphaNumericString();
		ProductMasterPojo productMasterPojo = ConverterUtil
				.convertProductSearchFormtoProductMasterPojo(productSearchForm);
		assertEquals(productSearchForm.name, productMasterPojo.getName());
		assertEquals(productSearchForm.barcode, productMasterPojo.getBarcode());

	}

	@Test
	public void testConvertOrderItemDatatoProductSearchForm() {
		OrderItemData orderItemData = TestUtil.getOrderItemData();
		ProductSearchForm productSearchForm = ConverterUtil.convertOrderItemDatatoProductSearchForm(orderItemData);
		assertEquals(orderItemData.barcode, productSearchForm.barcode);
		assertEquals(orderItemData.name, productSearchForm.name);
	}

}
