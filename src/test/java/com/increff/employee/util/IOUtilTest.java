package com.increff.employee.util;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilTest {
	// test IOUtil
	@Test
	public void testIOUtil() throws IOException {
		IOUtil i = new IOUtil();
		i.getClass();
		// test for null stream
		IOUtil.closeQuietly(null);
		String fileClasspath = "/com/increff/employee/test.properties";
		// test for file stream
		InputStream is = IOUtilTest.class.getResourceAsStream(fileClasspath);
		IOUtil.closeQuietly(is);
	}
}
