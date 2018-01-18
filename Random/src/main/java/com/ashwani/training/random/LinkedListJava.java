/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.util.LinkedList;

public class LinkedListJava {

	public static void main(final String[] args) {
		final LinkedList<String> lstLinked = new LinkedList<>();
		lstLinked.add("Ashwani");
		lstLinked.add("Solanki");

		lstLinked.add(1, "Geronimo");

		lstLinked.forEach(action -> {
			System.out.println(action);
		});

		final StringBuilder tempData = new StringBuilder();
		lstLinked.forEach(action -> {
			if (tempData.length() == 0) {
				tempData.append(action.toString());
			} else {
				tempData.append("|" + action.toString());
			}
		});
		System.out.println(tempData.toString());
	}
}
