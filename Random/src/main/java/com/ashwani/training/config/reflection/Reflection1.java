package com.ashwani.training.config.reflection;

public class Reflection1 {

	public static void main(final String[] args) {
		final TestObject testObject = new TestObject();

		final Class cls = testObject.getClass();

		System.out.println("Name of class is: " + cls.getSimpleName());

		System.out.println(cls.getDeclaredFields()[0].getName());
	}
}
