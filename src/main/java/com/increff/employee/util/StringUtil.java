package com.increff.employee.util;

public class StringUtil {

//	public static boolean isEmpty(String s) {
//		return s == null || s.trim().length() == 0;
//	}
	
	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	// function to generate a random string of length n
	public static String getAlphaNumericString() {

		// chose a Character random from this String
		String AlphaNumericString = "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(8);

		for (int i = 0; i < 8; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

}
