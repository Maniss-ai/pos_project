package com.increff.employee.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.data.InfoData;

@Controller
public class UIController {

	@Value("${app.baseUrl}")
	private String baseUrl;

	@RequestMapping(value = "")
	public String index() {
		return "index.html";
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/order/order-item")
	public ModelAndView placeOrder() {
		return mav("place_order.html");
	}

	@RequestMapping(value = "/ui/order/view-order")
	public ModelAndView viewOrder() {
		return mav("view_order.html");
	}

	@RequestMapping(value = "/ui/report")
	public ModelAndView report() {
		return mav("report.html");
	}

	private ModelAndView mav(String page) {
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("info", new InfoData());
		mav.addObject("baseUrl", baseUrl);
		return mav;
	}

}
