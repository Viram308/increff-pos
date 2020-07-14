package com.increff.pos.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PasswordUtil {
	public static String getHash(String password) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes("UTF-8"));
		return String.format("%032x", new BigInteger(1, md.digest()));
	}
}
