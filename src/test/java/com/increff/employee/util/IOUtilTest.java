package com.increff.employee.util;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilTest {
	@Test
	public void testIOUtil() throws IOException {
		IOUtil i = new IOUtil();
		i.getClass();
		IOUtil.closeQuietly(null);
		String fileClasspath = "/com/increff/employee/test.properties";
		InputStream is = IOUtilTest.class.getResourceAsStream(fileClasspath);
		IOUtil.closeQuietly(is);
	}
}
