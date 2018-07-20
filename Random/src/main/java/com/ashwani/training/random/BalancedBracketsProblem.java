package com.ashwani.training.random;

public class BalancedBracketsProblem {

	public static void main(String[] args) {
		final String exampleString = "{[geronimo]}";
		final char[] charArray = exampleString.toCharArray();
		final char[] sequenceOpeningArray = new char[3];
		final char[] sequenceClosingArray = new char[3];
		for (final char c : charArray) {
			if ((c == '{') || (c == '[')) {
				sequenceOpeningArray[sequenceOpeningArray.length + 1] = c;
			}
			if ((c == '}') || (c == ']')) {
				sequenceClosingArray[sequenceClosingArray.length + 1] = c;
			}
		}
		for (int i = 0; i < 3; i++) {

		}
	}

	private static Boolean searchInArray(final char[] incommingArray, char searchTarget) {
		for (final char c : incommingArray) {
			if (c == searchTarget) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
