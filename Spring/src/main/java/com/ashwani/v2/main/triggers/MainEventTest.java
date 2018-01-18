package com.ashwani.v2.main.triggers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ashwani.classconfig.HelloWorldConfig;


public class MainEventTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldConfig.class);
		context.start();
	}
}