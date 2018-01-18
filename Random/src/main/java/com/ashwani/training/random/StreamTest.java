package com.ashwani.training.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamTest {

	public static void main(String[] args) {
		List<String> lstURl = new ArrayList<>();
		lstURl.add("http://digistyle.bonprix.net/thumb/0/2/1/1/prev_15030211.jpg");
		lstURl.add("http://digistyle.bonprix.net/thumb/0/2/1/4/prev_15030214.jpg");
		lstURl.add("http://digistyle.bonprix.net/thumb/0/2/1/6/prev_15030216.jpg");

		System.err.println(lstURl	.stream()
									.count());
		List<String> lstNewUrl = lstURl	.stream()
										.filter(e -> e.contains("211"))
										.collect(Collectors.toList());
		lstURl	.stream()
				.filter(e -> e.contains("211"))
				.forEach(System.out::println);
		System.err.println(lstURl	.stream()
									.findFirst());
		System.err.println(lstNewUrl.stream()
									.count());

		List<Employee> lstEmployee = new ArrayList<>();
		/*
		 * lstEmployee.add(new Employee(12, "Ashwani")); lstEmployee.add(new
		 * Employee(13, "Singh")); lstEmployee.add(new Employee(14, "Solanki"));
		 */
		for (Employee employee : lstEmployee) {
			System.err.println("Hello");
		}

		Map<Object, List<Employee>> mapNewEmployee = lstEmployee.stream()
																.distinct()
																.collect(Collectors.groupingBy(emp -> emp	.getName()
																											.startsWith("S")));
		System.err.println(mapNewEmployee	.get(Boolean.TRUE)
											.size()
				+ " DRUMS");

		Boolean isExisting = lstEmployee.stream()
										.anyMatch(employee -> employee	.getName()
																		.equalsIgnoreCase("ashwani"));
		System.err.println(isExisting);

		Boolean isName = lstEmployee.stream()
									.allMatch(employee -> employee	.getName()
																	.contains("a"));
		System.err.println(isName);

		Map<String, String> testMap = new HashMap<>();
		testMap.put("HEY", "LOL");
		testMap.put("HEY", "LOL!");
		System.err.println(testMap.containsKey("HEYLO"));
		System.err.println(testMap.get("HOLA"));
	}

}