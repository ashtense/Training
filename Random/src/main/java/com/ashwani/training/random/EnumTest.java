/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.util.stream.Stream;

public class EnumTest {
	enum MandatoryRecordDataFields {
		Pay_Group_Country("ABC"), Payroll_Company_ID("DEF"), Pay_Group_ID("DEF");

		private final String tester;

		MandatoryRecordDataFields(final String tester) {
			this.tester = tester;
		}
	}

	public static void main(final String[] args) {
		for (final MandatoryRecordDataFields mandatoryRecordDataFields : MandatoryRecordDataFields.values()) {
			System.err.println(mandatoryRecordDataFields.name());
		}

		Stream.of(MandatoryRecordDataFields.values()).forEach(mandatoryField -> {
			System.out.println(mandatoryField.name());
		});

		Stream.of(MandatoryRecordDataFields.values()).forEach(drums -> {
			System.err.println(drums.tester);
		});
	}
}
