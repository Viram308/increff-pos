package com.increff.pos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.OrderSearchForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;

public class TestUtil {

	// functions for creating data
	
	public static OrderItemData getOrderItemData() {
		OrderItemData orderItemData = new OrderItemData();
		orderItemData.barcode = StringUtil.getAlphaNumericString();
		orderItemData.name = "increff";
		return orderItemData;
	}

	public static ProductData getProductData() {
		ProductData productData = new ProductData();
		String barcode = StringUtil.getAlphaNumericString();
		String brand = "increff";
		String category = "pos";
		int id = 1;
		double mrp = 10.50;
		String name = "munch";
		productData.barcode = barcode;
		productData.brand = brand;
		productData.category = category;
		productData.id = id;
		productData.mrp = mrp;
		productData.name = name;
		return productData;
	}

	public static ProductForm getProductForm() throws ApiException {
		ProductForm f = new ProductForm();
		BrandMasterPojo b = getBrandMasterPojo();
		String brand = b.getBrand();
		String category = b.getCategory();
		String name = "munch";
		double mrp = 10.50;
		f.brand = brand;
		f.category = category;
		f.mrp = mrp;
		f.name = name;
		return f;
	}

	public static OrderItemForm getOrderItemForm() {
		OrderItemForm f = new OrderItemForm();
		double mrp = 10.06;
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 15;
		f.sellingPrice = mrp;
		f.barcode = barcode;
		f.quantity = quantity;
		return f;
	}

	public static OrderItemPojo getOrderItemPojo() throws ApiException {
		OrderItemPojo i = new OrderItemPojo();
		int quantity = 10;
		double sellingPrice = 5.5;
		i.setOrderId(getOrderPojo().getId());
		i.setProductId(getProductMasterPojo().getId());
		i.setQuantity(quantity);
		i.setSellingPrice(sellingPrice);
		return i;
	}

	public static OrderPojo getOrderPojo() {
		OrderPojo o = new OrderPojo();
		o.setDatetime(getDateTime());
		return o;
	}

	public static InventoryForm getInventoryForm() {
		InventoryForm f = new InventoryForm();
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 35;
		f.barcode = barcode;
		f.quantity = quantity;
		return f;
	}

	public static InventoryPojo getInventoryPojo() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int quantity = 25;
		i.setProductId(getProductMasterPojo().getId());
		i.setQuantity(quantity);
		return i;
	}

	public static ProductMasterPojo getProductMasterPojo() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		String name = "munch";
		double mrp = 10;
		BrandMasterPojo brandMasterPojo = getBrandMasterPojo();
		p.setBarcode(barcode);
		p.setBrand_category_id(brandMasterPojo.getId());
		p.setName(name);
		p.setMrp(mrp);
		return p;
	}

	public static BrandForm getBrandForm() {
		BrandForm b = new BrandForm();
		String brand = "nestle";
		String category = "dairy";
		b.brand = brand;
		b.category = category;
		return b;
	}

	public static BrandMasterPojo getBrandMasterPojo() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		String brand = "nestlE";
		String category = "dairy";
		b.setBrand(brand);
		b.setCategory(category);
		return b;
	}

	public static UserPojo getUserPojo() {
		UserPojo p = new UserPojo();
		String email = "shahviram308@gmail.com";
		String password = "admin";
		String role = "standard";
		p.setEmail(email);
		p.setPassword(password);
		p.setRole(role);
		return p;
	}

	public static UserForm getUserForm() {
		UserForm f = new UserForm();
		String email = "shahviram308@gmail.com";
		String password = "admin";
		String role = "admin";
		f.setEmail(email);
		f.setPassword(password);
		f.setRole(role);
		return f;
	}

	public static ProductSearchForm getProductSearchForm() {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = StringUtil.getAlphaNumericString();
		productSearchForm.name = " MUNch       ";
		return productSearchForm;
	}

	public static String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	// create order data using dates
	public static List<OrderPojo> getOrderPojoListForSalesReport() throws ApiException {
		List<OrderPojo> list = new ArrayList<OrderPojo>();
		String[] dates = { "03-02-2020", "01-01-2020", "02-02-2019", "03-05-2020" };
		int i;
		for (i = 0; i < dates.length; i++) {
			OrderPojo o = new OrderPojo();
			o.setDatetime(dates[i]);
			list.add(o);
		}
		return list;
	}

	// create sales report data using brand,category,quantity and revenue
	public static List<SalesReportData> getSalesData() {
		List<SalesReportData> list = new ArrayList<SalesReportData>();
		String[] brand = { "viram", "increff", "nextscm", "increff", "viram" };
		String[] category = { "shah", "pos", "pvt", "shah", "shah" };
		int[] quantity = { 10, 15, 5, 20, 30 };
		double[] revenue = { 100.50, 150, 100, 400, 3000 };
		int i;
		for (i = 0; i < 5; i++) {
			SalesReportData s = new SalesReportData();
			s.brand = brand[i];
			s.category = category[i];
			s.quantity = quantity[i];
			s.revenue = revenue[i];
			list.add(s);
		}
		return list;
	}

	// create inventory report data using brand,category and quantity
	public static List<InventoryReportData> getInventoryData() {
		List<InventoryReportData> list = new ArrayList<InventoryReportData>();
		String[] brand = { "viram", "increff", "nextscm", "increff", "viram" };
		String[] category = { "shah", "pos", "pvt", "shah", "shah" };
		int[] quantity = { 10, 15, 5, 20, 30 };
		int i;
		for (i = 0; i < 5; i++) {
			InventoryReportData invData = new InventoryReportData();
			invData.brand = brand[i];
			invData.category = category[i];
			invData.quantity = quantity[i];
			list.add(invData);
		}
		return list;
	}

	public static UserForm getUserSearchForm(String email, String password, String role) {
		UserForm userForm = new UserForm();
		userForm.setEmail(email);
		userForm.setPassword(password);
		userForm.setRole(role);
		return userForm;
	}

	public static List<OrderPojo> getOrderPojoList() {
		OrderPojo orderPojo1 = new OrderPojo();
		OrderPojo orderPojo2 = new OrderPojo();
		orderPojo1.setDatetime("01-07-2020 09:45");
		orderPojo2.setDatetime("07-07-2020 09:45");
		List<OrderPojo> orderPojos = new ArrayList<OrderPojo>();
		orderPojos.add(orderPojo1);
		orderPojos.add(orderPojo2);
		return orderPojos;
	}

	public static BrandForm getBrandFormDto(String brand, String category) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = brand;
		brandForm.category = category;
		return brandForm;
	}

	public static InventorySearchForm getInventorySearchFormDto(String barcode, String name) {
		InventorySearchForm inventorySearchForm = new InventorySearchForm();
		inventorySearchForm.barcode = barcode;
		inventorySearchForm.name = name;
		return inventorySearchForm;
	}

	public static InventoryForm getInventoryFormDto(String barcode, int quantity) {
		InventoryForm inventoryForm = new InventoryForm();
		inventoryForm.barcode = barcode;
		inventoryForm.quantity = quantity;
		return inventoryForm;
	}

	public static ProductSearchForm getProductSearchFormDto(String barcode, String brand, String category,
			String name) {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = barcode;
		productSearchForm.brand = brand;
		productSearchForm.category = category;
		productSearchForm.name = name;
		return productSearchForm;
	}

	public static ProductForm getProductFormDto(String brand, String category, String name, double mrp) {
		ProductForm productForm = new ProductForm();
		productForm.brand = brand;
		productForm.category = category;
		productForm.name = name;
		productForm.mrp = mrp;
		return productForm;
	}

	public static List<OrderItemData> getOrderItemDataListDto(ProductData productData1, ProductData productData2,
			int quantity1, int quantity2) {
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

	public static OrderSearchForm getOrderSearchFormDto() {
		OrderSearchForm orderSearchForm = new OrderSearchForm();
		orderSearchForm.startdate = ConverterUtil.getDateTime().split(" ")[0];
		orderSearchForm.enddate = ConverterUtil.getDateTime().split(" ")[0];
		orderSearchForm.orderId = 0;
		orderSearchForm.orderCreater = "";
		return orderSearchForm;
	}

	public static OrderItemForm[] getOrderItemFormArrayDto(String barcode1, String barcode2, String name1, String name2,
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

	public static SalesReportForm getSalesReportFormDto(String startdate, String enddate, String brand,
			String category) {
		SalesReportForm salesReportForm = new SalesReportForm();
		salesReportForm.startdate = startdate;
		salesReportForm.enddate = enddate;
		salesReportForm.brand = brand;
		salesReportForm.category = category;
		return salesReportForm;
	}
	
	public static UserForm getUserFormDto(String email, String password, String role) {
		UserForm userForm = new UserForm();
		userForm.setEmail(email);
		userForm.setPassword(password);
		userForm.setRole(role);
		return userForm;
	}

	public static UserPojo getUserPojoDto(String email, String password, String role) {
		UserPojo u = new UserPojo();
		u.setEmail(email);
		u.setPassword(password);
		u.setRole(role);
		return u;
	}


}
