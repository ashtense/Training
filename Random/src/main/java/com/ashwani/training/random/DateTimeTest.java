/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateTimeTest {

	public static void main(final String[] args) {
		final OffsetDateTime dateTime = OffsetDateTime.parse("2015-11-02T00:08:23.205-08:00");

		final DateTimeFormatter customFormat = new DateTimeFormatterBuilder()
				.appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").toFormatter();
		System.out.println(dateTime.format(customFormat));
		// yyyy-MM-dd'T'hh:mm:ss.SSSZ

		final DateTimeFormatter dateOnlyFormat = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
				.toFormatter();
		System.err.println("Date format" + dateTime.format(dateOnlyFormat));

		final DateTimeFormatter timeOnlyFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").toFormatter();
		System.err.println("Time only format" + dateTime.format(timeOnlyFormat));

		/*
		 * ZonedDateTime zdi = ZonedDateTime.now(); String timeCapsule = zdi.getHour() +
		 * ":" + zdi.getMinute(); System.err.println(zdi.getHour() + ":" +
		 * zdi.getMinute());
		 *
		 * DateTimeFormatter fmt = new
		 * DateTimeFormatterBuilder().appendPattern("HH:m").toFormatter();
		 *
		 * ZonedDateTime incoming = ZonedDateTime.parse(timeCapsule, fmt);
		 * System.out.println("Geronimo" + incoming.toString());
		 */

		// 2018-02-08T13:15:56.326Z
		ZonedDateTime zdiNew = ZonedDateTime.now();
		final DateTimeFormatter customFormatForDas = new DateTimeFormatterBuilder()
				.appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").toFormatter();
		System.err.println("LOL: " + zdiNew.format(customFormatForDas));
	}

}