package com.ashwani.training.random;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeZoneTest {

	public static void main(String[] args) {
		ZonedDateTime zdt = ZonedDateTime.now();
		// final DateTimeFormatter customFormatForDas = new
		// DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").toFormatter();
		final DateTimeFormatter customFormatForDas = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss.SSS'Z'")
				.toFormatter();
		System.out.println(zdt.format(customFormatForDas));
		String formattedString = zdt.format(customFormatForDas);

		ZonedDateTime zdtTester = ZonedDateTime.parse(formattedString, customFormatForDas);
		System.out.println(zdtTester.toString());
	}
}