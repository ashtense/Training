package com.ashwani.training.random;

import java.util.Map;
import java.util.TreeMap;

public class SortedSetTest {

	public static void main(String[] args) {
		Map<String, Integer> mapper = new TreeMap<>();
		mapper.put("Q", 1);
		mapper.put("B", 1);
		mapper.put("D", 1);

		for (Map.Entry<String, Integer> mapObject : mapper.entrySet()) {
			String key = mapObject.getKey();
			Integer value = mapObject.getValue();

			System.err.println(key + " - " + value);
		}

		long startTime = System.currentTimeMillis();

		if (mapper.containsKey("B")) {
			System.err.println(mapper.get("B"));
		}

		long stopTime = System.currentTimeMillis();

		System.err.println(stopTime - startTime);
	}
}