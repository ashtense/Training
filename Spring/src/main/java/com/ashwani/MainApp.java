package com.ashwani;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		/**
		 * Bean Factory doesn't contain enterprise level functions
		 * 		like for instance registerShutdownHook() method hats why we have used 
		 * 		AbstractApplicaitonContext instead. Although it inherits BeanFactory context but 
		 * 		beanFactory context is kind of deprecated and everyone is using new context 
		 * 		for much more functionality and control.
		 */
		//BeanFactory context = new ClassPathXmlApplicationContext("Beans.xml");'
		
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		HelloWorld obj = (HelloWorld) context.getBean("helloWorld");
		obj.getMessage();
		context.registerShutdownHook();
	}
}
