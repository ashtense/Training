/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.util.HashMap;

public class HashTest {

	public static void main(final String[] args) {
		final Employee employee1 = new Employee(7L, "Ashwani Solanki");
		System.out.println(employee1.hashCode());

		final HashMap<String, String> lolMap = new HashMap<>();
		lolMap.put("abc", "great");
		System.out.println(lolMap.get("abc"));
		lolMap.put("abc", "geronimo");
		System.out.println(lolMap.get("abc"));
	}
}
