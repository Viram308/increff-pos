package com.increff.employee.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {

	public static void closeQuietly(Closeable c) throws IOException {
		if (c == null) {
			return;
		}
		c.close();
	}

}
