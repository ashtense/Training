/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalTest {

	public static void main(final String[] args) {
		final Map<String, String> testMap = new HashMap();
		final Optional<Map<String, String>> optionalTestMap = Optional.of(testMap);
		System.err.println(optionalTestMap.isPresent());

		final Person personObj = new Person("GERONIMO");
		final Optional<String> optionalName = Optional.ofNullable(personObj.getName());
		System.err.println(optionalName.isPresent());
		System.err.println(optionalName.get());
	}
}
