package com.ashwani.training.random;

import java.time.Instant;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class InstantTest {

	public static void main(final String[] args) {
		final Instant currentInstant = Instant.now();

		final LocalTime currentTime = LocalTime.now();
		System.out.println(currentTime.toString());

		final Instant plus = Instant.now().plus(Period.ofYears(100).getYears(), ChronoUnit.YEARS);
		System.out.println("LOL: " + plus.toString());

		// Instant.now().plus(5 * 60L * 60L * 1000L);

	}
}
