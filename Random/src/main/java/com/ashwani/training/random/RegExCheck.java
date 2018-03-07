package com.ashwani.training.random;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExCheck {

	public static void main(String[] args) {
		Pattern pt = Pattern.compile("[0-9]+");
		Matcher m2 = pt.matcher("12");
		System.err.println(m2.matches());

		String str1 = "12a";
		System.err.println(str1.matches("[0-9]+"));

		Pattern pt1 = Pattern.compile("^[^~!#^]*$");
		Matcher mt1 = pt1.matcher("Hello#");
		System.out.println(mt1.matches());
	}
}
