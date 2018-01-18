package com.ashwani.v2.model;

import org.springframework.stereotype.Component;

@Component
public class Circle {

	public Circle() {
		super();
		System.err.println("CIRCLE CONSTRUCTOR COMMING UP");
	}

	public void doIt(){
		System.err.println("CHECKING THE FLOW");
	}
}