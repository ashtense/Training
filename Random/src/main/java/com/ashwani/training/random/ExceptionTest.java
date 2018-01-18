package com.ashwani.training.random;

public class ExceptionTest {

	public static void main(String[] args) {
		Boolean bla = false;
		try {
			if (!bla) {
				throw new ClassCastException();
			}
		} catch (ClassCastException cce) {
			System.err.println();
		}
	}
}
