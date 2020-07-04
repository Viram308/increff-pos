package com.increff.pos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.model.UserPrincipal;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

@Component
public class ConverterUtil {

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	// Converts UserForm to UserPojo
	public UserPojo convertUserFormtoUserPojo(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setRole(f.getRole());
		p.setPassword(f.getPassword());
		return p;
	}

	// Converts UserPojo to UserData
	public UserData convertUserPojotoUserData(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		d.setPassword(p.getPassword());
		return d;
	}

	// Converts list of UserPojo to list of UserData
	public List<UserData> getUserDataList(List<UserPojo> list) {
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convertUserPojotoUserData(p));
		}
		return list2;
	}

	// Converts BrandMasterPojo to BrandData
	public BrandData convertBrandMasterPojotoBrandData(BrandMasterPojo p) {
		BrandData d = new BrandData();
		d.setCategory(p.getCategory());
		d.setBrand(p.getBrand());
		d.setId(p.getId());
		return d;
	}

	// Converts BrandForm to BrandMasterPojo
	public BrandMasterPojo convertBrandFormtoBrandMasterPojo(BrandForm f) {
		BrandMasterPojo b = new BrandMasterPojo();
		b.setCategory(f.getCategory());
		b.setBrand(f.getBrand());
		return b;
	}

	// Converts list of BrandMasterPojo to list of BrandData
	public List<BrandData> getBrandDataList(List<BrandMasterPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandMasterPojo p : list) {
			list2.add(convertBrandMasterPojotoBrandData(p));
		}
		return list2;
	}

	// Converts InventoryPojo to InventoryData
	public InventoryData convertInventoryPojotoInventoryData(InventoryPojo i, ProductMasterPojo productMasterPojo) {
		InventoryData d = new InventoryData();
		d.setId(i.getId());
		d.setName(productMasterPojo.getName());
		d.setBarcode(productMasterPojo.getBarcode());
		d.setQuantity(i.getQuantity());
		return d;
	}

	// Converts InventoryForm to InventoryPojo
	public InventoryPojo convertInventoryFormtoInventoryPojo(InventoryForm f, ProductMasterPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductid(p.getId());
		i.setQuantity(f.getQuantity());
		return i;
	}

	// Converts list of InventoryPojo to list of InventoryData
	public List<InventoryData> getInventoryDataList(List<InventoryPojo> list) {
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo inventoryPojo : list) {
			ProductMasterPojo productMasterPojo = productService.get(inventoryPojo.getProductid());
			list2.add(convertInventoryPojotoInventoryData(inventoryPojo, productMasterPojo));
		}
		return list2;
	}

	// Converts UserPojo to Authentication
	public Authentication convertUserPojotoAuthentication(UserPojo p) {
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
	public String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	// Converts OrderPojo to OrderData
	public OrderData convertOrderPojotoOrderData(OrderPojo p) {
		OrderData d = new OrderData();
		d.setId(p.getId());
		d.setDatetime(p.getDatetime());
		return d;
	}

	// Converts list of OrderPojo to list of OrderData
	public List<OrderData> getOrderDataList(List<OrderPojo> list) {
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(convertOrderPojotoOrderData(p));
		}
		return list2;
	}

	// Converts OrderItemPojo to OrderItemData
	public OrderItemData convertOrderItemPojotoOrderItemData(OrderItemPojo p, String barcode) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setOrderId(p.getOrderId());
		d.setBarcode(barcode);
		d.setQuantity(p.getQuantity());
		d.setSellingPrice(p.getSellingPrice());
		return d;
	}

	// Converts OrderItemForm to OrderItemPojo
	public OrderItemPojo convertOrderItemFormtoOrderItemPojo(OrderItemForm f) {
		OrderItemPojo i = new OrderItemPojo();
		i.setQuantity(f.getQuantity());
		return i;
	}

	// Converts list of OrderItemPojo to list of OrderItemData
	public List<OrderItemData> getOrderItemDataList(List<OrderItemPojo> list) {
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			ProductMasterPojo productMasterPojo = productService.get(p.getProductId());
			list2.add(convertOrderItemPojotoOrderItemData(p, productMasterPojo.getBarcode()));
		}
		return list2;
	}

	// Converts ProductForm to ProductMasterPojo during update operation
	public ProductMasterPojo convertProductFormtoProductMasterPojoUpdate(ProductForm f,
			BrandMasterPojo brandMasterPojo) {
		ProductMasterPojo p = new ProductMasterPojo();
		p.setBrand_category_id(brandMasterPojo.getId());
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

	// Converts ProductMasterPojo to ProductData
	public ProductData convertProductMasterPojotoProductData(ProductMasterPojo p, BrandMasterPojo brandMasterPojo) {
		ProductData d = new ProductData();
		d.setBrand(brandMasterPojo.getBrand());
		d.setCategory(brandMasterPojo.getCategory());
		d.setId(p.getId());
		d.setName(p.getName());
		d.setMrp(p.getMrp());
		d.setBarcode(p.getBarcode());
		return d;
	}

	// Converts ProductForm to ProductMasterPojo during insert operation
	public ProductMasterPojo convertProductFormtoProductMasterPojo(ProductForm f, BrandMasterPojo brandMasterPojo) {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		p.setBarcode(barcode);
		p.setBrand_category_id(brandMasterPojo.getId());
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}

	// Converts list of ProductMasterPojo to list of ProductData
	public List<ProductData> getProductDataList(List<ProductMasterPojo> list) {
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductMasterPojo p : list) {
			BrandMasterPojo brandMasterPojo = brandService.get(p.getBrand_category_id());
			list2.add(convertProductMasterPojotoProductData(p, brandMasterPojo));
		}
		return list2;
	}

	public List<SalesReportData> convertToSalesData(List<OrderItemPojo> listOfOrderItemPojo) {
		List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
		int i;
		// Converts OrderItemPojo to SalesReportData
		for (i = 0; i < listOfOrderItemPojo.size(); i++) {
			SalesReportData salesProductData = new SalesReportData();
			ProductMasterPojo p = productService.get(listOfOrderItemPojo.get(i).getProductId());
			BrandMasterPojo b = brandService.get(p.getBrand_category_id());
			salesProductData.setBrand(b.getBrand());
			salesProductData.setCategory(b.getCategory());
			salesProductData.setQuantity(listOfOrderItemPojo.get(i).getQuantity());
			salesProductData.setRevenue(
					listOfOrderItemPojo.get(i).getQuantity() * listOfOrderItemPojo.get(i).getSellingPrice());
			salesReportData.add(salesProductData);
		}
		return salesReportData;
	}

	public List<BrandData> convertToBrandData(List<BrandMasterPojo> list) {
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

	public List<InventoryReportData> convertToInventoryReportData(List<InventoryPojo> ip) {
		List<InventoryReportData> list2 = new ArrayList<InventoryReportData>();
		int i;
		// Converts InventoryPojo to InventoryReportData
		for (i = 0; i < ip.size(); i++) {
			ProductMasterPojo p = productService.get(ip.get(i).getProductid());
			BrandMasterPojo b = brandService.get(p.getBrand_category_id());
			InventoryReportData ir = new InventoryReportData();
			ir.setBrand(b.getBrand());
			ir.setCategory(b.getCategory());
			ir.setQuantity(ip.get(i).getQuantity());
			list2.add(ir);
		}
		return list2;
	}

	public ProductMasterPojo convertProductSearchFormtoProductMasterPojo(ProductSearchForm form) {
		ProductMasterPojo productMasterPojo = new ProductMasterPojo();
		productMasterPojo.setBarcode(form.getBarcode());
		productMasterPojo.setName(form.getName());
		return productMasterPojo;
	}
}
