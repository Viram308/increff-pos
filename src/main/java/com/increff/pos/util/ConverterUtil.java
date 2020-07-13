package com.increff.pos.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
	public static UserPojo convertUserFormtoUserPojo(UserForm f)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
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

//	// Converts list of UserPojo to list of UserData
//	public static List<UserData> getUserDataList(List<UserPojo> list) {
//		List<UserData> list2 = new ArrayList<UserData>();
//		for (UserPojo p : list) {
//			list2.add(convertUserPojotoUserData(p));
//		}
//		return list2;
//	}

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

//	// Converts list of BrandMasterPojo to list of BrandData
//	public static List<BrandData> getBrandDataList(List<BrandMasterPojo> list) {
//		List<BrandData> list2 = new ArrayList<BrandData>();
//		for (BrandMasterPojo p : list) {
//			list2.add(convertBrandMasterPojotoBrandData(p));
//		}
//		return list2;
//	}

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

//	// Converts list of OrderPojo to list of OrderData
//	public static List<OrderData> getOrderDataList(List<OrderPojo> list) {
//		List<OrderData> list2 = new ArrayList<OrderData>();
//		for (OrderPojo p : list) {
//			list2.add(convertOrderPojotoOrderData(p));
//		}
//		return list2;
//	}

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

//	// Converts list of OrderItemPojo to list of OrderItemData
//	public static List<OrderItemData> getOrderItemDataList(List<OrderItemPojo> list) {
//		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
//		for (OrderItemPojo p : list) {
//			ProductMasterPojo productMasterPojo = productService.get(p.getProductId());
//			list2.add(convertOrderItemPojotoOrderItemData(p, productMasterPojo));
//		}
//		return list2;
//	}

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

//	// Converts list of ProductMasterPojo to list of ProductData
//	public static List<ProductData> getProductDataList(List<ProductMasterPojo> list,List<BrandMasterPojo> brandMasterPojos) {
////		List<ProductData> list2 = new ArrayList<ProductData>();
////		for (ProductMasterPojo p : list) {
////			BrandMasterPojo brandMasterPojo = brandService.get(p.getBrand_category_id());
////			list2.add(convertProductMasterPojotoProductData(p, brandMasterPojo));
////		}
//		return ;
//	}

//	public static List<SalesReportData> convertToSalesData(List<OrderItemPojo> listOfOrderItemPojo) {
//		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
//		int i;
//		// Converts OrderItemPojo to SalesReportData
//		for (i = 0; i < listOfOrderItemPojo.size(); i++) {
//			SalesReportData salesProductData = new SalesReportData();
//			ProductMasterPojo p = productService.get(listOfOrderItemPojo.get(i).getProductId());
//			BrandMasterPojo b = brandService.get(p.getBrand_category_id());
//			salesProductData.brand = b.getBrand();
//			salesProductData.category = b.getCategory();
//			salesProductData.quantity = listOfOrderItemPojo.get(i).getQuantity();
//			salesProductData.revenue = listOfOrderItemPojo.get(i).getQuantity()
//					* listOfOrderItemPojo.get(i).getSellingPrice();
//			salesReportData.add(salesProductData);
//		}
//		return salesReportData;
//	}

//	public static List<BrandData> convertToBrandData(List<BrandMasterPojo> list) {
//		List<BrandData> list2 = new ArrayList<BrandData>();
//		int i = 0;
//		// Converts BrandMasterPojo to BrandData
//		for (i = 0; i < list.size(); i++) {
//			BrandData b = new BrandData();
//			b.id = i + 1;
//			b.brand = list.get(i).getBrand();
//			b.category = list.get(i).getCategory();
//			list2.add(b);
//		}
//		return list2;
//	}

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

//	public static List<OrderItemPojo> getOrderItemObject(List<OrderItemForm> orderItemList, OrderPojo op) throws ApiException {
//		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
//		int orderId = op.getId();
//		// Convert OrderItemForm to OrderItemPojo
//		for (OrderItemForm o : orderItemList) {
//			ProductMasterPojo productMasterPojo = productService.getByBarcode(o.barcode);
//			OrderItemPojo item = new OrderItemPojo();
//			item.setOrderId(orderId);
//			item.setProductId(productMasterPojo.getId());
//			item.setQuantity(o.quantity);
//			item.setSellingPrice(o.sellingPrice);
//			list.add(item);
//		}
//		return list;
//	}

}
