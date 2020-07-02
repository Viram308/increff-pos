package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;

@Component
public class OrderItemDto {
	@Autowired
	private ModelMapper modelMapper;

	// Converts OrderItemPojo to OrderItemData
	public OrderItemData convertOrderItemPojotoOrderItemData(OrderItemPojo orderItemPojo) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return modelMapper.map(orderItemPojo, OrderItemData.class);
	}

	// Converts OrderItemForm to OrderItemPojo
	public OrderItemPojo convertOrderItemFormtoOrderItemPojo(OrderItemForm orderItemForm) throws ApiException {
		checkEnteredQuantity(orderItemForm);
		return modelMapper.map(orderItemForm, OrderItemPojo.class);
	}

	// Converts list of OrderItemPojo to list of OrderItemData
	public List<OrderItemData> getOrderItemDataList(List<OrderItemPojo> list) {
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo orderItemPojo : list) {
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			OrderItemData orderItemData = modelMapper.map(orderItemPojo, OrderItemData.class);
			list2.add(orderItemData);
		}
		return list2;
	}

	public void checkEnteredQuantity(OrderItemForm f) throws ApiException {
		if (f.getQuantity() <= 0) {
			throw new ApiException("Quantity can not be negative or zero !!");
		}
	}
}
