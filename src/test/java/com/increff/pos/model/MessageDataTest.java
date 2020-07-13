package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageDataTest {
	@Test
	public void testMessageData() {
		MessageData messageData = new MessageData();
		String message = "no message";
		messageData.setMessage(message);
		assertEquals(message, messageData.getMessage());
	}
}
