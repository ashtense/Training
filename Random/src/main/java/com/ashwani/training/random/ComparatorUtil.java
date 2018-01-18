package com.ashwani.training.random;

import java.util.Comparator;

public class ComparatorUtil {
 
	public static void main(String[] args) {
		Comparable<Employee> empCompara = new Comparable<Employee>() {
			
			public int compareTo(Employee o) {
				return 0;
			}
		};
		
		Comparator<Employee> empCom = new Comparator<Employee>() {

			public int compare(Employee o1, Employee o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		};
	}
}
