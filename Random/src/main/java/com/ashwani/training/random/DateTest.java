/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTest {

	public static void main(final String[] args) {
		/*
		 * Date date = new Date(); String DATE_FORMAT = "MM/dd/yyyy";
		 * SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		 * System.out.println("Today is " + sdf.format(date));
		 */

		final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(Locale.GERMANY);
		final LocalDate localDate = LocalDate.of(1992, 01, 18);
		System.err.println(localDate.format(formatter));

		final Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		System.err.println(timeStamp.toString());
		System.out.println("Hello everyone ");
	}
}