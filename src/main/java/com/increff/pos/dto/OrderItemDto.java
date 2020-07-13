package com.increff.pos.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.ProductSearchForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConverterUtil;

@Component
public class OrderItemDto {
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;

	public List<OrderItemData> get(int orderId) throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
		return orderItemPojoList.stream()
				.map(o -> ConverterUtil.convertOrderItemPojotoOrderItemData(o, productService.get(o.getProductId())))
				.collect(Collectors.toList());
	}

	public List<OrderItemData> searchOrderItem(OrderItemData orderItemData) throws ApiException {
		ProductSearchForm productSearchForm = ConverterUtil.convertOrderItemDatatoProductSearchForm(orderItemData);
		List<ProductMasterPojo> productMasterPojoList = productService.searchData(productSearchForm);
		List<Integer> productIds = productMasterPojoList.stream().map(o -> o.getId()).collect(Collectors.toList());
		List<OrderItemPojo> orderItemPojos = orderItemService.getAll();
		if (orderItemData.orderId == 0) {
			orderItemPojos = orderItemPojos.stream().filter(o -> (productIds.contains(o.getProductId())))
					.collect(Collectors.toList());
			return orderItemPojos.stream().map(
					o -> ConverterUtil.convertOrderItemPojotoOrderItemData(o, productService.get(o.getProductId())))
					.collect(Collectors.toList());
		}
		orderItemPojos = orderItemPojos.stream()
				.filter(o -> (productIds.contains(o.getProductId()) && o.getOrderId() == orderItemData.orderId))
				.collect(Collectors.toList());
		return orderItemPojos.stream()
				.map(o -> ConverterUtil.convertOrderItemPojotoOrderItemData(o, productService.get(o.getProductId())))
				.collect(Collectors.toList());
	}

}
