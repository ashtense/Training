package com.ashwani.training.random;

public class ThisCheck {

	public ThisCheck(){
		System.err.println("default constructor");
	}
	
	public ThisCheck(int i){
		this();
		System.err.println(i);
	}
	
	public ThisCheck(String name){
		this(5);
		System.err.println(name);
	}
	
	public static void main(String[] args) {
		ThisCheck thisCheck = new ThisCheck("GERONimo");
	}
}
