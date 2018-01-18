package com.ashwani.training.random;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalTest {
	
	public static void main(String[] args) {
		BigDecimal objBiggie = new BigDecimal(45.00);
		objBiggie = objBiggie.setScale(1, RoundingMode.HALF_DOWN);
		System.err.println(objBiggie);
		DecimalFormat decFormatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		System.err.println(decFormatter.format(objBiggie));
	}
}
