package com.ashwani.special.backup.random.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DuplicateNumberProblem {

	/*
	 * Given an array nums containing n +1 integers where each integer is between 1
	 * and n (inclusive), prove that at least one duplicate number must exist.Assume
	 * that there is only one duplicate number, find the duplicate one.
	 */

	@SuppressWarnings("resource")

	public static void main(final String[] args) {
		final Scanner input = new Scanner(System.in);
		// Create a new array. The user enters the size
		final int[] array = new int[input.nextInt()];
		// Get the value of each element in the array
		for (int i = 0; i < array.length; i++) {
			array[i] = input.nextInt();
		}

		final Set<Integer> arrSet = new HashSet<>();
		for (final int incomming : array) {
			if (!arrSet.add(incomming)) {
				System.out.println(incomming);
			}
		}
	}

}
