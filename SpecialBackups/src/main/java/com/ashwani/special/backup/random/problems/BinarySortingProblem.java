package com.ashwani.special.backup.random.problems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinarySortingProblem {

	/**
	 * GOT FROM CROSSOVER
	 * <p>
	 * Complete the function below. DONOT MODIFY anything outside this function!
	 * </p>
	 */
	static int[] rearrange(final int[] elements) {
		final List<Integer> tempList = new ArrayList<>();
		for (final int i : elements) {
			if (!tempList.contains(i)) {
				tempList.add(i);
			}
		}
		tempList.sort((o1, o2) -> {
			final long countOf1 = Integer.toBinaryString(o1).chars().filter(ch -> ch == '1').count();
			final long countOf2 = Integer.toBinaryString(o2).chars().filter(ch -> ch == '1').count();
			if (countOf1 > countOf2) {
				return 1;
			} else if (countOf2 > countOf1) {
				return -1;
			} else if (countOf2 == countOf1) {
				if (o1 > o2) {
					return 1;
				} else {
					return -1;
				}
			}
			return 0;
		});
		final int[] arrResult = new int[tempList.size()];
		for (int i = 0; i < arrResult.length; i++) {
			arrResult[i] = tempList.get(i);
		}
		return arrResult;
	}

	/**
	 * DO NOT MODIFY THIS METHOD!
	 */
	public static void main(final String[] args) throws IOException {
		final int[] elements = new int[] { 5, 5, 3, 7, 10, 14 };

		// call rearrange function
		final int[] results = rearrange(elements);

		for (final int result : results) {
			System.out.println(String.valueOf(result));
		}
	}
}