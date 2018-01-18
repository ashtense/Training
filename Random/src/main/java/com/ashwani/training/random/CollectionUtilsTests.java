package com.ashwani.training.random;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class CollectionUtilsTests {

	public static void main(String[] args) {
		List<Employee> lstEmp1 = Arrays.asList(new Employee(1L, "Ashwani"), new Employee(2L, "Parag"));
		List<Employee> lstEmp2 = Arrays.asList(new Employee(1L, "Ashwani"), new Employee(3L, "Haridas"));

		List<Employee> lstEmp3 = (List<Employee>) CollectionUtils.subtract(lstEmp1, lstEmp2);

		System.out.println(CollectionUtils.isEqualCollection(lstEmp1, lstEmp2));
		for (Employee emp : lstEmp2) {
			System.out.println(emp.getId() + " _ " + emp.getName());
		}
		System.out.println("--------------------");

		for (Employee emp : lstEmp3) {
			System.out.println(emp.getId() + " _ " + emp.getName());
		}
		System.out.println("--------------------");

		Set<Employee> setEmp1 = new HashSet<>(lstEmp1);
		Set<Employee> setEmp2 = new HashSet<>(lstEmp2);
		setEmp2.removeAll(setEmp1);
		for (Employee emp : setEmp2) {
			System.out.println(emp.getId() + " _ " + emp.getName());
		}
	}
}
