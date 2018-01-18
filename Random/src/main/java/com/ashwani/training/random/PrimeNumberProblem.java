/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

public class PrimeNumberProblem {

	public static void main(final String[] args) {
		for (int i = 0; i < 35; i++) {
			if (isPrime(i, i - 1)) {
				System.out.println(" " + i + " is Prime");
			}
		}
	}

	static boolean isPrime(final int num, final int div) {
		if (div <= 1) {
			return true;
		}
		if ((num % div) == 0) {
			return false;
		}
		return isPrime(num, div - 1);
	}

}
