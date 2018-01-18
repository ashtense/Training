package com.ashwani.training.random;

import java.util.Arrays;
import java.util.List;

public class ArraySplitTest {

	public static void main(String[] args) {
		String arrTestString = "WES__Worker__Effective_Change__Compensation,WES__Worker__Effective_Change__Compensation__Compensation_Summary_Based_on_Compensation_Grade,WES__Worker__Effective_Change__Compensation__Compensation_Summary_in_Annualized_Frequency,WES__Worker__Effective_Change__Compensation__Compensation_Summary_in_Pay_Group_Frequency";
		List<String> stringList = Arrays.asList(arrTestString.split(","));
		List<String> subList = stringList.subList(1, stringList.size());
		subList.forEach(subs -> {
			System.err.println(subs);
		});
	}
}
