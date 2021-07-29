package com.ashwani.special.backup.xmlprocessor;

public class CustomField {

	private String name;
	private int sequenceNo;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(final int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public String toString() {
		return name;
	}

}
