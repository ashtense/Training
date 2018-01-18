package com.ashwani.training.random;

public class StringSplit {

	public static void main(String[] args) {
		String strURL = "http://digistyle.bonprix.net/images/9/8/2/2/prev_15029822.jpg";
		System.err.println(strURL.split("[/]")[strURL.split("[/]").length-1]);
		
		String strParam = "Pantaloni-stretch Bengalin, \"straight\" kjgkjgjk";
		strParam = strParam.contains("&") ? strParam.replace("&", "&amp;") : strParam;
		strParam = strParam.contains("\"") ? strParam.replace("\"", "&quot;") : strParam;
		strParam = strParam.contains("'") ? strParam.replace("'", "&apos;") : strParam;
		strParam = strParam.contains("<") ? strParam.replace("<", "&lt;") : strParam;
		strParam = strParam.contains(">") ? strParam.replace(">", "&gt;") : strParam;
		System.err.println(strParam);
	}
}