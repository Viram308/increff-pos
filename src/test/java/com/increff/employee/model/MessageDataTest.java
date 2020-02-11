package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class MessageDataTest extends AbstractUnitTest {

	@Test
	public void testMessageData() {
		MessageData m = new MessageData();
		String message = "valid";
		m.setMessage(message);
		assertEquals(message, m.getMessage());
	}

}
