package com.increff.employee.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

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

public class ConverterUtilTest {
	@Test
	public void testConvertUserFormtoUserPojo() {
		UserForm f = getUserForm();
		UserPojo p = ConverterUtil.convertUserFormtoUserPojo(f);
		assertEquals(f.getEmail(), p.getEmail());
		assertEquals(f.getPassword(), p.getPassword());
		assertEquals(f.getRole(), p.getRole());
	}

	@Test
	public void testConvertUserPojotoUserData() {
		UserPojo p = getUserPojo();
		int id = 1;
		p.setId(id);
		UserData d = ConverterUtil.convertUserPojotoUserData(p);
		assertEquals(p.getEmail(), d.getEmail());
		assertEquals(p.getPassword(), d.getPassword());
		assertEquals(p.getRole(), d.getRole());
		assertEquals(id, d.getId());
	}

	@Test
	public void testGetUserDataList() {
		List<UserPojo> list = getUserPojoList();
		List<UserData> list2 = ConverterUtil.getUserDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertBrandMasterPojotoBrandData() {
		BrandMasterPojo b = getBrandMasterPojo();
		int id = 1;
		b.setId(id);
		BrandData d = ConverterUtil.convertBrandMasterPojotoBrandData(b);
		assertEquals(b.getBrand(), d.getBrand());
		assertEquals(b.getCategory(), d.getCategory());
		assertEquals(b.getId(), d.getId());
	}

	@Test
	public void testConvertBrandFormtoBrandMasterPojo() {
		BrandForm f = getBrandForm();
		BrandMasterPojo p = ConverterUtil.convertBrandFormtoBrandMasterPojo(f);
		assertEquals(f.getBrand(), p.getBrand());
		assertEquals(f.getCategory(), p.getCategory());
	}

	@Test
	public void testGetBrandDataList() {
		List<BrandMasterPojo> list = getBrandMasterPojoList();
		List<BrandData> list2 = ConverterUtil.getBrandDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertInventoryPojotoInventoryData() {
		InventoryPojo i = getInventoryPojo();
		InventoryData d = ConverterUtil.convertInventoryPojotoInventoryData(i, i.getProductMasterPojo().getBarcode());
		assertEquals(i.getQuantity(), d.getQuantity());
		assertEquals(i.getProductMasterPojo().getBarcode(), d.getBarcode());
	}

	@Test
	public void testConvertInventoryFormtoInventoryPojo() {
		InventoryForm f = getInventoryForm();
		InventoryPojo p = ConverterUtil.convertInventoryFormtoInventoryPojo(f, getProductMasterPojo());
		assertEquals(f.getQuantity(), p.getQuantity());
	}

	@Test
	public void testGetInventoryDataList() {
		List<InventoryPojo> list = getInventoryPojoList();
		List<InventoryData> list2 = ConverterUtil.getInventoryDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertUserPojotoAuthentication() {
		UserPojo p = getUserPojo();
		int id = 1;
		p.setId(id);
		Authentication token = ConverterUtil.convertUserPojotoAuthentication(p);
		String role = "";
		for (Iterator<? extends GrantedAuthority> i = token.getAuthorities().iterator(); i.hasNext();)
			role = i.next().toString();
		assertEquals(p.getRole(), role);
	}

	@Test
	public void testGetDateTime() {
		String datetime = ConverterUtil.getDateTime();
		int s1 = datetime.split(" ").length;
		int s2 = datetime.split(" ")[0].split("-").length;
		int s3 = datetime.split(" ")[1].split(":").length;
		assertEquals(2, s1);
		assertEquals(3, s2);
		assertEquals(2, s3);
	}

	@Test
	public void testConvertOrderPojotoOrderData() {
		OrderPojo o = getOrderPojo();
		OrderData d = ConverterUtil.convertOrderPojotoOrderData(o);
		assertEquals(o.getDatetime(), d.getDatetime());
	}

	@Test
	public void testGetOrderDataList() {
		List<OrderPojo> list = getOrderPojoList();
		List<OrderData> list2 = ConverterUtil.getOrderDataList(list);
		assertEquals(list.size(), list2.size());
	}

	@Test
	public void testConvertOrderItemPojotoOrderItemData() {
		OrderItemPojo i = getOrderItemPojo();
		OrderItemData d = ConverterUtil.convertOrderItemPojotoOrderItemData(i, i.getProductMasterPojo().getBarcode());
		assertEquals(i.getProductMasterPojo().getBarcode(), d.getBarcode());
		assertEquals(i.getSellingPrice(), d.getMrp(), 0.01);
		assertEquals(i.getQuantity(), d.getQuantity());
	}

	@Test
	public void testConvertOrderItemFormtoOrderItemPojo() {
		OrderItemForm f = getOrderItemForm();
		OrderItemPojo o = ConverterUtil.convertOrderItemFormtoOrderItemPojo(f);
		assertEquals(o.getQuantity(), f.getQuantity());
	}
	
	@Test
	public void testGetOrderItemDataList() {
		List<OrderItemPojo> list = getOrderItemPojoList();
		List<OrderItemData> list2 = ConverterUtil.getOrderItemDataList(list);
		assertEquals(list.size(), list2.size());
	}
	
	@Test
	public void testConvertProductFormtoProductMasterPojoUpdate() {
		ProductForm f=getProductForm();
		BrandMasterPojo b=getBrandMasterPojo();
		int brand_category_id=1;
		ProductMasterPojo p=ConverterUtil.convertProductFormtoProductMasterPojoUpdate(f,brand_category_id, b);
		assertEquals(b.getBrand(), p.getBrand_category().getBrand());
		assertEquals(b.getCategory(), p.getBrand_category().getCategory());
		assertEquals(f.getName(), p.getName());
		assertEquals(f.getMrp(), p.getMrp(),0.01);
	}
	
	@Test
	public void testConvertProductMasterPojotoProductData() {
		ProductMasterPojo p=getProductMasterPojo();
		ProductData d=ConverterUtil.convertProductMasterPojotoProductData(p, p.getBrand_category().getBrand(),p.getBrand_category().getCategory());
		assertEquals(d.getBrand(), p.getBrand_category().getBrand());
		assertEquals(d.getCategory(), p.getBrand_category().getCategory());
		assertEquals(d.getBarcode(), p.getBarcode());
		assertEquals(d.getName(), p.getName());
		assertEquals(d.getMrp(), p.getMrp(),0.01);
	}
	
	@Test
	public void testConvertProductFormtoProductMasterPojo() {
		ProductForm f=getProductForm();
		BrandMasterPojo b=getBrandMasterPojo();
		int brand_category_id=1;
		ProductMasterPojo p=ConverterUtil.convertProductFormtoProductMasterPojo(f,brand_category_id, b);
		assertEquals(b.getBrand(), p.getBrand_category().getBrand());
		assertEquals(b.getCategory(), p.getBrand_category().getCategory());
		assertEquals(f.getName(), p.getName());
		assertEquals(f.getMrp(), p.getMrp(),0.01);
	}
	
	@Test
	public void testGetProductDataList() {
		List<ProductMasterPojo> list = getProductMasterPojoList();
		List<ProductData> list2 = ConverterUtil.getProductDataList(list);
		assertEquals(list.size(), list2.size());
	}
	
	@Test
	public void testConvertToSalesData() {
		List<OrderItemPojo> list=getOrderItemPojoList();
		List<SalesReportData> list2=ConverterUtil.convertToSalesData(list);
		int i;
		double revenue=0,selling=0;
		for(i=0;i<list.size();i++) {
			selling+=list.get(i).getQuantity()*list.get(i).getSellingPrice();
		}
		for(i=0;i<list2.size();i++) {
			assertEquals(list2.get(i).getBrand(), list.get(i).getProductMasterPojo().getBrand_category().getBrand());
			assertEquals(list2.get(i).getCategory(), list.get(i).getProductMasterPojo().getBrand_category().getCategory());
			assertEquals(list2.get(i).getQuantity(), list.get(i).getQuantity());
			revenue+=list2.get(i).getRevenue();
		}
		assertEquals(selling, revenue,0.01);
	}
	
	@Test
	public void testConvertToBrandData() {
		List<BrandMasterPojo> list=getBrandMasterPojoList();
		List<BrandData> list2=ConverterUtil.convertToBrandData(list);
		int i;
		for(i=0;i<list2.size();i++) {
			assertEquals(list2.get(i).getBrand(), list.get(i).getBrand());
			assertEquals(list2.get(i).getCategory(), list.get(i).getCategory());
			
		}
		assertEquals(list.size(), list2.size());
	}
	
	@Test
	public void testConvertToInventoryReportData() {
		List<InventoryPojo> list=getInventoryPojoList();
		List<InventoryReportData> list2=ConverterUtil.convertToInventoryReportData(list);
		int i;
		for(i=0;i<list2.size();i++) {
			assertEquals(list2.get(i).getBrand(), list.get(i).getProductMasterPojo().getBrand_category().getBrand());
			assertEquals(list2.get(i).getCategory(), list.get(i).getProductMasterPojo().getBrand_category().getCategory());
			assertEquals(list2.get(i).getQuantity(), list.get(i).getQuantity());
		}
		assertEquals(list.size(), list2.size());
	}
	private List<ProductMasterPojo> getProductMasterPojoList() {
		List<ProductMasterPojo> list = new ArrayList<ProductMasterPojo>();
		ProductMasterPojo p1 = getProductMasterPojo();
		ProductMasterPojo p2 = getProductMasterPojo();
		p1.setId(1);
		p2.setId(2);
		list.add(p1);
		list.add(p2);
		return list;
	}

	private ProductForm getProductForm() {
		ProductForm f=new ProductForm();
		BrandMasterPojo b=getBrandMasterPojo();
		String brand=b.getBrand();
		String category=b.getCategory();
		String name="munch";
		double mrp=10.50;
		f.setBrand(brand);
		f.setCategory(category);
		f.setMrp(mrp);
		f.setName(name);
		return f;
	}

	private List<OrderItemPojo> getOrderItemPojoList() {
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
		f.setMrp(mrp);
		f.setBarcode(barcode);
		f.setQuantity(quantity);
		return f;
	}

	private OrderItemPojo getOrderItemPojo() {
		OrderItemPojo i = new OrderItemPojo();
		int quantity = 10;
		double sellingPrice = 5.5;
		i.setOrderPojo(getOrderPojo());
		i.setProductMasterPojo(getProductMasterPojo());
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
		o.setDatetime(ConverterUtil.getDateTime());
		return o;
	}

	private List<InventoryPojo> getInventoryPojoList() {
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

	private InventoryPojo getInventoryPojo() {
		InventoryPojo i = new InventoryPojo();
		int quantity = 25;
		i.setProductMasterPojo(getProductMasterPojo());
		i.setQuantity(quantity);
		return i;
	}

	private ProductMasterPojo getProductMasterPojo() {
		ProductMasterPojo p = new ProductMasterPojo();
		String barcode = StringUtil.getAlphaNumericString();
		String name = "munch";
		double mrp = 10;
		BrandMasterPojo brand_category = getBrandMasterPojo();
		p.setBarcode(barcode);
		p.setBrand_category(brand_category);
		p.setName(name);
		p.setMrp(mrp);
		return p;
	}

	private List<BrandMasterPojo> getBrandMasterPojoList() {
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

	private BrandMasterPojo getBrandMasterPojo() {
		BrandMasterPojo b = new BrandMasterPojo();
		String brand = "nestle";
		String category = "dairy";
		b.setBrand(brand);
		b.setCategory(category);
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
