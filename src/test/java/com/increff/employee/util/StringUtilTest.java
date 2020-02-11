package com.increff.employee.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testStringUtil() {
		StringUtil s = new StringUtil();
		s.getClass();
		String t = StringUtil.toLowerCase(null);
		assertEquals(null, t);
		t = StringUtil.toLowerCase(" VirAm  ");
		assertEquals("viram", t);
		t = StringUtil.getAlphaNumericString();
		assertEquals(8, t.length());
	}

}
