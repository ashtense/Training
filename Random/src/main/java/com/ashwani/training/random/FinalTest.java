package com.ashwani.training.random;

import java.util.ArrayList;
import java.util.List;

public class FinalTest {

	public static void main(String[] args) {
		final Employee employee1 = new Employee(2L, "as");
		System.err.println(employee1.getId());

		// employee1 = new Employee(4, "hola"); // This cannot happen as the
		// reference variable is final
		// and cannot be reassigned to
		// another object instance.

		employee1.setName("pa");
		System.err.println(employee1.getName());

		final List<Employee> lstEmployee = new ArrayList<>();
		lstEmployee.add(employee1);

		final int jk = 2;
		final String strAss = "HELLO";

		System.out.println(strAss);
	}
}
