/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateTimeTest {

	public static void main(final String[] args) {
		final OffsetDateTime dateTime = OffsetDateTime.parse("2015-11-02T00:08:23.205-08:00");

		final DateTimeFormatter customFormat = new DateTimeFormatterBuilder()
				.appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").toFormatter();
		System.out.println(dateTime.format(customFormat));
		// yyyy-MM-dd'T'hh:mm:ss.SSSZ

		final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
				.toFormatter();
		System.err.println(dateTime.format(timeFormatter));
	}

}