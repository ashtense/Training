package com.ashwani.training.config.reflection;

public class TestObject {

	private final String demoString;

	public TestObject() {
		this.demoString = "Geronimo";
	}

	public String method1() {
		return "Geronimo1";
	}

	public String method2() {
		return "Geronimo2";
	}

	public String getDemoString() {
		return demoString;
	}

}
