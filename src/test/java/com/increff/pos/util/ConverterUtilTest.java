package com.increff.pos.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

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
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.spring.AbstractUnitTest;

public class ConverterUtilTest extends AbstractUnitTest {

	// test converterUtil

	// All the tests are just testing converted data with expected one

	@Autowired
	private BrandService bService;
	@Autowired
	private ProductService pService;
	@Autowired
	private ConverterUtil converterUtil;

	@Test
	public void testConvertUserFormtoUserPojo() {
		UserForm f = getUserForm();
		UserPojo p = converterUtil.convertUserFormtoUserPojo(f);
		assertEquals(f.getEmail(), p.getEmail());
		assertEquals(f.getPassword(), p.getPassword());
		assertEquals(f.getRole(), p.getRole());
	}

	@Test
	public void testConvertUserPojotoUserData() {
		UserPojo p = getUserPojo();
		int id = 1;
		p.setId(id);
		UserData d = converterUtil.convertUserPojotoUserData(p);
		assertEquals(p.getEmail(), d.getEmail());
		assertEquals(p.getPassword(), d.getPassword());
		assertEquals(p.getRole(), d.getRole());
		assertEquals(id, d.getId());
	}

	@Test
	public void testGetUserDataList() {
		List<UserPojo> list = getUserPojoList();
		List<UserData> list2 = converterUtil.getUserDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertBrandMasterPojotoBrandData() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojo();
		int id = 1;
		b.setId(id);
		BrandData d = converterUtil.convertBrandMasterPojotoBrandData(b);
		assertEquals(b.getBrand(), d.getBrand());
		assertEquals(b.getCategory(), d.getCategory());
		assertEquals(b.getId(), d.getId());
	}

	@Test
	public void testConvertBrandFormtoBrandMasterPojo() {
		BrandForm f = getBrandForm();
		BrandMasterPojo p = converterUtil.convertBrandFormtoBrandMasterPojo(f);
		assertEquals(f.getBrand(), p.getBrand());
		assertEquals(f.getCategory(), p.getCategory());
	}

	@Test
	public void testGetBrandDataList() throws ApiException {
		List<BrandMasterPojo> list = getBrandMasterPojoList();
		List<BrandData> list2 = converterUtil.getBrandDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertInventoryPojotoInventoryData() throws ApiException {
		InventoryPojo i = getInventoryPojo();
		ProductMasterPojo productMasterPojo = getProductMasterPojo();
		InventoryData d = converterUtil.convertInventoryPojotoInventoryData(i, productMasterPojo);
		assertEquals(i.getQuantity(), d.getQuantity());
		assertEquals(productMasterPojo.getBarcode(), d.getBarcode());
	}

	@Test
	public void testConvertInventoryFormtoInventoryPojo() throws ApiException {
		InventoryForm f = getInventoryForm();
		InventoryPojo p = converterUtil.convertInventoryFormtoInventoryPojo(f, getProductMasterPojo());
		assertEquals(f.getQuantity(), p.getQuantity());
	}

	@Test
	public void testGetInventoryDataList() throws ApiException {
		List<InventoryPojo> list = getInventoryPojoList();
		List<InventoryData> list2 = converterUtil.getInventoryDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertUserPojotoAuthentication() {
		UserPojo p = getUserPojo();
		int id = 1;
		p.setId(id);
		Authentication token = converterUtil.convertUserPojotoAuthentication(p);
		String role = "";
		for (Iterator<? extends GrantedAuthority> i = token.getAuthorities().iterator(); i.hasNext();)
			role = i.next().toString();
		// Role here can be admin or standard as set by created data
		assertEquals(p.getRole(), role);
	}

	@Test
	public void testGetDateTime() {
		String datetime = converterUtil.getDateTime();
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
	public void testConvertOrderPojotoOrderData() {
		OrderPojo o = getOrderPojo();
		OrderData d = converterUtil.convertOrderPojotoOrderData(o);
		assertEquals(o.getDatetime(), d.getDatetime());
	}

	@Test
	public void testGetOrderDataList() {
		List<OrderPojo> list = getOrderPojoList();
		List<OrderData> list2 = converterUtil.getOrderDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertOrderItemPojotoOrderItemData() throws ApiException {
		OrderItemPojo i = getOrderItemPojo();
		ProductMasterPojo productMasterPojo = getProductMasterPojo();
		OrderItemData d = converterUtil.convertOrderItemPojotoOrderItemData(i, productMasterPojo);
		assertEquals(productMasterPojo.getName(), d.getName());
		assertEquals(productMasterPojo.getBarcode(), d.getBarcode());
		assertEquals(i.getSellingPrice(), d.getSellingPrice(), 0.01);
		assertEquals(i.getQuantity(), d.getQuantity());
	}

	@Test
	public void testConvertOrderItemFormtoOrderItemPojo() {
		OrderItemForm f = getOrderItemForm();
		OrderItemPojo o = converterUtil.convertOrderItemFormtoOrderItemPojo(f);
		assertEquals(o.getQuantity(), f.getQuantity());
	}

	@Test
	public void testGetOrderItemDataList() throws ApiException {
		List<OrderItemPojo> list = getOrderItemPojoList();
		List<OrderItemData> list2 = converterUtil.getOrderItemDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertProductFormtoProductMasterPojoUpdate() throws ApiException {
		ProductForm f = getProductForm();
		BrandMasterPojo b = getBrandMasterPojo();
		ProductMasterPojo p = converterUtil.convertProductFormtoProductMasterPojoUpdate(f, b);
		assertEquals(b.getId(), p.getBrand_category_id());
		assertEquals(f.getName(), p.getName());
		assertEquals(f.getMrp(), p.getMrp(), 0.01);
	}

	@Test
	public void testConvertProductMasterPojotoProductData() throws ApiException {
		ProductMasterPojo p = getProductMasterPojo();
		BrandMasterPojo brandMasterPojo = getBrandMasterPojo();
		ProductData d = converterUtil.convertProductMasterPojotoProductData(p, brandMasterPojo);
		assertEquals(d.getBrand(), brandMasterPojo.getBrand());
		assertEquals(d.getCategory(), brandMasterPojo.getCategory());
		assertEquals(d.getBarcode(), p.getBarcode());
		assertEquals(d.getName(), p.getName());
		assertEquals(d.getMrp(), p.getMrp(), 0.01);
	}

	@Test
	public void testConvertProductFormtoProductMasterPojo() throws ApiException {
		ProductForm f = getProductForm();
		BrandMasterPojo b = getBrandMasterPojo();
		ProductMasterPojo p = converterUtil.convertProductFormtoProductMasterPojo(f, b);
		assertEquals(b.getId(), p.getBrand_category_id());
		assertEquals(f.getName(), p.getName());
		assertEquals(f.getMrp(), p.getMrp(), 0.01);
	}

	@Test
	public void testGetProductDataList() throws ApiException {
		List<ProductMasterPojo> list = getProductMasterPojoList();
		List<ProductData> list2 = converterUtil.getProductDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertToSalesData() throws ApiException {
		List<OrderItemPojo> list = getOrderItemPojoList();
		List<SalesReportData> list2 = converterUtil.convertToSalesData(list);
		int i;
		double revenue = 0, selling = 0;
		for (i = 0; i < list.size(); i++) {
			selling += list.get(i).getQuantity() * list.get(i).getSellingPrice();
		}
		for (i = 0; i < list2.size(); i++) {
			assertEquals(list2.get(i).getQuantity(), list.get(i).getQuantity());
			revenue += list2.get(i).getRevenue();
		}
		assertEquals(selling, revenue, 0.01);
	}

	@Test
	public void testConvertToBrandData() throws ApiException {
		List<BrandMasterPojo> list = getBrandMasterPojoList();
		List<BrandData> list2 = converterUtil.convertToBrandData(list);
		int i;
		for (i = 0; i < list2.size(); i++) {
			assertEquals(list2.get(i).getBrand(), list.get(i).getBrand());
			assertEquals(list2.get(i).getCategory(), list.get(i).getCategory());

		}
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertToInventoryReportData() throws ApiException {
		List<InventoryPojo> list = getInventoryPojoList();
		List<InventoryReportData> list2 = converterUtil.convertToInventoryReportData(list);
		int i;
		for (i = 0; i < list2.size(); i++) {
			assertEquals(list2.get(i).getQuantity(), list.get(i).getQuantity());
		}
		assertEquals(list.size(), list2.size());
	}

	// create data for tests

	private List<ProductMasterPojo> getProductMasterPojoList() throws ApiException {
		List<ProductMasterPojo> list = new ArrayList<ProductMasterPojo>();
		ProductMasterPojo p1 = getProductMasterPojo();
		ProductMasterPojo p2 = getProductMasterPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private ProductForm getProductForm() throws ApiException {
		ProductForm f = new ProductForm();
		BrandMasterPojo b = getBrandMasterPojo();
		String brand = b.getBrand();
		String category = b.getCategory();
		String name = "munch";
		double mrp = 10.50;
		f.setBrand(brand);
		f.setCategory(category);
		f.setMrp(mrp);
		f.setName(name);
		return f;
	}

	private List<OrderItemPojo> getOrderItemPojoList() throws ApiException {
		List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		OrderItemPojo p1 = getOrderItemPojo();
		OrderItemPojo p2 = getOrderItemPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private OrderItemForm getOrderItemForm() {
		OrderItemForm f = new OrderItemForm();
		double mrp = 10.06;
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 15;
		f.setSellingPrice(mrp);
		f.setBarcode(barcode);
		f.setQuantity(quantity);
		return f;
	}

	private OrderItemPojo getOrderItemPojo() throws ApiException {
		OrderItemPojo i = new OrderItemPojo();
		int quantity = 10;
		double sellingPrice = 5.5;
		i.setOrderId(getOrderPojo().getId());
		i.setProductId(getProductMasterPojo().getId());
		i.setQuantity(quantity);
		i.setSellingPrice(sellingPrice);
		return i;
	}

	private List<OrderPojo> getOrderPojoList() {
		List<OrderPojo> list = new ArrayList<OrderPojo>();
		OrderPojo p1 = getOrderPojo();
		OrderPojo p2 = getOrderPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private OrderPojo getOrderPojo() {
		OrderPojo o = new OrderPojo();
		o.setDatetime(converterUtil.getDateTime());
		return o;
	}

	private List<InventoryPojo> getInventoryPojoList() throws ApiException {
		List<InventoryPojo> list = new ArrayList<InventoryPojo>();
		InventoryPojo p1 = getInventoryPojo();
		InventoryPojo p2 = getInventoryPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private InventoryForm getInventoryForm() {
		InventoryForm f = new InventoryForm();
		String barcode = StringUtil.getAlphaNumericString();
		int quantity = 35;
		f.setBarcode(barcode);
		f.setQuantity(quantity);
		return f;
	}

	private InventoryPojo getInventoryPojo() throws ApiException {
		InventoryPojo i = new InventoryPojo();
		int quantity = 25;
		i.setProductid(getProductMasterPojo().getId());
		i.setQuantity(quantity);
		return i;
	}

	private ProductMasterPojo getProductMasterPojo() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		String name = "munch";
		double mrp = 10;
		BrandMasterPojo brandMasterPojo = getBrandMasterPojo();
		p.setBarcode(barcode);
		p.setBrand_category_id(brandMasterPojo.getId());
		p.setName(name);
		p.setMrp(mrp);
		pService.add(p, brandMasterPojo);
		return p;
	}

	private List<BrandMasterPojo> getBrandMasterPojoList() throws ApiException {
		List<BrandMasterPojo> list = new ArrayList<BrandMasterPojo>();
		BrandMasterPojo p1 = getBrandMasterPojo();
		BrandMasterPojo p2 = getBrandMasterPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private BrandForm getBrandForm() {
		BrandForm b = new BrandForm();
		String brand = "nestle";
		String category = "dairy";
		b.setBrand(brand);
		b.setCategory(category);
		return b;
	}

	private BrandMasterPojo getBrandMasterPojo() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		String brand = StringUtil.getAlphaNumericString();
		String category = "dairy";
		b.setBrand(brand);
		b.setCategory(category);
		bService.add(b);
		return b;
	}

	private List<UserPojo> getUserPojoList() {
		List<UserPojo> list = new ArrayList<UserPojo>();
		UserPojo p1 = getUserPojo();
		UserPojo p2 = getUserPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private UserPojo getUserPojo() {
		UserPojo p = new UserPojo();
		String email = "shahviram308@gmail.com";
		String password = "admin";
		String role = "standard";
		p.setEmail(email);
		p.setPassword(password);
		p.setRole(role);
		return p;
	}

	private UserForm getUserForm() {
		UserForm f = new UserForm();
		String email = "shahviram308@gmail.com";
		String password = "admin";
		String role = "admin";
		f.setEmail(email);
		f.setPassword(password);
		f.setRole(role);
		return f;
	}
}
