package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.pojo.UserPojo;

public class NormalizeUtil {

	public static void normalizeBrandForm(BrandForm brandForm) {
		brandForm.brand = StringUtil.toLowerCase(brandForm.brand);
		brandForm.category = StringUtil.toLowerCase(brandForm.category);
	}

	public static void normalizeBrandMasterPojo(BrandMasterPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
	
	public static void normalizeProductSearchForm(ProductSearchForm productSearchForm) {
		productSearchForm.name = StringUtil.toLowerCase(productSearchForm.name);
		productSearchForm.barcode = StringUtil.toLowerCase(productSearchForm.barcode);
	}

	public static void normalizeProductMasterPojo(ProductMasterPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
	}
	
	public static void normalizeUserForm(UserForm userForm) {
		userForm.setEmail(userForm.getEmail().toLowerCase().trim());
		userForm.setRole(userForm.getRole().toLowerCase().trim());
	}
	
	public static void normalizeUserPojo(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}

	
	
	
}
