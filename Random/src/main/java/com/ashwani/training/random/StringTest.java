/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import org.apache.commons.lang3.StringUtils;

public class StringTest {

	public static void main(final String[] args) {
		final String str1 = "abc";
		final String str2 = "def";

		if (str1 != str2) {
			System.err.println("First case");
		}
		if (!str1.equals("")) {
			System.err.println("Second case");
		}
		System.err.println(Boolean.FALSE.toString());

		final StringBuilder testString = new StringBuilder("HelloGeronimoOperation");
		testString.replace(testString.length() - 9, testString.length(), "");
		System.out.println(testString.toString() + " ************************************** ");

		final String testStringNew = "WES__Worker__Effective_Change__Person_Communication__Address__Usage--Address_Field2";
		final String[] split = StringUtils.split(testStringNew, "--");
		System.err.println(split.length);
	}
}
