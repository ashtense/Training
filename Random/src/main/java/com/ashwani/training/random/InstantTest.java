package com.ashwani.training.random;

import java.time.Instant;
import java.time.LocalTime;

public class InstantTest {

	public static void main(String[] args) {
		Instant currentInstant = Instant.now();

		LocalTime currentTime = LocalTime.now();
		System.out.println(currentTime.toString());
	}
}
