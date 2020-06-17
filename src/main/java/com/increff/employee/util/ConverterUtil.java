package com.increff.employee.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.UserData;
import com.increff.employee.model.UserForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;

public class ConverterUtil {

	// Converts UserForm to UserPojo
	public static UserPojo convertUserFormtoUserPojo(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setRole(f.getRole());
		p.setPassword(f.getPassword());
		return p;
	}

	// Converts UserPojo to UserData
	public static UserData convertUserPojotoUserData(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		d.setPassword(p.getPassword());
		return d;
	}

	// Converts list of UserPojo to list of UserData
	public static List<UserData> getUserDataList(List<UserPojo> list) {
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convertUserPojotoUserData(p));
		}
		return list2;
	}

	// Converts BrandMasterPojo to BrandData
	public static BrandData convertBrandMasterPojotoBrandData(BrandMasterPojo p) {
		BrandData d = new BrandData();
		d.setCategory(p.getCategory());
		d.setBrand(p.getBrand());
		d.setId(p.getId());
		return d;
	}

	// Converts BrandForm to BrandMasterPojo
	public static BrandMasterPojo convertBrandFormtoBrandMasterPojo(BrandForm f) {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setCategory(f.getCategory());
		b.setBrand(f.getBrand());
		return b;
	}

	// Converts list of BrandMasterPojo to list of BrandData
	public static List<BrandData> getBrandDataList(List<BrandMasterPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandMasterPojo p : list) {
			list2.add(convertBrandMasterPojotoBrandData(p));
		}
		return list2;
	}

	// Converts InventoryPojo to InventoryData
	public static InventoryData convertInventoryPojotoInventoryData(InventoryPojo i, String barcode) {
		InventoryData d = new InventoryData();
		d.setId(i.getId());
		d.setBarcode(barcode);
		d.setQuantity(i.getQuantity());
		return d;
	}

	// Converts InventoryForm to InventoryPojo
	public static InventoryPojo convertInventoryFormtoInventoryPojo(InventoryForm f, ProductMasterPojo p)
			throws ApiException {
		InventoryPojo i = new InventoryPojo();
		i.setProductMasterPojo(p);
		i.setQuantity(f.getQuantity());
		return i;
	}

	// Converts list of InventoryPojo to list of InventoryData
	public static List<InventoryData> getInventoryDataList(List<InventoryPojo> list) {
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo i : list) {
			list2.add(convertInventoryPojotoInventoryData(i, i.getProductMasterPojo().getBarcode()));
		}
		return list2;
	}

	// Converts UserPojo to Authentication
	public static Authentication convertUserPojotoAuthentication(UserPojo p) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(p.getEmail());
		principal.setId(p.getId());
		principal.setRole(p.getRole());
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
	public static OrderData convertOrderPojotoOrderData(OrderPojo p) {
		OrderData d = new OrderData();
		d.setId(p.getId());
		d.setDatetime(p.getDatetime());
		return d;
	}

	// Converts list of OrderPojo to list of OrderData
	public static List<OrderData> getOrderDataList(List<OrderPojo> list) {
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(convertOrderPojotoOrderData(p));
		}
		return list2;
	}

	// Converts OrderItemPojo to OrderItemData
	public static OrderItemData convertOrderItemPojotoOrderItemData(OrderItemPojo p, String barcode) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setOrderId(p.getOrderPojo().getId());
		d.setBarcode(barcode);
		d.setQuantity(p.getQuantity());
		d.setMrp(p.getSellingPrice());
		return d;
	}

	// Converts OrderItemForm to OrderItemPojo
	public static OrderItemPojo convertOrderItemFormtoOrderItemPojo(OrderItemForm f) {
		OrderItemPojo i = new OrderItemPojo();
		i.setQuantity(f.getQuantity());
		return i;
	}

	// Converts list of OrderItemPojo to list of OrderItemData
	public static List<OrderItemData> getOrderItemDataList(List<OrderItemPojo> list) {
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			list2.add(convertOrderItemPojotoOrderItemData(p, p.getProductMasterPojo().getBarcode()));
		}
		return list2;
	}

	// Converts ProductForm to ProductMasterPojo during update operation
	public static ProductMasterPojo convertProductFormtoProductMasterPojoUpdate(ProductForm f, int brand_category_id,
			BrandMasterPojo brandMasterPojo) throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		p.setBrand_category(brandMasterPojo);
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

	// Converts ProductMasterPojo to ProductData
	public static ProductData convertProductMasterPojotoProductData(ProductMasterPojo p, String brand,
			String category) {
		ProductData d = new ProductData();
		d.setBrand(brand);
		d.setCategory(category);
		d.setId(p.getId());
		d.setName(p.getName());
		d.setMrp(p.getMrp());
		d.setBarcode(p.getBarcode());
		return d;
	}

	// Converts ProductForm to ProductMasterPojo during insert operation
	public static ProductMasterPojo convertProductFormtoProductMasterPojo(ProductForm f, int brand_category_id,
			BrandMasterPojo brandMasterPojo) throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		p.setBarcode(barcode);
		p.setBrand_category(brandMasterPojo);
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

	// Converts list of ProductMasterPojo to list of ProductData
	public static List<ProductData> getProductDataList(List<ProductMasterPojo> list) {
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductMasterPojo p : list) {
			list2.add(convertProductMasterPojotoProductData(p, p.getBrand_category().getBrand(),
					p.getBrand_category().getCategory()));
		}
		return list2;
	}

	public static List<SalesReportData> convertToSalesData(List<OrderItemPojo> listOfOrderItemPojo) {
		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
		int i;
		// Converts OrderItemPojo to SalesReportData
		for (i = 0; i < listOfOrderItemPojo.size(); i++) {
			SalesReportData salesProductData = new SalesReportData();
			ProductMasterPojo p = listOfOrderItemPojo.get(i).getProductMasterPojo();
			BrandMasterPojo b = p.getBrand_category();
			salesProductData.setBrand(b.getBrand());
			salesProductData.setCategory(b.getCategory());
			salesProductData.setQuantity(listOfOrderItemPojo.get(i).getQuantity());
			salesProductData.setRevenue(
					listOfOrderItemPojo.get(i).getQuantity() * listOfOrderItemPojo.get(i).getSellingPrice());
			salesReportData.add(salesProductData);
		}
		return salesReportData;
	}

	public static List<BrandData> convertToBrandData(List<BrandMasterPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		int i = 0;
		// Converts BrandMasterPojo to BrandData
		for (i = 0; i < list.size(); i++) {
			BrandData b = new BrandData();
			b.setId(i + 1);
			b.setBrand(list.get(i).getBrand());
			b.setCategory(list.get(i).getCategory());
			list2.add(b);
		}
		return list2;
	}

	public static List<InventoryReportData> convertToInventoryReportData(List<InventoryPojo> ip) {
		List<InventoryReportData> list2 = new ArrayList<InventoryReportData>();
		int i;
		// Converts InventoryPojo to InventoryReportData
		for (i = 0; i < ip.size(); i++) {
			ProductMasterPojo p = ip.get(i).getProductMasterPojo();
			BrandMasterPojo b = p.getBrand_category();
			InventoryReportData ir = new InventoryReportData();
			ir.setBrand(b.getBrand());
			ir.setCategory(b.getCategory());
			ir.setQuantity(ip.get(i).getQuantity());
			list2.add(ir);
		}
		return list2;
	}
}
