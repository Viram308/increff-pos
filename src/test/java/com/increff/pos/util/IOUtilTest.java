package com.increff.pos.util;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class IOUtilTest extends AbstractUnitTest{
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
