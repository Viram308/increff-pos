package com.increff.pos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.InventorySearchForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductDetails;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.model.UserPrincipal;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;

public class ConverterUtil {

	// Converts UserForm to UserPojo
	public static UserPojo convertUserFormtoUserPojo(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setRole(f.getRole());
		return p;
	}

	// Converts UserPojo to UserData
	public static UserData convertUserPojotoUserData(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}



	// Converts BrandMasterPojo to BrandData
	public static BrandData convertBrandMasterPojotoBrandData(BrandMasterPojo p) {
		BrandData d = new BrandData();
		d.category = p.getCategory();
		d.brand = p.getBrand();
		d.id = p.getId();
		return d;
	}

	// Converts BrandForm to BrandMasterPojo
	public static BrandMasterPojo convertBrandFormtoBrandMasterPojo(BrandForm f) {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setCategory(f.category);
		b.setBrand(f.brand);
		return b;
	}



	// Converts InventoryPojo to InventoryData
	public static InventoryData convertInventoryPojotoInventoryData(InventoryPojo i,
			ProductMasterPojo productMasterPojo) {
		InventoryData d = new InventoryData();
		d.id = i.getId();
		d.name = productMasterPojo.getName();
		d.barcode = productMasterPojo.getBarcode();
		d.quantity = i.getQuantity();
		return d;
	}

	// Converts InventoryForm to InventoryPojo
	public static InventoryPojo convertInventoryFormtoInventoryPojo(InventoryForm f, ProductMasterPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductId(p.getId());
		i.setQuantity(f.quantity);
		return i;
	}

	// Converts UserPojo to Authentication
	public static Authentication convertUserPojotoAuthentication(UserPojo p) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.email = p.getEmail();
		principal.id = p.getId();
		principal.role = p.getRole();
		// Create Authorities
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(p.getRole()));
		// you can add more roles if required

		// Create Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}

	// Returns date time in required format
	public static String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	// Converts OrderPojo to OrderData
	public static OrderData convertOrderPojotoOrderData(OrderPojo p, List<OrderItemPojo> orderItemPojos) {
		OrderData d = new OrderData();
		d.id = p.getId();
		d.datetime = p.getDatetime();
		d.orderCreater = p.getOrderCreater();
		double billAmount = 0;
		for (OrderItemPojo orderItemPojo : orderItemPojos) {
			billAmount += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
		}
		d.billAmount = billAmount;
		return d;
	}



	// Converts OrderItemPojo to OrderItemData
	public static OrderItemData convertOrderItemPojotoOrderItemData(OrderItemPojo orderItemPojo,
			ProductMasterPojo productMasterPojo) {
		OrderItemData d = new OrderItemData();
		d.id = orderItemPojo.getId();
		d.orderId = orderItemPojo.getOrderId();
		d.name = productMasterPojo.getName();
		d.barcode = productMasterPojo.getBarcode();
		d.quantity = orderItemPojo.getQuantity();
		d.sellingPrice = orderItemPojo.getSellingPrice();
		return d;
	}

	// Converts OrderItemForm to OrderItemPojo
	public static OrderItemPojo convertOrderItemFormtoOrderItemPojo(OrderItemForm orderItemForm) {
		OrderItemPojo orderItemPojo = new OrderItemPojo();
		orderItemPojo.setQuantity(orderItemForm.quantity);
		return orderItemPojo;
	}

	// Converts ProductForm to ProductMasterPojo during update operation
	public static ProductMasterPojo convertProductFormtoProductMasterPojoUpdate(ProductForm f,
			BrandMasterPojo brandMasterPojo) {
		ProductMasterPojo p = new ProductMasterPojo();
		p.setBrand_category_id(brandMasterPojo.getId());
		p.setName(f.name);
		p.setMrp(f.mrp);
		return p;
	}

	// Converts ProductMasterPojo to ProductData
	public static ProductData convertProductMasterPojotoProductData(ProductMasterPojo p,
			BrandMasterPojo brandMasterPojo) {
		ProductData d = new ProductData();
		d.brand = brandMasterPojo.getBrand();
		d.category = brandMasterPojo.getCategory();
		d.id = p.getId();
		d.name = p.getName();
		d.mrp = p.getMrp();
		d.barcode = p.getBarcode();
		return d;
	}

	// Converts ProductForm to ProductMasterPojo during insert operation
	public static ProductMasterPojo convertProductFormtoProductMasterPojo(ProductForm f,
			BrandMasterPojo brandMasterPojo) {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		p.setBarcode(barcode);
		p.setBrand_category_id(brandMasterPojo.getId());
		p.setName(f.name);
		p.setMrp(f.mrp);
		return p;
	}


	public static InventoryReportData convertToInventoryReportData(InventoryPojo inventoryPojo,
			BrandMasterPojo brandMasterPojo) {
		InventoryReportData inventoryReportData = new InventoryReportData();
		inventoryReportData.brand = brandMasterPojo.getBrand();
		inventoryReportData.category = brandMasterPojo.getCategory();
		inventoryReportData.quantity = inventoryPojo.getQuantity();
		return inventoryReportData;
	}

	public static SalesReportData convertToSalesReportData(OrderItemPojo orderItemPojo,
			BrandMasterPojo brandMasterPojo) {
		SalesReportData salesProductData = new SalesReportData();
		salesProductData.brand = brandMasterPojo.getBrand();
		salesProductData.category = brandMasterPojo.getCategory();
		salesProductData.quantity = orderItemPojo.getQuantity();
		salesProductData.revenue = orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
		return salesProductData;
	}

	public static ProductMasterPojo convertProductSearchFormtoProductMasterPojo(ProductSearchForm form) {
		ProductMasterPojo productMasterPojo = new ProductMasterPojo();
		productMasterPojo.setBarcode(form.barcode);
		productMasterPojo.setName(form.name);
		return productMasterPojo;
	}

	public static ProductDetails convertProductDatatoProductDetails(ProductData productData,
			InventoryPojo inventoryPojo) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.barcode = productData.barcode;
		productDetails.brand = productData.brand;
		productDetails.availableQuantity = inventoryPojo.getQuantity();
		productDetails.category = productData.category;
		productDetails.mrp = productData.mrp;
		productDetails.name = productData.name;
		productDetails.id = productData.id;
		return productDetails;
	}

	public static BrandForm convertProductSearchFormtoBrandForm(ProductSearchForm form) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = form.brand;
		brandForm.category = form.category;
		return brandForm;
	}

	public static BrandForm convertProductFormtoBrandForm(ProductForm form) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = form.brand;
		brandForm.category = form.category;
		return brandForm;
	}

	public static ProductSearchForm convertInventorySearchFormtoProductSearchForm(InventorySearchForm form) {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = form.barcode;
		productSearchForm.name = form.name;
		return productSearchForm;
	}

	public static ProductSearchForm convertOrderItemDatatoProductSearchForm(OrderItemData orderItemData) {
		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.barcode = orderItemData.barcode;
		productSearchForm.name = orderItemData.name;
		return productSearchForm;
	}

	public static OrderItemPojo convertOrderItemFormToOrderItemPojo(OrderItemForm orderItemForm, OrderPojo orderPojo,
			ProductMasterPojo productMasterPojo) {
		OrderItemPojo item = new OrderItemPojo();
		item.setOrderId(orderPojo.getId());
		item.setProductId(productMasterPojo.getId());
		item.setQuantity(orderItemForm.quantity);
		item.setSellingPrice(orderItemForm.sellingPrice);
		return item;
	}

	public static BrandForm convertSalesReportFormtoBrandForm(SalesReportForm salesReportForm) {
		BrandForm brandForm = new BrandForm();
		brandForm.brand = salesReportForm.brand;
		brandForm.category = salesReportForm.category;
		return brandForm;
	}

}
