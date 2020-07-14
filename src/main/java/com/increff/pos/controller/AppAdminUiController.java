package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ui/admin")
public class AppAdminUiController extends AbstractUiController {

	// Controller which returns model and view of given name of html file

	@RequestMapping(value = "/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/order")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/orderitem")
	public ModelAndView orderitem() {
		return mav("orderitem.html");
	}

	@RequestMapping(value = "/sales-report")
	public ModelAndView salesReport() {
		return mav("salesreport.html");
	}

	@RequestMapping(value = "/brand-report")
	public ModelAndView brandReport() {
		return mav("brandreport.html");
	}

	@RequestMapping(value = "/inventory-report")
	public ModelAndView inventoryReport() {
		return mav("inventoryreport.html");
	}

	@RequestMapping(value = "/user")
	public ModelAndView admin() {
		return mav("user.html");
	}

}
