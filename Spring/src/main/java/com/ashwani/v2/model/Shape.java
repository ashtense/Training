package com.ashwani.v2.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.*;

@Component
public class Shape {

	@Autowired
	private Circle circle;

	public Shape() {
		super();
		System.err.println("SHAPE CONSTRUCTOR COMMING UP");
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		System.err.println("Inside circle setter in shape object");
		this.circle = circle;
	}

	@PostConstruct
	public void init(){
		System.out.println("Bean is going through initialization");
	}
	
	@PreDestroy
	public void destroy(){
		System.out.println("Auto Self Destruct");
	}
}