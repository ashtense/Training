package com.ashwani.training.random;

import java.util.ArrayList;
import java.util.List;

public class CollectionMergeTest {

	public static void main(String[] args) {
		final Employee emp1 = new Employee(8007l, "Ashwani");
		final Employee emp2 = new Employee(8008l, "Solanki");

		final List<Employee> lstOriginal = new ArrayList<>();
		final List<Employee> lstMaster = new ArrayList<>();
		lstOriginal.add(emp1);
		lstOriginal.add(emp2);

		lstMaster.add(emp1);
		lstMaster.add(new Employee(879l, "Roma"));

		lstMaster.removeAll(lstOriginal);
		lstMaster.addAll(lstOriginal);
		System.out.println(lstMaster.size());

	}
}
