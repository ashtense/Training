package com.ashwani.locator;

public class LocatorCheck {

	public static void main(final String[] args) {
		final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LocatorConfig.class);
		final Object bean = context.getBean("geronimoImplementation1");
	}
}
