package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.OrderPojo;

@Component
public class OrderDto {
	@Autowired
	private ModelMapper modelMapper;

	// Converts OrderPojo to OrderData
	public OrderData convertOrderPojotoOrderData(OrderPojo orderPojo) {
		return modelMapper.map(orderPojo, OrderData.class);
	}

	// Converts list of OrderPojo to list of OrderData
	public List<OrderData> getOrderDataList(List<OrderPojo> list) {
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo orderPojo : list) {
			OrderData orderData = modelMapper.map(orderPojo, OrderData.class);
			list2.add(orderData);
		}
		return list2;
	}
}
