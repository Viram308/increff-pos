package com.increff.pos.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
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
		// map OrderItemPojo to OrderItemData
		return orderItemPojoList.stream()
				.map(o -> ConverterUtil.convertOrderItemPojotoOrderItemData(o, productService.get(o.getProductId())))
				.collect(Collectors.toList());
	}

}
