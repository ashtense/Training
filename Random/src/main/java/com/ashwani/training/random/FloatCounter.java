package com.ashwani.training.random;

import java.text.DecimalFormat;

public class FloatCounter {

	public static void main(String[] args) {
		double dx = 41;
		DecimalFormat formatSpecs = new DecimalFormat("#0.00");
		for (int i = 0; i < 50; i++) {
			dx += 0.01;
			// System.out.println(Double.valueOf(formatSpecs.format(dx)).toString());
		}

		double dx1 = 41;
		System.out.println((dx1 % 1) == 0);
	}
}
