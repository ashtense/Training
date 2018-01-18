package com.ashwani.classconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ashwani.HelloWorld;
import com.ashwani.v2.event.CStartEventHandler;

@Configuration
public class HelloWorldConfig {

	@Bean
	public HelloWorld helloWorld(){
		return new HelloWorld();
	}
	
	@Bean
	public CStartEventHandler getContextStartEvent(){
		return new CStartEventHandler();
	}
}
