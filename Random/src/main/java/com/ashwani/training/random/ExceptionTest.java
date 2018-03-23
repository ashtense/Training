package com.ashwani.training.random;

public class ExceptionTest {

	public static void main(String[] args) {
		final Boolean bla = false;
		try {
			if (!bla) {
				throw new ClassCastException();
			}
		} catch (final ClassCastException cce) {
			System.err.println("Geronimo");
		} finally {
			System.err.println("LOL");

		}
	}
}
