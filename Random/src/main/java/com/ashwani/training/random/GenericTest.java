package com.ashwani.training.random;

import java.util.ArrayList;
import java.util.List;

public class GenericTest {

	public static void main(String[] args) {
		List<Employee> lstEmployee = new ArrayList<Employee>();
		lstEmployee.add(new Employee(123L, "GERONIMO"));
		List<Object> lstO = (List<Object>) parseLists(lstEmployee);
		System.err.println(lstO	.get(0)
								.toString());
	}

	public static List<?> parseLists(List<?> lstO) {
		List<Object> lstReturn = new ArrayList<>();
		if (lstO.get(0) instanceof Employee) {
			List<Employee> lstEmployee = new ArrayList<>();
			for (Object obj : lstO) {
				lstReturn.add(parseCycle((Employee) obj));
			}
		}
		System.out.println(lstReturn.size());
		return lstReturn;
	}

	public static Cycle parseCycle(Employee objEmployee) {
		Cycle cyc = new Cycle();
		// cyc.setId(objEmployee.getId().to);
		cyc.setName(objEmployee.getName());
		return cyc;
	}
}