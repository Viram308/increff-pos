package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class MessageDataTest extends AbstractUnitTest {

	// test for MessageData
	
	@Test
	public void testMessageData() {
		MessageData messageData = new MessageData();
		String message = "no message";
		messageData.setMessage(message);
		assertEquals(message, messageData.getMessage());
	}
}
